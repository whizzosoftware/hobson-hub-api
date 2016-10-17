/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.job.AsyncJobHandle;
import com.whizzosoftware.hobson.api.job.JobManager;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.disco.DeviceAdvertisement;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.*;
import com.whizzosoftware.hobson.api.hub.HobsonHub;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.variable.*;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A base class that implements several HobsonPlugin methods. This provides a good starting point for third-party
 * HobsonPlugin implementations.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHobsonPlugin implements HobsonPlugin, EventLoopExecutor, ActionExecutionContext {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHobsonPlugin.class);

    private PluginContext context;
    private String version;
    private PropertyContainerClass configurationClass;
    private PluginStatus status;
    private DeviceManager deviceManager;
    private DiscoManager discoManager;
    private EventManager eventManager;
    private HubManager hubManager;
    private JobManager jobManager;
    private PluginManager pluginManager;
    private TaskManager taskManager;
    private TaskProvider taskProvider;
    private EventLoopGroup eventLoop;
    private final Map<String,HobsonDeviceProxy> devices = Collections.synchronizedMap(new HashMap<String,HobsonDeviceProxy>());

    public AbstractHobsonPlugin(String pluginId, String version) {
        this(pluginId, version, new LocalEventLoopGroup(1));
    }

    AbstractHobsonPlugin(String pluginId, String version, EventLoopGroup eventLoop) {
        this.context = PluginContext.create(HubContext.createLocal(), pluginId);
        this.version = version;
        this.eventLoop = eventLoop;
        this.configurationClass = new PropertyContainerClass(PropertyContainerClassContext.create(getContext(), "configurationClass"), PropertyContainerClassType.PLUGIN_CONFIG);

        // register any supported properties the subclass needs
        TypedProperty[] props = getConfigurationPropertyTypes();
        if (props != null) {
            for (TypedProperty p : props) {
                this.configurationClass.addSupportedProperty(p);
            }
        }
    }

    @Override
    public AsyncJobHandle executeDeviceAction(String deviceId, String actionId, final PropertyContainer properties) {
        HobsonDeviceProxy device = devices.get(deviceId);
        return executeActionClass(actionId, device.getActionClass(actionId), properties);
    }

    @Override
    public AsyncJobHandle executePluginAction(PropertyContainer properties) {
        String actionId = properties.getContainerClassContext().getContainerClassId();
        return executeActionClass(actionId, getActionClass(actionId), properties);
    }

    private AsyncJobHandle executeActionClass(String actionId, ActionClass ac, final PropertyContainer properties) {
        if (ac != null) {
            return jobManager.createJob(getContext(), ac.newInstance(this, properties, getEventLoopExecutor()), ac.getTimeoutInterval());
        } else {
            throw new HobsonNotFoundException("No action found: " + actionId);
        }
    }

    @Override
    public PluginContext getContext() {
        return context;
    }

    protected void setStatus(PluginStatus status) {
        this.status = status;
    }

    @Override
    public HobsonLocalPluginDescriptor getDescriptor() {
        HobsonLocalPluginDescriptor d = new HobsonLocalPluginDescriptor(getContext(), getType(), getName(), getDescription(), version, status);
        d.setConfigurationClass(configurationClass);
        d.setActionClasses(getActionClasses());
        return d;
    }

    abstract protected String getName();

    protected String getDescription() {
        return null;
    }

    protected PluginType getType() {
        return PluginType.PLUGIN;
    }

    /*
     * HobsonPlugin methods
     */

    protected HobsonDeviceProxy getDeviceProxy(String deviceId) {
        HobsonDeviceProxy p = devices.get(deviceId);
        if (p == null) {
            throw new HobsonNotFoundException("Device not found");
        } else {
            return p;
        }
    }

    protected ActionClass getActionClass(String actionId) {
        validateTaskManager();
        return taskManager.getActionClass(PropertyContainerClassContext.create(getContext(), actionId));
    }

    protected Collection<ActionClass> getActionClasses() {
        validateTaskManager();
        return taskManager.getActionClasses(getContext());
    }

    protected HobsonDeviceProxy getProxyDevice(String deviceId) {
        if (devices.containsKey(deviceId)) {
            return devices.get(deviceId);
        } else {
            throw new HobsonNotFoundException("No device found with ID: " + deviceId);
        }
    }

    @Override
    public Object getDeviceConfigurationProperty(String deviceId, String name) {
        validateDeviceManager();
        return deviceManager.getDeviceConfigurationProperty(DeviceContext.create(getContext(), deviceId), name);
    }

    @Override
    public Long getDeviceLastCheckin(String deviceId) {
        return devices.get(deviceId).getLastCheckin();
    }

    @Override
    public DeviceVariableState getDeviceVariableState(String deviceId, String name) {
        return devices.get(deviceId).getVariableState(name);
    }

    @Override
    public String[] getEventTopics() {
        return null;
    }

    @Override
    public long getRefreshInterval() {
        return 0;
    }

    @Override
    public TaskProvider getTaskProvider() {
        return taskProvider;
    }

    public void setTaskProvider(TaskProvider taskProvider) {
        this.taskProvider = taskProvider;
    }

    @Override
    public boolean hasTaskProvider() {
        return (taskProvider != null);
    }

    /*
     * HobsonPluginRuntime methods
     */

    @Override
    public EventLoopExecutor getEventLoopExecutor() {
        return this;
    }

    @Override
    public void postHubEvent(HobsonEvent event) {
        validateEventManager();
        eventManager.postEvent(HubContext.createLocal(), event);
    }

    @Override
    public void postPluginEvent(final String name, final Object o) {
        validateJobManager();
        jobManager.postEvent(getContext(), name, o);
    }

    @Override
    public void onDeviceConfigurationUpdate(String deviceId, PropertyContainer config) {
        getDeviceProxy(deviceId).onDeviceConfigurationUpdate(config);
    }

    @Override
    public void onDeviceUpdate(HobsonDeviceProxy device) {
        validateDeviceManager();
        deviceManager.updateDevice(device.getDescriptor());
    }

    @Override
    public void onHobsonEvent(final HobsonEvent event) {
        if (hasTaskProvider()) {
            if (event instanceof TaskRegistrationEvent) {
                getTaskProvider().onRegisterTasks(((TaskRegistrationEvent)event).getTasks());
            } else if (event instanceof TaskCreatedEvent) {
                getTaskProvider().onCreateTask(((TaskCreatedEvent)event).getTask());
            } else if (event instanceof TaskUpdatedEvent) {
                getTaskProvider().onUpdateTask(((TaskUpdatedEvent)event).getTask());
            } else if (event instanceof TaskDeletedEvent) {
                getTaskProvider().onDeleteTask(((TaskDeletedEvent)event).getContext());
            }
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onSetDeviceVariable(String deviceId, String variableName, Object value) {
        getDeviceProxy(deviceId).onSetVariable(variableName, value);
    }

    protected void publishActionClass(ActionClass actionClass) {
        taskManager.publishActionClass(actionClass);
    }

    protected void publishTaskConditionClass(TaskConditionClass conditionClass) {
        validateTaskManager();
        taskManager.publishConditionClass(conditionClass);
    }

    @Override
    public void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit) {
        eventLoop.scheduleAtFixedRate(runnable, initialDelay, time, unit);
    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext dctx, PropertyContainerClass configClass, String name, Object value) {
        validateDeviceManager();
        deviceManager.setDeviceConfigurationProperty(dctx, configClass, name, value);
    }

    @Override
    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Override
    public void setDiscoManager(DiscoManager discoManager) {
        this.discoManager = discoManager;
    }

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void setHubManager(HubManager hubManager) {
        this.hubManager = hubManager;
    }

    @Override
    public void setJobManager(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /*
     * EventLoopExecutor methods
     */

    @Override
    public Future executeInEventLoop(final Runnable runnable) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Throwable t) {
                    logger.error("An uncaught exception was thrown in the event loop", t);
                }
            }
        });
    }

    /*
     * Other methods
     */

    /**
     * Returns an array of configuration properties the plugin supports. These will automatically
     * be registered with the plugin's configuration class.
     *
     * @return an array of TypedProperty objects (or null if there are none)
     */
    abstract protected TypedProperty[] getConfigurationPropertyTypes();

    /**
     * Returns a File for the plugin's directory sandbox.
     *
     * @return a File object
     */
    protected File getDataDirectory() {
        return pluginManager.getDataDirectory(getContext());
    }

    /**
     * Returns a File located in the plugin's directory sandbox.
     *
     * @param filename the filename
     *
     * @return a File instance
     */
    protected File getDataFile(String filename) {
        return pluginManager.getDataFile(getContext(), filename);
    }

    /**
     * Sets a plugin configuration property.
     *
     * @param ctx the context of the target plugin
     * @param name the name of the property
     * @param value the value of the property
     */
    protected void setPluginConfigurationProperty(PluginContext ctx, String name, Object value) {
        validatePluginManager();
        pluginManager.setLocalPluginConfigurationProperty(ctx, name, value);
    }

    protected Future publishDeviceProxy(HobsonDeviceProxy proxy) {
        PropertyContainer config = deviceManager != null ? deviceManager.getDeviceConfiguration(proxy.getContext()) : null;
        return publishDeviceProxy(proxy, config != null ? config.getPropertyValues() : null);
    }

    protected Future publishDeviceProxy(final HobsonDeviceProxy proxy, final Map<String,Object> config) {
        validateDeviceManager();
        logger.trace("Publishing device: {}", proxy.getContext());
        return deviceManager.publishDevice(proxy, config, new Runnable() {
            @Override
            public void run() {
                String deviceId = proxy.getContext().getDeviceId();
                devices.put(deviceId, proxy);
            }
        });
    }

    /**
     * Requests all currently known device advertisements. This is an asynchronous call that will be serviced
     * as multiple DeviceAdvertisementEvent events to the plugin's onHobsonEvent() callback.
     *
     * @param protocolId the protocol ID for the advertisements requested
     */
    protected void requestDeviceAdvertisementSnapshot(final String protocolId) {
        validateDiscoManager();
        // this is called in the plugin event loop so that it can safely be invoked in the onStartup() method
        executeInEventLoop(new Runnable() {
            @Override
            public void run() {
            discoManager.requestExternalDeviceAdvertisementSnapshot(getContext(), protocolId);
            }
        });
    }

    /**
     * Publishes a new DeviceAdvertisement.
     *
     * @param advertisement the advertisement to publish
     */
    public void publishDeviceAdvertisement(DeviceAdvertisement advertisement, boolean internal) {
        validateDiscoManager();
        discoManager.publishDeviceAdvertisement(HubContext.createLocal(), advertisement, internal);
    }

    protected void executeTask(TaskContext context) {
        validateTaskManager();
        taskManager.fireTaskTrigger(context);
    }

    protected Collection<HobsonDeviceDescriptor> getPublishedDeviceDescriptions() {
        validateDeviceManager();
        return deviceManager.getDevices(getContext());
    }

    protected HobsonHub getLocalHub() {
        validateHubManager();
        return hubManager.getHub(HubContext.createLocal());
    }

    protected DiscoManager getDiscoManager() {
        return discoManager;
    }

    protected HubManager getHubManager() {
        return hubManager;
    }

    protected TaskManager getTaskManager() {
        return taskManager;
    }

    protected Future setDeviceVariable(DeviceVariableContext dvctx, Object value) {
        return deviceManager.setDeviceVariable(dvctx, value);
    }

    protected Future setDeviceVariables(Map<DeviceVariableContext,Object> values) {
        return deviceManager.setDeviceVariables(values);
    }

    private void validateDeviceManager() {
        if (deviceManager == null) {
            throw new HobsonRuntimeException("No device manager has been set");
        }
    }

    private void validateDiscoManager() {
        if (discoManager == null) {
            throw new HobsonRuntimeException("No disco manager has been set");
        }
    }

    private void validateEventManager() {
        if (eventManager == null) {
            throw new HobsonRuntimeException("No event manager has been set");
        }
    }

    private void validateHubManager() {
        if (hubManager == null) {
            throw new HobsonRuntimeException("No hub manager has been set");
        }
    }

    private void validateJobManager() {
        if (jobManager == null) {
            throw new HobsonRuntimeException("No job manager has been set");
        }
    }

    private void validatePluginManager() {
        if (pluginManager == null) {
            throw new HobsonRuntimeException("No plugin manager has been set");
        }
    }

    private void validateTaskManager() {
        if (taskManager == null) {
            throw new HobsonRuntimeException("No task manager has been set");
        }
    }

    protected void setGlobalVariable(String name, Object value, long timestamp) {
        hubManager.setGlobalVariable(GlobalVariableContext.create(getContext(), name), value, timestamp);
    }

    protected void setGlobalVariables(Map<String,Object> values, long timestamp) {
        Map<GlobalVariableContext,Object> vars = new HashMap<>();
        for (String name : values.keySet()) {
            vars.put(GlobalVariableContext.create(getContext(), name), values.get(name));
        }
        hubManager.setGlobalVariables(vars, timestamp);
    }
}
