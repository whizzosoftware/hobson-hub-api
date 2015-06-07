/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventListener;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.task.TaskActionClass;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.telemetry.TelemetryManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableManager;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import io.netty.util.concurrent.Future;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interface for methods related to Hobson plugin runtime functions including lifecycle callbacks.
 *
 * @author Dan Noguerol
 */
public interface HobsonPluginRuntime extends EventListener {
    /**
     * Fires a HobsonEvent.
     *
     * @param event the event to fire
     */
    public void fireHobsonEvent(HobsonEvent event);

    /**
     * Fires a notification that a variable has been successfully updated.
     *
     * @param variableUpdate a VariableUpdate instance
     */
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate);

    /**
     * Fires a notification that variables has been successfully updated.
     *
     * @param updates a List of VariableUpdate instances
     */
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates);

    /**
     * Retrieves a specific device variable.
     *
     * @param ctx the context of the device that published the variable
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     * @throws com.whizzosoftware.hobson.api.variable.DeviceVariableNotFoundException if the variable wasn't found
     */
    public HobsonVariable getDeviceVariable(DeviceContext ctx, String variableName);

    /**
     * Returns the event loop executor for this plugin.
     *
     * @return an EventLoopExecutor instance
     */
    public EventLoopExecutor getEventLoopExecutor();

    /**
     * Returns the topics this plugin is interested in receiving events for.
     *
     * @return an array of String topic names (or null if no events are desired)
     */
    public String[] getEventTopics();

    /**
     * Returns how often the refresh() method will be called.
     *
     * @return the refresh interval in seconds (a 0 value means never)
     */
    public long getRefreshInterval();

    /**
     * Returns the task provider associated with this plugin.
     *
     * @return a TaskProvider instance (or null if one is not available)
     */
    public TaskProvider getTaskProvider();

    /**
     * Called when the plugin device's configuration has changed.
     *  @param ctx the context of the device that owns the configuration
     * @param config the new configuration
     */
    public void onDeviceConfigurationUpdate(DeviceContext ctx, PropertyContainer config);

    /**
     * Called when a condition that a plugin published needs to be evaluated.
     *
     * @param condition the condition to evaluate
     *
     * @return the boolean results of the condition
     */
    public boolean onEvaluateCondition(PropertyContainer condition);

    /**
     * Called when an action that a plugin has published needs to be executed.
     *
     * @param action the action to execute
     */
    public void onExecuteAction(PropertyContainer action);

    /**
     * Called when the plugin's configuration has changed.
     *
     * @param config the new configuration
     */
    public void onPluginConfigurationUpdate(PropertyContainer config);

    /**
     * Callback that gives a plugin the opportunity to perform work. This will be called every
     * refresh interval.
     */
    public void onRefresh();

    /**
     * Callback when a request to set a device variable has been received.
     *
     * @param ctx the context of the device that owns the variable
     * @param variableName the variable name
     * @param value the variable value
     */
    public void onSetDeviceVariable(DeviceContext ctx, String variableName, Object value);

    /**
     * Callback method invoked when the plugin starts up.
     *
     * @param config the plugin configuration
     */
    public void onStartup(PropertyContainer config);

    /**
     * Callback method invoked when the plugin shuts down.
     */
    public void onShutdown();

    /**
     * Publish an action class.
     *
     * @param actionClass the action class to publish
     */
    public void publishActionClass(TaskActionClass actionClass);

    /**
     * Publish a condition class.
     *
     * @param ctx a
     * @param name b
     * @param properties c
     */
    public void publishConditionClass(PropertyContainerClassContext ctx, String name, List<TypedProperty> properties);

    /**
     * Publish a device variable.
     *
     * @param ctx the context of the device publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     */
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask);

    /**
     * Publish a device variable.
     *
     * @param ctx the context of the device publishing the variable
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the variable mask
     * @param proxyType indicates the type of proxy that can perform value substitutions (or null if not applicable)
     */
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask, String proxyType);

    /**
     * Publish a global variable.
     *
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     */
    public void publishGlobalVariable(String name, Object value, HobsonVariable.Mask mask);

    /**
     * Publish a global variable.
     *
     * @param name the name of the new variable to publish
     * @param value the value of the new variable (or null if not known)
     * @param mask the access mask of the new variable
     * @param proxyType indicates the type of proxy that can perform value substitutions (or null if not applicable)
     */
    public void publishGlobalVariable(String name, Object value, HobsonVariable.Mask mask, String proxyType);

    /**
     * Execute a recurring task.
     *
     * @param runnable a task to execute
     * @param initialDelay how long to wait for the first execution
     * @param time the wait interval between executions
     * @param unit the temporal unit for the time argument
     */
    public void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit);

    /**
     * Sets a configuration property for a specific device.
     *
     * @param ctx the context of the device that owns the configuration
     * @param name the name of the variable
     * @param value the value of the variable
     * @param overwrite whether to overwrite the property if it already exists
     */
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite);

    /**
     * Sets the DeviceManager instance the plugin should use. This will be called before the init() method.
     *
     * @param deviceManager a DeviceManager
     */
    public void setDeviceManager(DeviceManager deviceManager);

    /**
     * Sets the DiscoManager instance the plugin should use. This will be called before the init() method.
     *
     * @param discoManager a DiscoManager
     */
    public void setDiscoManager(DiscoManager discoManager);

    /**
     * Sets the EventManager instance the plugin should use. This will be called before the init() method.
     *
     * @param eventManager a EventManager
     */
    public void setEventManager(EventManager eventManager);

    /**
     * Sets the HubManager instance the plugin should use. This will be called before the init() method.
     *
     * @param hubManager a HubManager
     */
    public void setHubManager(HubManager hubManager);

    /**
     * Sets the PluginManager instance the plugin should use. This will be called before the init() method.
     *
     * @param pluginManager a PluginManager
     */
    public void setPluginManager(PluginManager pluginManager);

    /**
     * Sets the TaskManager instance the plugin should use. This will be called before the init() method.
     *
     * @param taskManager a TaskManager
     */
    public void setTaskManager(TaskManager taskManager);

    /**
     * Sets the TelemetryManager instance the plugin should use. This will be called before the init() method.
     *
     * @param telemetryManager a TelemetryManager
     */
    public void setTelemetryManager(TelemetryManager telemetryManager);

    /**
     * Sets the VariableManager instance the plugin should use. This will be called before the init() method.
     *
     * @param variableManager a VariableManager
     */
    public void setVariableManager(VariableManager variableManager);

    /**
     * Execute a task using the plugin event loop. Note that this will tie up the event loop while the task is being
     * executed so this should not be used for long running tasks.
     *
     * @param runnable a task to execute
     *
     * @return a Future for monitoring the task execution status
     */
    public Future submitInEventLoop(Runnable runnable);
}
