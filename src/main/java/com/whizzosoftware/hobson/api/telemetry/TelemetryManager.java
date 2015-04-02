/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.device.DeviceContext;

import java.util.Collection;
import java.util.Map;

/**
 * An interface for managing device telemetry.
 *
 * @author Dan Noguerol
 */
public interface TelemetryManager {
    /**
     * Enables/disables telemetry for a specific device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID
     * @param enabled whether to enable telemetry
     */
    public void enableDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, boolean enabled);

    /**
     * Retrieves telemetry data for a specific device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID
     * @param endTime the end time for the returned data
     * @param interval how much data to return (determines the start time of the data)
     *
     * @return a Map (keyed by variable name) to Collections of TemporalValue instances
     */
    public Map<String,Collection<TemporalValue>> getDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, long endTime, TelemetryInterval interval);

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
     * Writes telemetry data for a specific device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID
     * @param values a Map (keyed by variable name) to a TemporalValue
     */
    public void writeDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, Map<String,TemporalValue> values);

    /**
     * Writes telemetry data for a device variable.
     *
     * @param ctx the context of the device that produced the data
     * @param name the variable name
     * @param value the variable value
     * @param time the time the variable held the value (in epoch time)
     */
    public void writeDeviceVariableTelemetry(DeviceContext ctx, String name, Object value, long time);
}
