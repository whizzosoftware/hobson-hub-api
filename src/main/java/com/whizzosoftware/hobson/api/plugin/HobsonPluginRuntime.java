/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.device.proxy.DeviceProxyVariable;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventListener;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import io.netty.util.concurrent.Future;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Interface for methods related to Hobson plugin runtime functions including lifecycle callbacks.
 *
 * @author Dan Noguerol
 */
public interface HobsonPluginRuntime extends EventListener {
    PropertyContainerClass getDeviceConfigurationClass(String deviceId);

    /**
     * Retrieves a specific device variable.
     *
     * @param deviceId the device ID of the variable
     * @param name the name of the variable
     *
     * @return a DeviceProxyVariable instance
     * @throws com.whizzosoftware.hobson.api.variable.VariableNotFoundException if the variable wasn't found
     */
    DeviceProxyVariable getDeviceVariableValue(String deviceId, String name);

    /**
     * Returns all variables for a device.
     *
     * @param deviceId the device ID
     *
     * @return a Collection of DeviceProxyVariable instances
     */
    Collection<DeviceProxyVariable> getDeviceVariableValues(String deviceId);

    /**
     * Returns the event loop executor for this plugin.
     *
     * @return an EventLoopExecutor instance
     */
    EventLoopExecutor getEventLoopExecutor();

    /**
     * Returns the topics this plugin is interested in receiving events for.
     *
     * @return an array of String topic names (or null if no events are desired)
     */
    String[] getEventTopics();

    /**
     * Returns how often the refresh() method will be called.
     *
     * @return the refresh interval in seconds (a 0 value means never)
     */
    long getRefreshInterval();

    /**
     * Returns the task provider associated with this plugin.
     *
     * @return a TaskProvider instance (or null if one is not available)
     */
    TaskProvider getTaskProvider();

    /**
     * Indicates if a device has published a particular variable.
     *
     * @param ctx the context of the device that published the variable
     * @param variableName the variable name
     *
     * @return a boolean
     */
    boolean hasDeviceVariableValue(String deviceId, String variableName);

    /**
     * Called when the plugin device's configuration has changed.
     *  @param ctx the context of the device that owns the configuration
     * @param config the new configuration
     */
    void onDeviceConfigurationUpdate(String deviceId, PropertyContainer config);

    /**
     * Callback method when a device hint is received. This is an indicator to the plugin
     * that it should attempt to location a device with the provided configuration. This
     * is primarily used for devices that the plugin is unable to discover itself.
     *
     * @param name the device name
     * @param deviceType the type of device
     * @param config the configuration for the device
     */
    void onDeviceHint(String name, DeviceType deviceType, PropertyContainer config);

    /**
     * Called when an action that a plugin has published needs to be executed.
     *
     * @param action the action to execute
     */
    void onExecuteAction(PropertyContainer action);

    /**
     * Called when the plugin's configuration has changed.
     *
     * @param config the new configuration
     */
    void onPluginConfigurationUpdate(PropertyContainer config);

    /**
     * Callback that gives a plugin the opportunity to perform work. This will be called every
     * refresh interval.
     */
    void onRefresh();

    /**
     * Callback when a request to set a device variable has been received.
     *
     * @param ctx the context of the device that owns the variable
     * @param variableName the variable name
     * @param value the variable value
     */
    void onSetDeviceVariable(String deviceId, String variableName, Object value);

    /**
     * Callback method invoked when the plugin starts up.
     *
     * @param config the plugin configuration
     */
    void onStartup(PropertyContainer config);

    /**
     * Callback method invoked when the plugin shuts down.
     */
    void onShutdown();

    /**
     * Fires a HobsonEvent.
     *
     * @param event the event to fire
     */
    void postEvent(HobsonEvent event);

    /**
     * Publish an action class.
     *
     * @param actionClass the action class to publish
     */
    void publishActionClass(TaskActionClass actionClass);

    /**
     * Publish a condition class.
     *
     * @param conditionClass the condition class to publish
     */
    void publishConditionClass(TaskConditionClass conditionClass);

    void publishDeviceType(DeviceType type, TypedProperty[] configProperties);

    /**
     * Execute a recurring task.
     *
     * @param runnable a task to execute
     * @param initialDelay how long to wait for the first execution
     * @param time the wait interval between executions
     * @param unit the temporal unit for the time argument
     */
    void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit);

    /**
     * Indicates that Hobson has just contacted a device.
     *
     * @param deviceId the device ID
     * @param available whether the device is currently available
     * @param checkInTime the time the device was contacted (or null to leave the last check-in time unchanged)
     */
    void setDeviceAvailability(String deviceId, boolean available, Long checkInTime);

    /**
     * Sets a configuration property for a specific device.
     *
     * @param ctx the context of the device that owns the configuration
     * @param name the name of the variable
     * @param value the value of the variable
     * @param overwrite whether to overwrite the property if it already exists
     */
    void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite);

    /**
     * Sets the DeviceManager instance the plugin should use. This will be called before the init() method.
     *
     * @param deviceManager a DeviceManager
     */
    void setDeviceManager(DeviceManager deviceManager);

    Future setDeviceVariable(DeviceVariableContext dvctx, Object value);

    Future setDeviceVariables(Map<DeviceVariableContext,Object> values);

    /**
     * Sets the DiscoManager instance the plugin should use. This will be called before the init() method.
     *
     * @param discoManager a DiscoManager
     */
    void setDiscoManager(DiscoManager discoManager);

    /**
     * Sets the EventManager instance the plugin should use. This will be called before the init() method.
     *
     * @param eventManager a EventManager
     */
    void setEventManager(EventManager eventManager);

    void setGlobalVariable(String name, Object value, long timestamp);

    void setGlobalVariables(Map<String,Object> values, long timestamp);

    /**
     * Sets the HubManager instance the plugin should use. This will be called before the init() method.
     *
     * @param hubManager a HubManager
     */
    void setHubManager(HubManager hubManager);

    /**
     * Sets the PluginManager instance the plugin should use. This will be called before the init() method.
     *
     * @param pluginManager a PluginManager
     */
    void setPluginManager(PluginManager pluginManager);

    /**
     * Sets the TaskManager instance the plugin should use. This will be called before the init() method.
     *
     * @param taskManager a TaskManager
     */
    void setTaskManager(TaskManager taskManager);

    /**
     * Execute a task using the plugin event loop. Note that this will tie up the event loop while the task is being
     * executed so this should not be used for long running tasks.
     *
     * @param runnable a task to execute
     *
     * @return a Future for monitoring the task execution status
     */
    Future submitInEventLoop(Runnable runnable);
}
