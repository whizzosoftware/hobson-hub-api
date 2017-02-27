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

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.*;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.event.device.DeviceConfigurationUpdateEvent;
import com.whizzosoftware.hobson.api.event.device.DeviceDeletedEvent;
import com.whizzosoftware.hobson.api.event.device.DeviceVariablesUpdateRequestEvent;
import com.whizzosoftware.hobson.api.event.plugin.PluginConfigurationUpdateEvent;
import com.whizzosoftware.hobson.api.event.plugin.PluginStatusChangeEvent;
import com.whizzosoftware.hobson.api.event.task.*;
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
abstract public class AbstractHobsonPlugin implements HobsonPlugin, EventLoopExecutor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHobsonPlugin.class);

    private PluginContext context;
    private String description;
    private String version;
    private PropertyContainerClass configurationClass;
    private PluginStatus status;
    private DeviceManager deviceManager;
    private DiscoManager discoManager;
    private EventManager eventManager;
    private HubManager hubManager;
    private ActionManager actionManager;
    private PluginManager pluginManager;
    private TaskManager taskManager;
    private TaskProvider taskProvider;
    private EventLoopGroup eventLoop;
    private final Map<String,HobsonDeviceProxy> devices = Collections.synchronizedMap(new HashMap<String,HobsonDeviceProxy>());

    public AbstractHobsonPlugin(String pluginId, String version, String description) {
        this(pluginId, version, description, new LocalEventLoopGroup(1));
    }

    AbstractHobsonPlugin(String pluginId, String version, String description, EventLoopGroup eventLoop) {
        this.context = PluginContext.create(HubContext.createLocal(), pluginId);
        this.version = version;
        this.description = description;
        this.eventLoop = eventLoop;
        this.configurationClass = new PropertyContainerClass(PropertyContainerClassContext.create(getContext(), "configurationClass"), PropertyContainerClassType.PLUGIN_CONFIG);
        this.status = PluginStatus.initializing();

        // register any supported properties the subclass needs
        TypedProperty[] props = getConfigurationPropertyTypes();
        if (props != null) {
            for (TypedProperty p : props) {
                this.configurationClass.addSupportedProperty(p);
            }
        }
    }

    @Override
    public PluginContext getContext() {
        return context;
    }

    @Override
    public PropertyContainerClass getConfigurationClass() {
        return configurationClass;
    }

    protected void setStatus(PluginStatus status) {
        this.status = status;
        if (eventManager != null) {
            eventManager.postEvent(getContext().getHubContext(), new PluginStatusChangeEvent(System.currentTimeMillis(), getContext(), status));
        }
    }

    @Override
    public HobsonLocalPluginDescriptor getDescriptor() {
        HobsonLocalPluginDescriptor d = new HobsonLocalPluginDescriptor(getContext(), getType(), getName(), getDescription(), version, status);
        d.setConfigurationClass(configurationClass);
        d.setActionClasses(getActionClasses());
        return d;
    }

    @Override
    public PluginStatus getStatus() {
        return status;
    }

    @EventHandler
    public void onHandleTaskEvents(final TaskEvent event) {
        if (hasTaskProvider()) {
            if (event instanceof TaskRegistrationEvent) {
                getTaskProvider().onRegisterTasks(((TaskRegistrationEvent)event).getTasks());
            } else if (event instanceof TaskUpdatedEvent) {
                TaskUpdatedEvent e = (TaskUpdatedEvent)event;
                if (e.getPluginId() == null || !getContext().getPluginId().equals(e.getPluginId())) {
                    getTaskProvider().onUpdateTask(e.getTask());
                }
            } else if (event instanceof TaskDeletedEvent) {
                getTaskProvider().onDeleteTask(((TaskDeletedEvent)event).getTask());
            }
        }
    }

    @EventHandler
    public void onDeviceConfigurationUpdateEvent(DeviceConfigurationUpdateEvent event) {
        if (event.getDeviceContext().getPluginId().equals(getContext().getPluginId())) {
            getDeviceProxy(event.getDeviceContext().getDeviceId()).onDeviceConfigurationUpdate(event.getConfiguration());
        }
    }

    @EventHandler
    public void onDeviceDeletedEvent(final DeviceDeletedEvent event) {
        if (event.getDeviceContext().getPluginId().equals(getContext().getPluginId())) {
            String deviceId = event.getDeviceContext().getDeviceId();
            HobsonDeviceProxy device = devices.get(deviceId);
            device.onShutdown();
            devices.remove(deviceId);
        }
    }

    @EventHandler
    public void onPluginConfigurationUpdateEvent(PluginConfigurationUpdateEvent event) {
        if (event.getPluginId().equals(getContext().getPluginId())) {
            onPluginConfigurationUpdate(event.getConfiguration());
        }
    }

    abstract public void onPluginConfigurationUpdate(PropertyContainer config);

    /**
     * Returns the device variable state for a device not created by this plugin.
     *
     * @param dvctx the device variable context
     *
     * @return the device state
     */
    protected DeviceVariableState getDeviceVariableState(DeviceVariableContext dvctx) {
        validateDeviceManager();
        return deviceManager.getDeviceVariable(dvctx);
    }

    protected boolean hasDeviceVariableState(DeviceVariableContext dvctx) {
        validateDeviceManager();
        return deviceManager.hasDeviceVariable(dvctx);
    }

    public void setTaskProvider(TaskProvider taskProvider) {
        this.taskProvider = taskProvider;
    }

    abstract protected String getName();

    protected String getDescription() {
        return description;
    }

    protected PluginType getType() {
        return PluginType.PLUGIN;
    }

    /*
     * HobsonPlugin methods
     */

    protected boolean hasDeviceProxy(String deviceId) {
        return devices.containsKey(deviceId);
    }

    protected HobsonDeviceProxy getDeviceProxy(String deviceId) {
        HobsonDeviceProxy p = devices.get(deviceId);
        if (p == null) {
            throw new DeviceNotFoundException(DeviceContext.create(getContext(), deviceId));
        } else {
            return p;
        }
    }

    protected Collection<HobsonDeviceProxy> getDeviceProxies() {
        return devices.values();
    }

    protected ActionClass getActionClass(String actionId) {
        validateActionManager();
        return actionManager.getActionClass(PropertyContainerClassContext.create(getContext(), actionId));
    }

    protected Collection<ActionClass> getActionClasses() {
        validateActionManager();
        return actionManager.getActionClasses(getContext());
    }

    @Override
    public Object getDeviceConfigurationProperty(String deviceId, String name) {
        validateDeviceManager();
        return deviceManager.getDeviceConfigurationProperty(DeviceContext.create(getContext(), deviceId), name);
    }

    @Override
    public Long getDeviceLastCheckin(String deviceId) {
        Long result = null;
        HobsonDeviceProxy d = devices.get(deviceId);
        if (d != null) {
            result = d.getLastCheckin();
        }
        return result;
    }

    @Override
    public DeviceVariableState getDeviceVariableState(String deviceId, String name) {
        HobsonDeviceProxy proxy = devices.get(deviceId);
        if (proxy != null) {
            return proxy.getVariableState(name);
        } else {
            return null;
        }
    }

    @Override
    public boolean hasDeviceVariableState(String deviceId, String name) {
        HobsonDeviceProxy proxy = devices.get(deviceId);
        if (proxy != null) {
            return proxy.hasVariable(name);
        } else {
            return false;
        }
    }

    @Override
    public boolean hasTaskProvider() {
        return (taskProvider != null);
    }

    @Override
    public long getRefreshInterval() {
        return 0;
    }

    @Override
    public TaskProvider getTaskProvider() {
        return taskProvider;
    }


    /*
     * HobsonPluginRuntime methods
     */

    @Override
    public EventLoopExecutor getEventLoopExecutor() {
        return this;
    }

    @Override
    public void postEvent(HobsonEvent event) {
        validateEventManager();
        eventManager.postEvent(HubContext.createLocal(), event);
    }

    @Override
    public void onDeviceVariablesUpdate(Collection<DeviceVariableDescriptor> vars) {
        validateDeviceManager();
        deviceManager.updateDeviceVariables(vars);
    }

    @Override
    public void onRefresh() {
    }

    @EventHandler
    public void onSetDeviceVariables(DeviceVariablesUpdateRequestEvent event) {
        if (event.getDeviceContext().getPluginContext().equals(getContext())) {
            getDeviceProxy(event.getDeviceContext().getDeviceId()).onSetVariables(event.getValues());
        }
    }

    @Override
    public void publishActionProvider(ActionProvider provider) {
        actionManager.publishActionProvider(provider);
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
    public void setDeviceConfigurationProperty(DeviceContext dctx, String name, Object value) {
        validateDeviceManager();
        deviceManager.setDeviceConfigurationProperty(dctx, name, value);
    }

    @Override
    public void setDeviceConfigurationProperties(DeviceContext dctx, Map<String,Object> values) {
        validateDeviceManager();
        deviceManager.setDeviceConfiguration(dctx, values);
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
    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
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

    protected boolean hasDeviceVariable(String deviceId, String name) {
        HobsonDeviceProxy device = getDeviceProxy(deviceId);
        if (device != null) {
            return device.hasVariable(name);
        }
        return false;
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

    protected void deleteDeviceProxy(String deviceId) {
        validateDeviceManager();
        if (devices.containsKey(deviceId)) {
            logger.trace("Deleting device: {}", deviceId);
            deviceManager.deleteDevice(DeviceContext.create(getContext(), deviceId));
        }
    }

    protected void addJobStatusMessage(String msg, Object props) {
        validateActionManager();
        actionManager.addJobStatusMessage(getContext(), msg, props);
    }

    /**
     * Requests all currently known device advertisements.
     *
     * @param protocolId the protocol ID for the advertisements requested
     *
     * @return a Collection of DeviceAdvertisement instances
     */
    protected Collection<DeviceAdvertisement> getDeviceAdvertisementSnapshot(final String protocolId) {
        validateDiscoManager();
        return discoManager.getExternalDeviceAdvertisements(getContext(), protocolId);
    }

    /**
     * Publishes a new DeviceAdvertisement.
     *
     * @param advertisement the advertisement to publish
     * @param internal indicates if this is an internal advertisement. A value of true will insure that the
     *                 advertisement is included in any discovery requests that external clients make for a
     *                 particular protocol.
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

    private void validateActionManager() {
        if (actionManager == null) {
            throw new HobsonRuntimeException("No action manager has been set");
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
