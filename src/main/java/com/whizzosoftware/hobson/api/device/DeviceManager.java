/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.telemetry.TelemetryInterval;
import com.whizzosoftware.hobson.api.variable.telemetry.TemporalValue;

import java.util.Collection;
import java.util.Map;

/**
 * An interface for managing Hobson devices.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DeviceManager {
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
     * Returns all published devices.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonDevice> getAllDevices(String userId, String hubId);

    /**
     * Returns all devices published by a particular plugin
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonDevice> getAllPluginDevices(String userId, String hubId, String pluginId);

    /**
     * Returns all devices for which telemetry has been enabled.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a Collection of HobsonDevice instances
     */
    public Collection<HobsonDevice> getAllTelemetryEnabledDevices(String userId, String hubId);

    /**
     * Returns a specific device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a HobsonDevice instance (or null if it wasn't found)
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonDevice getDevice(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Returns the device level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID that owns the device
     * @param deviceId the device ID that owns the configuration
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getDeviceConfiguration(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Returns a device level configuration property.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID that owns the device
     * @param deviceId the device ID that owns the configuration property.
     * @param name the configuration property name
     *
     * @return the property value (or null if not set)
     */
    public Object getDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name);

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
     * Indicates whether a device has been published.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasDevice(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Indicates whether a specific device has telemetry enabled.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID of the device
     * @param deviceId the device ID
     *
     * @return a boolean
     */
    public boolean isDeviceTelemetryEnabled(String userId, String hubId, String pluginId, String deviceId);

    /**
     * Set a device level configuration property.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID that owns the device
     * @param deviceId the device ID that owns the configuration property.
     * @param name the configuration property name
     * @param value the configuration property value
     * @param overwrite indicates whether an existing key should be overwritten
     */
    public void setDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name, Object value, boolean overwrite);

    /**
     * Sets the name of a specific device.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     * @param name the new name of the device
     *
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    public void setDeviceName(String userId, String hubId, String pluginId, String deviceId, String name);

    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param plugin the HobsonPlugin instance performing the action
     * @param device the HobsonDevice to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device);

    /**
     * Stops and unpublishes a device associated with a specific plugin. This allows plugins that require it
     * (e.g. the RadioRA plugin) to unpublish individual devices.
     *
     * @param plugin the HobsonPlugin instance performing the action
     * @param deviceId the device ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDevice(HobsonPlugin plugin, String deviceId);

    /**
     * Stops an unpublishes all devices associated with a specific plugin.
     *
     * @param plugin the HobsonPlugin instance performing the action
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllDevices(HobsonPlugin plugin);

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
}
