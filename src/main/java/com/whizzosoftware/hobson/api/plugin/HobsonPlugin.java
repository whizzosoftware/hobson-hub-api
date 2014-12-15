/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.action.HobsonAction;
import com.whizzosoftware.hobson.api.action.ActionManager;
import com.whizzosoftware.hobson.api.config.ConfigurationPropertyMetaData;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.disco.DeviceAdvertisement;
import com.whizzosoftware.hobson.api.disco.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventListener;
import com.whizzosoftware.hobson.api.event.EventManager;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.task.TaskProvider;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.VariableManager;
import io.netty.util.concurrent.Future;

import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Interface for all Hobson Hub plugins.
 *
 * @author Dan Noguerol
 */
public interface HobsonPlugin extends EventListener {
    /**
     * Returns the plugin ID.
     *
     * @return the plugin ID
     */
    public String getId();

    /**
     * Returns the plugin name.
     *
     * @return the plugin name
     */
    public String getName();

    /**
     * Returns the plugin version string.
     *
     * @return the plugin version
     */
    public String getVersion();

    /**
     * Returns the type of plugin.
     *
     * @return a PluginType instance
     */
    public PluginType getType();

    /**
     * Returns the status of the plugin.
     *
     * @return a PluginStatus instance
     */
    public PluginStatus getStatus();

    /**
     * Indicates whether this plugin is configurable.
     *
     * @return a boolean
     */
    public boolean isConfigurable();

    /**
     * Returns the configuration property meta-data associated with this plugin.
     *
     * @return a Collection of ConfigurationPropertyMetaData objects
     */
    public Collection<ConfigurationPropertyMetaData> getConfigurationPropertyMetaData();

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
     * Sets the ActionManager instance the plugin should use. This will be called before the init() method.
     *
     * @param actionManager an ActionManager
     */
    public void setActionManager(ActionManager actionManager);

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
     * Sets the VariableManager instance the plugin should use. This will be called before the init() method.
     *
     * @param variableManager a VariableManager
     */
    public void setVariableManager(VariableManager variableManager);

    /**
     * Sets the TaskManager instance the plugin should use. This will be called before the init() method.
     *
     * @param taskManager a TaskManager
     */
    public void setTaskManager(TaskManager taskManager);

    /**
     * Execute a task using the plugin event loop. Note that this will tie up the event loop while the task is being
     * executed so this should not be used for long running tasks.
     *
     * @param runnable a task to execute
     */
    public void executeInEventLoop(Runnable runnable);

    /**
     * Execute a task using the plugin event loop. Note that this will tie up the event loop while the task is being
     * executed so this should not be used for long running tasks.
     *
     * @param runnable a task to execute
     *
     * @return a Future for monitoring the task execution status
     */
    public Future submitInEventLoop(Runnable runnable);

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
     * @param id the ID of the device
     * @param name the name of the variable
     * @param value the value of the variable
     * @param overwrite whether to overwrite the property if it already exists
     */
    public void setDeviceConfigurationProperty(String id, String name, Object value, boolean overwrite);

    /**
     * Retrieves a specific device variable.
     *
     * @param deviceId the ID Of the device that published the variable
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     * @throws com.whizzosoftware.hobson.api.variable.DeviceVariableNotFoundException if the variable wasn't found
     */
    public HobsonVariable getDeviceVariable(String deviceId, String variableName);

    /**
     * Publish a global variable.
     *
     * @param variable the variable to publish
     */
    public void publishGlobalVariable(HobsonVariable variable);

    /**
     * Publish a device variable.
     *
     * @param deviceId the ID of the device publishing the variable
     * @param variable the variable to publish
     */
    public void publishDeviceVariable(String deviceId, HobsonVariable variable);

    /**
     * Publish a task provider.
     *
     * @param taskProvider the task provider object
     */
    public void publishTaskProvider(TaskProvider taskProvider);

    /**
     * Publish an action.
     *
     * @param action the action to publish
     */
    public void publishAction(HobsonAction action);

    /**
     * Fires a notification that variables has been successfully updated.
     *
     * @param updates a List of VariableUpdate instances
     */
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates);

    /**
     * Fires a notification that a variable has been successfully updated.
     *
     * @param variableUpdate a VariableUpdate instance
     */
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate);

    /**
     * Callback method invoked when the plugin starts up.
     *
     * @param config the plugin configuration
     */
    public void onStartup(Dictionary config);

    /**
     * Callback method invoked when the plugin shuts down.
     */
    public void onShutdown();

    /**
     * Callback that gives a plugin the opportunity to perform work. This will be called every
     * refresh interval.
     */
    public void onRefresh();

    /**
     * Called when the plugin's configuration has changed.
     *
     * @param config the new configuration
     */
    public void onPluginConfigurationUpdate(Dictionary config);

    /**
     * Called when the plugin device's configuration has changed.
     *
     * @param deviceId the device ID
     * @param config the new configuration
     */
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config);

    /**
     * Callback when a request to set a device variable has been received.
     *
     * @param deviceId the ID Of the device that published the variable
     * @param variableName the variable name
     * @param value the variable value
     */
    public void onSetDeviceVariable(String deviceId, String variableName, Object value);
}
