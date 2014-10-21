/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.action.manager.ActionManager;
import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.config.manager.PluginConfigurationListener;
import com.whizzosoftware.hobson.api.device.manager.DeviceManager;
import com.whizzosoftware.hobson.api.disco.manager.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventTopics;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.event.PluginStartedEvent;
import com.whizzosoftware.hobson.api.event.PluginStoppedEvent;
import com.whizzosoftware.hobson.api.event.manager.EventManager;
import com.whizzosoftware.hobson.api.event.manager.EventManagerListener;
import com.whizzosoftware.hobson.api.trigger.TriggerProvider;
import com.whizzosoftware.hobson.api.trigger.manager.TriggerManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.bootstrap.api.config.ConfigurationMetaData;
import com.whizzosoftware.hobson.bootstrap.api.plugin.PluginStatus;

import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

/**
 * A class that encapsulates a Hobson plugin class in order to handle OSGi lifecycle events and provide
 * the plugin event loop. This implements HobsonPlugin so it can will appear to the OSGi runtime as
 * an actual plugin while it intercepts the OSGi lifecycle callbacks.
 *
 * @author Dan Noguerol
 */
public class HobsonPluginEventLoopWrapper implements HobsonPlugin, PluginEventLoopListener, PluginConfigurationListener, EventManagerListener {
    // these will be dependency injected by the OSGi runtime
    private volatile DeviceManager deviceManager;
    private volatile VariableManager variableManager;
    private volatile EventManager eventManager;
    private volatile ConfigurationManager configManager;
    private volatile DiscoManager discoManager;
    private volatile TriggerManager triggerManager;
    private volatile ActionManager actionManager;

    private HobsonPlugin plugin;
    private PluginEventLoop eventLoop;

    /**
     * Constructor.
     *
     * @param plugin the plugin to wrapper
     */
    public HobsonPluginEventLoopWrapper(HobsonPlugin plugin) {
        if (plugin != null) {
            this.plugin = plugin;
            this.eventLoop = new PluginEventLoop(this, getRefreshInterval());
        } else {
            throw new HobsonRuntimeException("Passed a null plugin to HobsonPluginEventLoopWrapper");
        }
    }

    /**
     * Called when the OSGi service is started. This performs plugin dependency injection and gets the
     * plugin event loop started.
     */
    public void start() {
        // register plugin for configuration updates
        configManager.registerForPluginConfigurationUpdates(getId(), this);

        // register plugin for variable events
        eventManager.addListener(this, EventTopics.VARIABLES_TOPIC);

        // inject manager dependencies
        setDeviceManager(deviceManager);
        setVariableManager(variableManager);
        setConfigurationManager(configManager);
        setDiscoManager(discoManager);
        setTriggerManager(triggerManager);
        setActionManager(actionManager);

        // start the event loop
        eventLoop.start();

        // post plugin started event
        eventManager.postEvent(new PluginStartedEvent(getId()));
    }

    /**
     * Called when the OSGi service is stopped. This will stop the plugin event loop.
     */
    public void stop() {
        // stop listening for configuration changes
        configManager.unregisterForConfigurationUpdates(getId(), this);

        // stop listening for variable events
        eventManager.removeListener(this);

        // unpublish all variables published by this plugin's devices
        variableManager.unpublishAllPluginVariables(getId());

        // stop all devices
        deviceManager.stopAndUnpublishAllDevices(getId());

        // alert the event loop
        eventLoop.cancel();
        eventLoop = null;

        // post plugin stopped event
        eventManager.postEvent(new PluginStoppedEvent(getId()));
    }

    /*
     * EventLoopListener methods
     */

    @Override
    public void onEventLoopInitializing(Dictionary config) {
        init(config);
    }

    @Override
    public void onEventLoopRefresh() {
        onRefresh();
    }

    @Override
    public void onEventLoopStop() {
        plugin.stop();

        // stop referencing the child plugin
        plugin = null;
    }

    @Override
    public void onEventLoopPluginConfigChange(Dictionary config) {
        plugin.onPluginConfigurationUpdate(config);
    }

    @Override
    public void onEventLoopDeviceConfigChange(String deviceId, Dictionary config) {
        plugin.onDeviceConfigurationUpdate(deviceId, config);
    }

    @Override
    public void onEventLoopEvent(HobsonEvent event) {
        plugin.onHobsonEvent(event);
    }

    /*
     * PluginConfigurationListener methods
     */

    @Override
    public void onPluginConfigurationUpdate(Dictionary config) {
        eventLoop.onPluginConfigurationUpdate(config);
    }

    /*
     * DeviceConfigurationListener methods
     */

    @Override
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config) {
        eventLoop.onDeviceConfigurationUpdate(deviceId, config);
    }

    /*
     * EventManagerListener methods
     */

    @Override
    public void onHobsonEvent(HobsonEvent event) {
        eventLoop.onHobsonEvent(event);
    }

    /*
     * Hobson plugin interface methods --  these all pass-through to the real plugin implementation
     */

    @Override
    public void init(Dictionary config) {
        plugin.init(config);
    }

    @Override
    public void onRefresh() {
        plugin.onRefresh();
    }

    @Override
    public void setDeviceManager(DeviceManager deviceManager) {
        plugin.setDeviceManager(deviceManager);
    }

    @Override
    public void setVariableManager(VariableManager deviceManager) {
        plugin.setVariableManager(deviceManager);
    }

    @Override
    public void setConfigurationManager(ConfigurationManager configManager) {
        plugin.setConfigurationManager(configManager);
    }

    @Override
    public void setDiscoManager(DiscoManager discoManager) {
        plugin.setDiscoManager(discoManager);
    }

    @Override
    public void setTriggerManager(TriggerManager triggerManager) {
        plugin.setTriggerManager(triggerManager);
    }

    @Override
    public void setActionManager(ActionManager actionManager) {
        plugin.setActionManager(actionManager);
    }

    @Override
    public void setDeviceConfigurationProperty(String id, String name, Object value, boolean overwrite) {
        plugin.setDeviceConfigurationProperty(id, name, value, overwrite);
    }

    @Override
    public void publishGlobalVariable(HobsonVariable variable) {
        plugin.publishGlobalVariable(variable);
    }

    @Override
    public void publishDeviceVariable(String deviceId, HobsonVariable variable) {
        plugin.publishDeviceVariable(deviceId, variable);
    }

    @Override
    public void publishTriggerProvider(TriggerProvider triggerProvider) {
        plugin.publishTriggerProvider(triggerProvider);
    }

    @Override
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        plugin.fireVariableUpdateNotifications(updates);
    }

    @Override
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate) {
        plugin.fireVariableUpdateNotification(variableUpdate);
    }

    @Override
    public HobsonVariable getDeviceVariable(String deviceId, String variableName) {
        return plugin.getDeviceVariable(deviceId, variableName);
    }

    @Override
    public void onSetDeviceVariable(String deviceId, String variableName, Object value) {
        plugin.onSetDeviceVariable(deviceId, variableName, value);
    }

    @Override
    public long getRefreshInterval() {
        return plugin.getRefreshInterval();
    }

    @Override
    public String getId() {
        return plugin.getId();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public String getVersion() {
        return plugin.getVersion();
    }

    @Override
    public PluginStatus getStatus() {
        return plugin.getStatus();
    }

    @Override
    public Collection<ConfigurationMetaData> getConfigurationMetaData() {
        return plugin.getConfigurationMetaData();
    }

    @Override
    public boolean isConfigurable() {
        return plugin.isConfigurable();
    }
}
