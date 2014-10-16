/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.manager;

import com.whizzosoftware.hobson.api.device.HobsonDevice;

import java.util.Collection;

/**
 * An interface for managing Hobson devices.
 *
 * @author Dan Noguerol
 */
public interface DeviceManager {
    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param device the HobsonDevice to publish
     */
    public void publishAndStartDevice(HobsonDevice device);

    /**
     * Retrieves all known devices.
     *
     * @return a Collection of HobsonDevice instances
     */
    public Collection<HobsonDevice> getAllDevices();

    /**
     * Retrieves all devices published by a particular plugin
     *
     * @param pluginId the plugin ID
     *
     * @return a Collection of HobsonDevice instances
     */
    public Collection<HobsonDevice> getAllPluginDevices(String pluginId);

    /**
     * Retrieves a specific device.
     *
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a HobsonDevice instance (or null if it wasn't found)
     * @throws DeviceNotFoundException if device isn't found
     */
    public HobsonDevice getDevice(String pluginId, String deviceId);

    /**
     * Indicates whether a device has been published.
     *
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a boolean
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
     */
    public void setDeviceName(String pluginId, String deviceId, String name);

    /**
     * Stops an unpublishes all devices associated with a specific plugin.
     *
     * @param pluginId the plugin ID
     */
    public void stopAndUnpublishAllDevices(String pluginId);

    /**
     * Stops and unpublishes a device associated with a specific plugin. This allows plugins that require it
     * (e.g. the RadioRA plugin) to unpublish individual devices.
     *
     * @param pluginId the plugin ID
     * @param deviceId the device ID
     */
    public void stopAndUnpublishDevice(String pluginId, String deviceId);
}
