/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.hub.HubContext;

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
     * @param ctx the context of the target device
     * @param enabled whether to enable telemetry
     */
    public void enableDeviceTelemetry(DeviceContext ctx, boolean enabled);

    /**
     * Returns all hub devices for which telemetry has been enabled.
     *
     * @param ctx the context of the hub that published the devices
     *
     * @return a Collection of HobsonDevice instances
     */
    public Collection<HobsonDevice> getAllTelemetryEnabledDevices(HubContext ctx);

    /**
     * Retrieves telemetry data for a specific device.
     *
     * @param ctx the context of the target device
     * @param endTime the end time for the returned data
     * @param interval how much data to return (determines the start time of the data)
     *
     * @return a Map (keyed by variable name) to Collections of TemporalValue instances
     */
    public Map<String,Collection<TemporalValue>> getDeviceTelemetry(DeviceContext ctx, long endTime, TelemetryInterval interval);

    /**
     * Retrieves telemetry data for a device variable.
     *
     * @param ctx the context of the target device
     * @param name the variable name
     * @param endTime the end time for which data is requested (in epoch time)
     * @param interval an interval representing the amount of data requested (endTime - interval == startTime)
     *
     * @return a Collection of TemporalValue instances
     */
    public Collection<TemporalValue> getDeviceVariableTelemetry(DeviceContext ctx, String name, long endTime, TelemetryInterval interval);

    /**
     * Indicates whether a device's telemetry is enabled.
     *
     * @param ctx the context of the target device
     *
     * @return a boolean
     */
    public boolean isDeviceTelemetryEnabled(DeviceContext ctx);

    /**
     * Writes telemetry data for a specific device.
     *
     * @param ctx the context of the target device
     * @param values a Map (keyed by variable name) to a TemporalValue
     */
    public void writeDeviceTelemetry(DeviceContext ctx, Map<String,TemporalValue> values);

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
