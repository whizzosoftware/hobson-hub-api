/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.telemetry.TelemetryInterval;
import com.whizzosoftware.hobson.api.variable.telemetry.TemporalValue;

import java.util.Collection;
import java.util.List;

/**
 * An interface for managing global and device variables. The variable manager is a separate entity because there are
 * variables, such as global variables, that are not associated with a particular device.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface VariableManager {
    /**
     * Publish a new global variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID publishing the variable
     * @param var the variable object
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishGlobalVariable(String userId, String hubId, String pluginId, HobsonVariable var);

    /**
     * Returns a collection of all published global variables.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonVariable> getGlobalVariables(String userId, String hubId);

    /**
     * Returns a specific published global variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param name the variable name
     *
     * @return a HobsonVariable instance
     * @throws GlobalVariableNotFoundException if not found
     */
    public HobsonVariable getGlobalVariable(String userId, String hubId, String name);

    /**
     * Unpublishes a global variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID that published the variable
     * @param name the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishGlobalVariable(String userId, String hubId, String pluginId, String name);

    /**
     * Publish a new device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device publishing the variable
     * @param deviceId the device ID publishing the variable
     * @param var the variable object
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDeviceVariable(String userId, String hubId, String pluginId, String deviceId, HobsonVariable var);

    /**
     * Unpublishes a device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name);

    /**
     * Unpublishes all variables published by a device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variables
     * @param deviceId the device ID that published the variables
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllDeviceVariables(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Unpublishes all variables published by a plugin's devices.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllPluginVariables(String userId, String hubId, String pluginId);

    /**
     * Returns all published variables for a device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID that published the variables
     *
     * @return a Collection of HobsonVariable instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonVariable> getDeviceVariables(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Returns all the variable change IDs that can be produced by a device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID
     *
     * @return a Collection of variable change IDs
     */
    public Collection<String> getDeviceVariableChangeIds(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Returns a specific published variable for a device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     *
     * @return a HobsonVariable instance (or null if not found)
     * @throws DeviceVariableNotFoundException if not found
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonVariable getDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name);

    /**
     * Indicates whether a device variable has been published.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name);

    /**
     * Sets a specific device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     * @param value the variable value
     *
     * @return the last update time prior to this set call
     * @throws DeviceVariableNotFoundException if not found
     *
     * @since hobson-hub-api 0.1.6
     */
    public Long setDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name, Object value);

    /**
     * Writes telemetry data for a device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     * @param value the variable value
     * @param time the time the variable held the value (in epoch time)
     */
    public void writeDeviceVariableTelemetry(String userId, String hubId, String pluginId, String deviceId, String name, Object value, long time);

    /**
     * Retrieves telemetry data for a device variable.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device that published the variable
     * @param deviceId the device ID that published the variable
     * @param name the variable name
     * @param endTime the end time for which data is requested (in epoch time)
     * @param interval an interval representing the amount of data requested (endTime - interval == startTime)
     *
     * @return a Collection of TemporalValue instances
     */
    public Collection<TemporalValue> getDeviceVariableTelemetry(String userId, String hubId, String pluginId, String deviceId, String name, long endTime, TelemetryInterval interval);

    /**
     * Updates a variable and publishes an update notification.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param plugin the plugin firing the update
     * @param update the VariableUpdate
     *
     * @since hobson-hub-api 0.1.6
     */
    public void fireVariableUpdateNotification(String userId, String hubId, HobsonPlugin plugin, VariableUpdate update);

    /**
     * Updates variables and publishes an update notification.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param plugin the plugin firing the update
     * @param updates the VariableUpdate
     *
     * @since hobson-hub-api 0.1.6
     */
    public void fireVariableUpdateNotifications(String userId, String hubId, HobsonPlugin plugin, List<VariableUpdate> updates);
}
