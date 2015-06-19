/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.disco.DeviceAdvertisement;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.*;
import com.whizzosoftware.hobson.api.hub.HobsonHub;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.task.TaskActionClass;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.telemetry.TelemetryManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.VariableManager;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;

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
    private DeviceManager deviceManager;
    private DiscoManager discoManager;
    private EventManager eventManager;
    private HubManager hubManager;
    private PluginManager pluginManager;
    private VariableManager variableManager;
    private TaskManager taskManager;
    private TelemetryManager telemetryManager;
    private PluginContext ctx;
    private String version;
    private PluginStatus status = PluginStatus.initializing();
    private final PropertyContainerClass configClass = new PropertyContainerClass();
    private EventLoopGroup eventLoop;
    private final Map<PropertyContainerClassContext,TaskActionClass> actionClasses = new HashMap<>();

    public AbstractHobsonPlugin(String pluginId) {
        this(pluginId, new LocalEventLoopGroup(1));
    }

    public AbstractHobsonPlugin(String pluginId, EventLoopGroup eventLoop) {
        this.ctx = PluginContext.createLocal(pluginId);
        this.eventLoop = eventLoop;

        // register any supported properties the subclass needs
        TypedProperty[] props = createSupportedProperties();
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

    @Override
    public EventLoopExecutor getEventLoopExecutor() {
        return this;
    }

    @Override
    public void fireHobsonEvent(HobsonEvent event) {
        validateEventManager();
        eventManager.postEvent(HubContext.createLocal(), event);
    }

    @Override
    public void fireVariableUpdateNotification(VariableUpdate update) {
        // post the variable update notification event
        validateVariableManager();
        variableManager.applyVariableUpdates(HubContext.createLocal(), Collections.singletonList(update));
    }

    @Override
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        // post the variable update notification event
        validateVariableManager();
        variableManager.applyVariableUpdates(HubContext.createLocal(), updates);
    }

    @Override
    public HobsonVariable getDeviceVariable(DeviceContext ctx, String variableName) {
        validateVariableManager();
        return variableManager.getDeviceVariable(ctx, variableName);
    }

    @Override
    public void onDeviceConfigurationUpdate(DeviceContext ctx, PropertyContainer config) {
        getDevice(ctx).getRuntime().onDeviceConfigurationUpdate(config);
    }

    @Override
    public boolean onEvaluateCondition(PropertyContainer condition) {
        // should be overriden by sub-classes
        return false;
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
    public void onSetDeviceVariable(DeviceContext ctx, String variableName, Object value) {
        getDevice(ctx).getRuntime().onSetVariable(variableName, value);
    }

    @Override
    public void onShutdown() {
        // unpublish all tasks published by this plugin
        if (taskManager != null) {
            taskManager.unpublishAllTasks(getContext());
        }
    }

    @Override
    public void publishActionClass(TaskActionClass actionClass) {
        validateTaskManager();
        taskManager.publishActionClass(actionClass.getContext(), actionClass.getName(), actionClass.getProperties());
        actionClasses.put(actionClass.getContext(), actionClass);
    }

    @Override
    public void publishConditionClass(PropertyContainerClassContext ctx, String name, List<TypedProperty> properties) {
        validateTaskManager();
        taskManager.publishConditionClass(ctx, name, properties);
    }

    @Override
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask) {
        validateVariableManager();
        variableManager.publishDeviceVariable(ctx, name, value, mask);
    }

    @Override
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask, String proxyType) {
        validateVariableManager();
        variableManager.publishDeviceVariable(ctx, name, value, mask, proxyType);
    }

    @Override
    public void publishGlobalVariable(String name, Object value, HobsonVariable.Mask mask) {
        validateVariableManager();
        variableManager.publishGlobalVariable(getContext(), name, value, mask);
    }

    @Override
    public void publishGlobalVariable(String name, Object value, HobsonVariable.Mask mask, String proxyType) {
        validateVariableManager();
        variableManager.publishGlobalVariable(getContext(), name, value, mask, proxyType);
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
    public void setTelemetryManager(TelemetryManager telemetryManager) {
        this.telemetryManager = telemetryManager;
    }

    @Override
    public void setVariableManager(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public Future submitInEventLoop(Runnable runnable) {
        return eventLoop.submit(runnable);
    }

    /*
     * EventLoopExecutor methods
     */

    @Override
    public void executeInEventLoop(Runnable runnable) {
        eventLoop.execute(runnable);
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
    abstract protected TypedProperty[] createSupportedProperties();

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

    /**
     * Publishes a new device and invokes its start() method.
     *
     * @param device the device to publish
     */
    protected void publishDevice(final HobsonDevice device) {
        publishDevice(device, false);
    }

    /**
     * Publishes a new device and invokes its start() method.
     *
     * @param device the device to publish
     * @param republish indicates whether this is a re-publish of a previously published device (if this is false, a re-publish will throw an exception)
     */
    protected void publishDevice(final HobsonDevice device, boolean republish) {
        validateDeviceManager();
        deviceManager.publishDevice(device, republish);
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
            discoManager.requestDeviceAdvertisementSnapshot(getContext(), protocolId);
            }
        });
    }

    /**
     * Publishes a new DeviceAdvertisement.
     *
     * @param advertisement the advertisement to publish
     */
    protected void fireDeviceAdvertisement(DeviceAdvertisement advertisement) {
        validateDiscoManager();
        discoManager.fireDeviceAdvertisement(HubContext.createLocal(), advertisement);
    }

    /**
     * Indicates whether a specific device ID has already been published.
     *
     * @param ctx the context of the device
     *
     * @return a boolean
     */
    protected boolean hasDevice(DeviceContext ctx) {
        return deviceManager.hasDevice(ctx);
    }

    /**
     * Retrieves all HobsonDevices that have been published.
     *
     * @return a Collection of HobsonDevice instances
     */
    protected Collection<HobsonDevice> getAllDevices() {
        return deviceManager.getAllDevices(HubContext.createLocal());
    }

    /**
     * Retrieves all HobsonDevices that have been published by this plugin.
     *
     * @return a Collection of HobsonDevice instances
     */
    protected Collection<HobsonDevice> getAllPluginDevices() {
        return deviceManager.getAllDevices(getContext());
    }

    /**
     * Retrieves a HobsonDevice with the specific device ID.
     *
     * @param ctx the device context
     *
     * @return a HobsonDevice instance
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if the device ID was not found
     */
    protected HobsonDevice getDevice(DeviceContext ctx) {
        validateDeviceManager();
        return deviceManager.getDevice(ctx);
    }

    /**
     * Unpublishes a published device after invoking its stop() method.
     *
     * @param deviceId the device ID to unpublish
     */
    protected void unpublishDevice(final String deviceId) {
        validateVariableManager();
        validateDeviceManager();
        DeviceContext dctx = DeviceContext.create(getContext(), deviceId);
        variableManager.unpublishAllDeviceVariables(dctx);
        deviceManager.unpublishDevice(dctx, this);
    }

    /**
     * Unpublishes all published devices associates with a plugin after invoking their stop() methods.
     */
    protected void unpublishAllDevices() {
        validateVariableManager();
        validateDeviceManager();
        variableManager.unpublishAllPluginVariables(getContext());
        deviceManager.unpublishAllDevices(getContext(), this);
    }

    protected Collection<HobsonVariable> getAllVariables() {
        return variableManager.getAllVariables(HubContext.createLocal());
    }

    protected DeviceManager getDeviceManager() {
        return deviceManager;
    }

    protected HubManager getHubManager() {
        return hubManager;
    }

    protected VariableManager getVariableManager() {
        return variableManager;
    }

    protected TelemetryManager getTelemetryManager() {
        return telemetryManager;
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

    private void validateVariableManager() {
        if (variableManager == null) {
            throw new HobsonRuntimeException("No variable manager has been set");
        }
    }
}
