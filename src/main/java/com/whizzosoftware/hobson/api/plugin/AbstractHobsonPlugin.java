/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.HobsonAction;
import com.whizzosoftware.hobson.api.action.ActionManager;
import com.whizzosoftware.hobson.api.config.ConfigurationPropertyMetaData;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.disco.DeviceAdvertisement;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.*;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.util.UserUtil;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.VariableManager;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A base class that implements several HobsonPlugin methods. This provides a good starting point for third-party
 * HobsonPlugin implementations.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHobsonPlugin implements HobsonPlugin, HobsonPluginRuntime {
    private ActionManager actionManager;
    private DeviceManager deviceManager;
    private DiscoManager discoManager;
    private EventManager eventManager;
    private HubManager hubManager;
    private PluginManager pluginManager;
    private VariableManager variableManager;
    private TaskManager taskManager;
    private String pluginId;
    private String version;
    private PluginStatus status = new PluginStatus(PluginStatus.Status.INITIALIZING);
    private final List<ConfigurationPropertyMetaData> configMetaData = new ArrayList<>();
    private EventLoopGroup eventLoop;

    public AbstractHobsonPlugin(String pluginId) {
        this(pluginId, new LocalEventLoopGroup(1));
    }

    public AbstractHobsonPlugin(String pluginId, EventLoopGroup eventLoop) {
        this.pluginId = pluginId;
        this.eventLoop = eventLoop;
    }

    @Override
    public String getId() {
        return pluginId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public PluginStatus getStatus() {
        return status;
    }

    @Override
    public String[] getEventTopics() {
        return null;
    }

    @Override
    public Collection<ConfigurationPropertyMetaData> getConfigurationPropertyMetaData() {
        return configMetaData;
    }

    @Override
    public PluginType getType() {
        return PluginType.PLUGIN;
    }

    @Override
    public boolean isConfigurable() {
        return (configMetaData.size() > 0);
    }

    @Override
    public long getRefreshInterval() {
        return 0;
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public HobsonPluginRuntime getRuntime() {
        return this;
    }

    @Override
    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
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
    public void setVariableManager(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public void executeInEventLoop(Runnable runnable) {
        eventLoop.execute(runnable);
    }

    @Override
    public Future submitInEventLoop(Runnable runnable) {
        return eventLoop.submit(runnable);
    }

    @Override
    public void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit) {
        eventLoop.scheduleAtFixedRate(runnable, initialDelay, time, unit);
    }

    @Override
    public void setDeviceConfigurationProperty(String id, String name, Object value, boolean overwrite) {
        validateDeviceManager();
        deviceManager.setDeviceConfigurationProperty(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), id, name, value, overwrite);
    }

    @Override
    public HobsonVariable getDeviceVariable(String deviceId, String variableName) {
        validateVariableManager();
        return variableManager.getDeviceVariable(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), deviceId, variableName);
    }

    @Override
    public void publishGlobalVariable(String name, Object value, HobsonVariable.Mask mask) {
        validateVariableManager();
        variableManager.getPublisher().publishGlobalVariable(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), name, value, mask);
    }

    @Override
    public void publishDeviceVariable(String deviceId, String name, Object value, HobsonVariable.Mask mask) {
        validateVariableManager();
        variableManager.getPublisher().publishDeviceVariable(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), deviceId, name, value, mask);
    }

    @Override
    public void publishTaskProvider(TaskProvider taskProvider) {
        validateTaskManager();
        taskProvider.setActionManager(actionManager);
        taskManager.publishTaskProvider(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, taskProvider);
    }

    @Override
    public void publishAction(HobsonAction action) {
        validateActionManager();
        actionManager.publishAction(action);
    }

    @Override
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        validateVariableManager();
        variableManager.getPublisher().fireVariableUpdateNotifications(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, this, updates);
    }

    @Override
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate) {
        validateVariableManager();
        variableManager.getPublisher().fireVariableUpdateNotification(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, this, variableUpdate);
    }

    @Override
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config) {
        getDevice(deviceId).getRuntime().onDeviceConfigurationUpdate(config);
    }

    @Override
    public void onHobsonEvent(HobsonEvent event) {
        if (event instanceof VariableUpdateRequestEvent) {
            VariableUpdateRequestEvent vure = (VariableUpdateRequestEvent)event;
            if (vure.getPluginId() == null || vure.getPluginId().equals(getId())) {
                onSetDeviceVariable(vure.getDeviceId(), vure.getName(), vure.getValue());
            }
        }
    }

    @Override
    public void onSetDeviceVariable(String deviceId, String variableName, Object value) {
        getDevice(deviceId).getRuntime().onSetVariable(variableName, value);
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
     * Add configuration property metadata for this plugin.
     *
     * @param metaData the metadata to add
     */
    protected void addConfigurationPropertyMetaData(ConfigurationPropertyMetaData metaData) {
        configMetaData.add(metaData);
    }

    /**
     * Returns a File located in the plugin's directory sandbox.
     *
     * @param filename the filename
     *
     * @return a File instance
     */
    protected File getDataFile(String filename) {
        return pluginManager.getDataFile(getId(), filename);
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
     * @param pluginId the plugin ID
     * @param name the name of the property
     * @param value the value of the property
     */
    protected void setPluginConfigurationProperty(String pluginId, String name, Object value) {
        validatePluginManager();
        pluginManager.setPluginConfigurationProperty(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, pluginId, name, value);
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
        deviceManager.getPublisher().publishDevice(this, device, republish);
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
            discoManager.requestDeviceAdvertisementSnapshot(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), protocolId);
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
        discoManager.fireDeviceAdvertisement(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, advertisement);
    }

    /**
     * Indicates whether a specific device ID has already been published.
     *
     * @param deviceId the device ID of interest
     *
     * @return a boolean
     */
    protected boolean hasDevice(String deviceId) {
        return deviceManager.hasDevice(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), deviceId);
    }

    /**
     * Retrieves a HobsonDevice with the specific device ID.
     *
     * @param deviceId the device ID
     *
     * @return a HobsonDevice instance
     * @throws com.whizzosoftware.hobson.api.HobsonNotFoundException if the device ID was not found
     */
    protected HobsonDevice getDevice(String deviceId) {
        validateDeviceManager();
        return deviceManager.getDevice(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), deviceId);
    }

    /**
     * Retrieves all HobsonDevices that have been published by this plugin.
     *
     * @return a Collection of HobsonDevice instances
     */
    protected Collection<HobsonDevice> getAllDevices() {
        return deviceManager.getAllPluginDevices(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId());
    }

    /**
     * Unpublishes a published device after invoking its stop() method.
     *
     * @param deviceId the device ID to unpublish
     */
    protected void unpublishDevice(final String deviceId) {
        validateVariableManager();
        validateDeviceManager();
        variableManager.getPublisher().unpublishAllDeviceVariables(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId(), deviceId);
        deviceManager.getPublisher().unpublishDevice(this, deviceId);
    }

    /**
     * Unpublishes all published devices associates with a plugin after invoking their stop() methods.
     */
    protected void unpublishAllDevices() {
        validateVariableManager();
        validateDeviceManager();
        variableManager.getPublisher().unpublishAllPluginVariables(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, getId());
        deviceManager.getPublisher().unpublishAllDevices(this);
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

    private void validateActionManager() {
        if (actionManager == null) {
            throw new HobsonRuntimeException("No action manager has been set");
        }
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
