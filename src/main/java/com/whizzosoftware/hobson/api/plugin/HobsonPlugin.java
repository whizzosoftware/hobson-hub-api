/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.device.HobsonDeviceDescriptor;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.job.AsyncJobHandle;
import com.whizzosoftware.hobson.api.job.JobManager;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;

import java.util.concurrent.TimeUnit;

/**
 * Interface for all Hobson Hub plugins.
 *
 * @author Dan Noguerol
 */
public interface HobsonPlugin {
    AsyncJobHandle executeDeviceAction(String deviceId, String actionId, PropertyContainer properties);
    AsyncJobHandle executePluginAction(PropertyContainer properties);
    HobsonLocalPluginDescriptor getDescriptor();
    PluginContext getContext();
    Object getDeviceConfigurationProperty(String deviceId, String name);
    Long getDeviceLastCheckin(String deviceId);
    DeviceVariableState getDeviceVariableState(String deviceId, String name);
    EventLoopExecutor getEventLoopExecutor();
    String[] getEventTopics();
    long getRefreshInterval();
    TaskProvider getTaskProvider();
    boolean hasTaskProvider();
    void onDeviceConfigurationUpdate(String deviceId, PropertyContainer config);
    void onDeviceUpdate(HobsonDeviceProxy device);
    void onHobsonEvent(HobsonEvent event);
    void onPluginConfigurationUpdate(PropertyContainer config);
    void onRefresh();
    void onSetDeviceVariable(String deviceId, String name, Object value);
    void onShutdown();
    void onStartup(PropertyContainer config);
    void postHubEvent(HobsonEvent event);
    void postPluginEvent(String name, Object o);
    void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit);
    void setDeviceConfigurationProperty(DeviceContext dctx, PropertyContainerClass configClass, String name, Object value);
    void setDeviceManager(DeviceManager deviceManager);
    void setDiscoManager(DiscoManager discoManager);
    void setEventManager(EventManager eventManager);
    void setHubManager(HubManager hubManager);
    void setJobManager(JobManager jobManager);
    void setPluginManager(PluginManager pluginManager);
    void setTaskManager(TaskManager taskManager);
}
