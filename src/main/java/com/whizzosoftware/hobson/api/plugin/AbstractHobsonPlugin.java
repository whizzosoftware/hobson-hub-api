/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.device.proxy.DeviceProxy;
import com.whizzosoftware.hobson.api.device.proxy.DeviceProxyVariable;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.disco.DeviceAdvertisement;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.*;
import com.whizzosoftware.hobson.api.hub.HobsonHub;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.variable.*;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
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
abstract public class AbstractHobsonPlugin implements HobsonPlugin, HobsonPluginRuntime, EventLoopExecutor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHobsonPlugin.class);

    private DeviceManager deviceManager;
    private DiscoManager discoManager;
    private EventManager eventManager;
    private HubManager hubManager;
    private PluginManager pluginManager;
    private TaskManager taskManager;
    private PluginContext ctx;
    private String version;
    private PluginStatus status = PluginStatus.initializing();
    private final PropertyContainerClass configClass;
    private EventLoopGroup eventLoop;
    private final Map<PropertyContainerClassContext,TaskActionClass> actionClasses = new HashMap<>();
    private final Map<String,DeviceProxy> deviceProxyMap = Collections.synchronizedMap(new HashMap<String,DeviceProxy>());

    public AbstractHobsonPlugin(String pluginId) {
        this(pluginId, new LocalEventLoopGroup(1));
    }

    AbstractHobsonPlugin(String pluginId, EventLoopGroup eventLoop) {
        this.ctx = PluginContext.createLocal(pluginId);
        this.eventLoop = eventLoop;
        this.configClass = new PropertyContainerClass(PropertyContainerClassContext.create(ctx, "configurationClass"), PropertyContainerClassType.PLUGIN_CONFIG);

        // register any supported properties the subclass needs
        TypedProperty[] props = getConfigurationPropertyTypes();
        if (props != null) {
            for (TypedProperty p : props) {
                configClass.addSupportedProperty(p);
            }
        }
    }

    /*
     * HobsonPlugin methods
     */

    @Override
    public PluginContext getContext() {
        return ctx;
    }

    protected DeviceProxy getDeviceProxy(String deviceId) {
        DeviceProxy p = deviceProxyMap.get(deviceId);
        if (p == null) {
            throw new HobsonNotFoundException("Device not found");
        } else {
            return p;
        }
    }

    @Override
    public String[] getEventTopics() {
        return null;
    }

    @Override
    public PropertyContainerClass getConfigurationClass() {
        return configClass;
    }

    @Override
    public long getRefreshInterval() {
        return 0;
    }

    @Override
    public HobsonPluginRuntime getRuntime() {
        return this;
    }

    @Override
    public PluginStatus getStatus() {
        return status;
    }

    @Override
    public TaskProvider getTaskProvider() {
        return null;
    }

    @Override
    public PluginType getType() {
        return PluginType.PLUGIN;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isConfigurable() {
        return configClass.hasSupportedProperties();
    }

    /*
     * HobsonPluginRuntime methods
     */

    public void onDeviceHint(String name, DeviceType type, PropertyContainer config) {
        throw new HobsonRuntimeException("This plugin does not support adding devices");
    }

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
    public PropertyContainerClass getDeviceConfigurationClass(String deviceId) {
        DeviceProxy proxy = deviceProxyMap.get(deviceId);
        if (proxy != null) {
            return proxy.getConfigurationClass();
        } else {
            return null;
        }
    }

//    @Override
//    public DeviceProxyVariable getDeviceVariableValue(String deviceId, String name) {
//        try {
//            DeviceProxy proxy = getDeviceProxy(deviceId);
//            return proxy.getVariableValue(name);
//        } catch (HobsonNotFoundException e) {
//            return null;
//        }
//    }
    public DeviceVariable getDeviceVariable(String deviceId, String name) {
        DeviceProxy proxy = getDeviceProxy(deviceId);
        return proxy.getVariable(name);
    }

//    @Override
//    public Collection<DeviceProxyVariable> getDeviceVariableValues(String deviceId) {
//        try {
//            DeviceProxy proxy = getDeviceProxy(deviceId);
//            return proxy.getVariableValues();
//        } catch (HobsonNotFoundException e) {
//            return null;
//        }
//    }
    public Collection<DeviceVariable> getDeviceVariables(String deviceId) {
            DeviceProxy proxy = getDeviceProxy(deviceId);
            return proxy.getVariables();
    }

    @Override
    public boolean hasDeviceVariableValue(String deviceId, String variableName) {
        DeviceProxy proxy = getDeviceProxy(deviceId);
        return proxy.hasVariableValue(variableName);
    }

    @Override
    public void setDeviceAvailability(String deviceId, boolean available, Long checkInTime) {
        validateDeviceManager();
        deviceManager.setDeviceAvailability(DeviceContext.create(getContext(), deviceId), available, checkInTime);
    }

    @Override
    public void onDeviceConfigurationUpdate(String deviceId, PropertyContainer config) {
        getDeviceProxy(deviceId).onDeviceConfigurationUpdate(config);
    }

    @Override
    public void onExecuteAction(final PropertyContainer action) {
        final TaskActionClass tac = actionClasses.get(action.getContainerClassContext());
        if (tac != null) {
            submitInEventLoop(new Runnable() {
                @Override
                public void run() {
                    tac.getExecutor().executeAction(action);
                }
            });
        }
    }

    @Override
    public void onHobsonEvent(HobsonEvent event) {
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onSetDeviceVariable(String deviceId, String variableName, Object value) {
        getDeviceProxy(deviceId).onSetVariable(variableName, value);
    }

    @Override
    public void publishActionClass(TaskActionClass actionClass) {
        validateTaskManager();
        taskManager.publishActionClass(actionClass);
        actionClasses.put(actionClass.getContext(), actionClass);
    }

    @Override
    public void publishConditionClass(TaskConditionClass conditionClass) {
        validateTaskManager();
        taskManager.publishConditionClass(conditionClass);
    }

    @Override
    public void publishDeviceType(DeviceType type, TypedProperty[] configProperties) {
        validateDeviceManager();
        deviceManager.publishDeviceType(getContext(), type, configProperties);
    }

    @Override
    public void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit) {
        eventLoop.scheduleAtFixedRate(runnable, initialDelay, time, unit);
    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite) {
        validateDeviceManager();
        deviceManager.setDeviceConfigurationProperty(ctx, name, value, overwrite);
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
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public Future submitInEventLoop(final Runnable runnable) {
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
     * Returns information about the local Hub.
     *
     * @return a HobsonHub instance
     */
    public HobsonHub getHub() {
        return hubManager.getHub(HubContext.createLocal());
    }

    /**
     * Sets the plugin version string.
     *
     * @param version the version string
     */
    public void setVersion(String version) {
        this.version = version;
    }

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
     * Sets the plugin status.
     *
     * @param status the status
     */
    protected void setStatus(PluginStatus status) {
        this.status = status;
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

    protected Future registerDeviceProxy(DeviceProxy proxy) {
        return registerDeviceProxy(proxy, null);
    }

    protected Future registerDeviceProxy(final DeviceProxy proxy, final Map<String,Object> config) {
        validateDeviceManager();
        return deviceManager.publishDevice(getContext(), proxy, config).addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isSuccess()) {
                    deviceProxyMap.put(proxy.getDeviceId(), proxy);
                } else {
                    logger.error("Error registering device proxy: " + DeviceContext.create(getContext(), proxy.getDeviceId()), future.cause());
                }
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

    /**
     * Indicates whether a specific device ID has already been published.
     *
     * @param ctx the context of the device
     *
     * @return a boolean
     */
    protected boolean hasDevice(DeviceContext ctx) {
        validateDeviceManager();
        return deviceManager.hasDevice(ctx);
    }

    /**
     * Retrieves all HobsonDevices that have been published.
     *
     * @return a Collection of HobsonDevice instances
     */
    protected Collection<DeviceDescription> getAllDevices() {
        validateDeviceManager();
        return deviceManager.getAllDeviceDescriptions(HubContext.createLocal());
    }

    /**
     * Retrieves all HobsonDevices that have been published by this plugin.
     *
     * @return a Collection of HobsonDevice instances
     */
    protected Collection<DeviceDescription> getAllPluginDevices() {
        validateDeviceManager();
        return deviceManager.getAllDeviceDescriptions(getContext());
    }

    /**
     * Retrieves a HobsonDevice with the specific device ID.
     *
     * @param ctx the device context
     *
     * @return a HobsonDevice instance
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if the device ID was not found
     */
    protected DeviceDescription getDevice(DeviceContext ctx) {
        validateDeviceManager();
        return deviceManager.getDeviceDescription(ctx);
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

    @Override
    public Future setDeviceVariable(DeviceVariableContext dvctx, Object value) {
        return deviceManager.setDeviceVariable(dvctx, value);
    }

    @Override
    public Future setDeviceVariables(Map<DeviceVariableContext,Object> values) {
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
