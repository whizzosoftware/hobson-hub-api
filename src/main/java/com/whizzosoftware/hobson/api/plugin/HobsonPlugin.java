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

import com.whizzosoftware.hobson.api.action.ActionProvider;
import com.whizzosoftware.hobson.api.action.ActionManager;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.variable.DeviceVariableDescriptor;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Interface for all Hobson Hub plugins.
 *
 * @author Dan Noguerol
 */
public interface HobsonPlugin {
    HobsonLocalPluginDescriptor getDescriptor();
    PropertyContainerClass getConfigurationClass();
    PluginContext getContext();
    Object getDeviceConfigurationProperty(String deviceId, String name);
    Long getDeviceLastCheckin(String deviceId);

    /**
     * Returns the device variable state for a device published by this plugin.
     *
     * @param deviceId the device ID
     * @param name the variable name
     *
     * @return a DeviceVariableState
     */
    DeviceVariableState getDeviceVariableState(String deviceId, String name);

    EventLoopExecutor getEventLoopExecutor();
    long getRefreshInterval();
    PluginStatus getStatus();
    TaskProvider getTaskProvider();

    /**
     * Returns whether this device has published a variable.
     *
     * @param deviceId the device ID
     * @param name the variable name
     *
     * @return a boolean
     */
    boolean hasDeviceVariableState(String deviceId, String name);

    boolean hasTaskProvider();
    void onDeviceVariablesUpdate(Collection<DeviceVariableDescriptor> vars);
    void onRefresh();

    /**
     * Callback method for plugins to perform tasks when they are being stopped.
     */
    void onShutdown();

    /**
     * Callback method for plugins to perform initialization once they've been started.
     *
     * @param config the current plugin configuration
     */
    void onStartup(PropertyContainer config);
    void postEvent(HobsonEvent event);
    void publishActionProvider(ActionProvider actionProvider);
    void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit);
    void setActionManager(ActionManager actionManager);
    void setDeviceConfigurationProperty(DeviceContext dctx, String name, Object value);
    void setDeviceConfigurationProperties(DeviceContext dctx, Map<String,Object> values);
    void setDeviceManager(DeviceManager deviceManager);
    void setDiscoManager(DiscoManager discoManager);
    void setEventManager(EventManager eventManager);
    void setHubManager(HubManager hubManager);
    void setPluginManager(PluginManager pluginManager);
    void setTaskManager(TaskManager taskManager);
}
