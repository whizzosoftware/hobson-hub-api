/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;

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
     * Creates a new bootstrap object for a new device with a globally unique ID.
     *
     *
     * @param hubContext the hub context
     * @param deviceId the globally unique device ID
     *
     * @return the device secret
     */
    public DeviceBootstrap createDeviceBootstrap(HubContext hubContext, String deviceId);

    /**
     * Returns a collection of all created device bootstraps.
     *
     * @param hubContext the hub context
     *
     * @return a Collection of DeviceBootstrap instances
     */
    public Collection<DeviceBootstrap> getDeviceBootstraps(HubContext hubContext);

    /**
     * Retrieves a device bootstrap.
     *
     * @param hubContext the hub context
     * @param id the bootstrap ID
     *
     * @return a DeviceBootstrap instance
     */
    public DeviceBootstrap getDeviceBootstrap(HubContext hubContext, String id);

    /**
     * Registers a device bootstrap.
     *
     * @param hubContext the hub context
     * @param deviceId the device ID
     *
     * @return a DeviceBootstrap
     */
    public DeviceBootstrap registerDeviceBootstrap(HubContext hubContext, String deviceId);

    /**
     * Deletes a device bootstrap.
     *
     * @param hubContext the hub context
     * @param id the bootstrap ID
     */
    public void deleteDeviceBootstrap(HubContext hubContext, String id);

    /**
     * Authenticates a device bootstrap.
     *
     * @param hubContext the hub context
     * @param id the globally unique device ID
     * @param secret the secret to verify
     *
     * @return a boolean indicating whether the device ID and secret are valid
     */
    public boolean verifyDeviceBootstrap(HubContext hubContext, String id, String secret);

    /**
     * Resets a device bootstrap to its initially created state. This will allow a device to successfully
     * re-request its bootstrap information.
     *
     * @param hubContext the hub context
     * @param id the globally unique device ID
     */
    public void resetDeviceBootstrap(HubContext hubContext, String id);

    /**
     * Returns all devices published by a hub.
     *
     * @param ctx the context of the hub that published the devices
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonDevice> getAllDevices(HubContext ctx);

    /**
     * Returns all devices published by a plugin.
     *
     * @param ctx the context of the plugin that published the devices
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonDevice> getAllDevices(PluginContext ctx);

    /**
     * Returns a specific device.
     *
     * @param ctx the context of the device to retrieve
     *
     * @return a HobsonDevice instance (or null if it wasn't found)
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonDevice getDevice(DeviceContext ctx);

    /**
     * Returns a specific device's configuration.
     *
     * @param ctx the context of the device to retrieve configuration for
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public PropertyContainer getDeviceConfiguration(DeviceContext ctx);

    /**
     * Returns a device configuration property.
     *
     * @param ctx the context of the device to retrieve configuration for
     * @param name the configuration property name
     *
     * @return the property value (or null if not set)
     */
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name);

    /**
     * Indicates whether a device has been published.
     *
     * @param ctx the context of the device to check
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    public boolean hasDevice(DeviceContext ctx);

    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param device the HobsonDevice to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDevice(HobsonDevice device);

    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param device the HobsonDevice to publish
     * @param republish indicates whether this is a forced republish of an existing device
     *
     * @since hobson-hub-api 0.4.2
     */
    public void publishDevice(HobsonDevice device, boolean republish);

    /**
     * Sets configuration for a device.
     *  @param ctx the context of the target device
     * @param config the new configuration
     */
    public void setDeviceConfiguration(DeviceContext ctx, PropertyContainer config);

    /**
     * Set a device configuration property.
     *
     * @param ctx the context of the target device
     * @param name the configuration property name
     * @param value the configuration property value
     * @param overwrite indicates whether an existing configuration value should be overwritten
     */
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite);

    /**
     * Sets device configuration properties.
     *
     * @param ctx the context of the target device
     * @param values a map of configuration property name to values
     * @param overwrite indicates whether an existing configuration value should be overwritten
     */
    public void setDeviceConfigurationProperties(DeviceContext ctx, Map<String,Object> values, boolean overwrite);

    /**
     * Sets the name of a device.
     *
     * @param ctx the context of the target device
     * @param name the new name of the device
     *
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    public void setDeviceName(DeviceContext ctx, String name);

    /**
     * Stops and unpublishes a device associated with a specific plugin. This allows plugins that require it
     * (e.g. the RadioRA plugin) to unpublish individual devices.
     *
     * @param ctx the context of the target device
     * @param executor an executor used to call the plugin's onShutdown() lifecycle method
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDevice(DeviceContext ctx, EventLoopExecutor executor);

    /**
     * Stops an unpublishes all devices associated with a specific plugin.
     *
     * @param ctx the context of the plugin that published the devices
     * @param executor an executor used to call the plugins' onShutdown() lifecycle method
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllDevices(PluginContext ctx, EventLoopExecutor executor);
}
