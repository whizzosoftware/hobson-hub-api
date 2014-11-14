/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

import java.util.Collection;

/**
 * An interface for managing Hobson devices.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DeviceManager {
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
     * Returns all published devices.
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonDevice> getAllDevices();

    /**
     * Returns all devices published by a particular plugin
     *
     * @param pluginId the plugin ID
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonDevice> getAllPluginDevices(String pluginId);

    /**
     * Returns a specific device.
     *
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a HobsonDevice instance (or null if it wasn't found)
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonDevice getDevice(String pluginId, String deviceId);

    /**
     * Indicates whether a device has been published.
     *
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasDevice(String pluginId, String deviceId);

    /**
     * Sets the name of a specific device.
     *
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     * @param name the new name of the device
     *
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    public void setDeviceName(String pluginId, String deviceId, String name);

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
}
