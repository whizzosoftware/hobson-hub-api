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
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

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
     * Activates a device passport.
     *
     * @param hubContext the hub context
     * @param deviceId the device ID
     *
     * @return a DevicePassport
     */
    DevicePassport activateDevicePassport(HubContext hubContext, String deviceId);

    /**
     * Creates a new device passport with a globally unique ID.
     *
     *
     * @param hubContext the hub context
     * @param deviceId the globally unique device ID
     *
     * @return the device secret
     */
    DevicePassport createDevicePassport(HubContext hubContext, String deviceId);

    /**
     * Deletes a device passport.
     *
     * @param hubContext the hub context
     * @param id the passport ID
     */
    void deleteDevicePassport(HubContext hubContext, String id);

    /**
     * Returns all devices published by a hub.
     *
     * @param ctx the context of the hub that published the devices
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonDevice> getAllDevices(HubContext ctx);

    /**
     * Returns all devices published by a plugin.
     *
     * @param ctx the context of the plugin that published the devices
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonDevice> getAllDevices(PluginContext ctx);

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
    HobsonDevice getDevice(DeviceContext ctx);

    /**
     * Returns a collection of all created device passports.
     *
     * @param hubContext the hub context
     *
     * @return a Collection of DevicePassport instances
     */
    Collection<DevicePassport> getDevicePassports(HubContext hubContext);

    /**
     * Retrieves a device passport.
     *
     * @param hubContext the hub context
     * @param id the passport ID
     *
     * @return a DevicePassport instance
     */
    DevicePassport getDevicePassport(HubContext hubContext, String id);

    /**
     * Returns a specific device's configuration.
     *
     * @param ctx the context of the desired device
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    PropertyContainer getDeviceConfiguration(DeviceContext ctx);

    /**
     * Returns a specific device's configuration class.
     *
     * @param ctx the context of the desired device
     *
     * @return a PropertyContainerClass object
     */
    PropertyContainerClass getDeviceConfigurationClass(DeviceContext ctx);

    /**
     * Returns a device configuration property.
     *
     * @param ctx the context of the device to retrieve configuration for
     * @param name the configuration property name
     *
     * @return the property value (or null if not set)
     */
    Object getDeviceConfigurationProperty(DeviceContext ctx, String name);

    /**
     * Returns the last time the device was contacted.
     *
     * @param ctx the context of the device
     *
     * @return a Long value (or null if it has never been contacted)
     */
    Long getDeviceLastCheckIn(DeviceContext ctx);

    /**
     * Indicates whether a device has been published.
     *
     * @param ctx the context of the device to check
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    boolean hasDevice(DeviceContext ctx);

    /**
     * Returns whether a device is available.
     *
     * @param ctx the context of the device
     *
     * @return a boolean
     */
    boolean isDeviceAvailable(DeviceContext ctx);

    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param device the HobsonDevice to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    void publishDevice(HobsonDevice device);

    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param device the HobsonDevice to publish
     * @param republish indicates whether this is a forced republish of an existing device
     *
     * @since hobson-hub-api 0.4.2
     */
    void publishDevice(HobsonDevice device, boolean republish);

    /**
     * Resets a device passport to its initially created state. This will allow a device to successfully
     * re-activate the passport.
     *
     * @param hubContext the hub context
     * @param id the globally unique device ID
     */
    void resetDevicePassport(HubContext hubContext, String id);

    /**
     * Updates the a device's availability.
     *
     * @param ctx the device context
     * @param checkInTime the check-in time
     */
    void setDeviceAvailability(DeviceContext ctx, boolean available, Long checkInTime);

    /**
     * Set a device configuration property.
     *
     * @param ctx the context of the target device
     * @param name the configuration property name
     * @param value the configuration property value
     * @param overwrite indicates whether an existing configuration value should be overwritten
     */
    void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite);

    /**
     * Sets device configuration properties.
     *
     * @param ctx the context of the target device
     * @param values a map of configuration property name to values
     * @param overwrite indicates whether an existing configuration value should be overwritten
     */
    void setDeviceConfigurationProperties(DeviceContext ctx, Map<String,Object> values, boolean overwrite);

    /**
     * Stops an unpublishes all devices associated with a specific plugin.
     *
     * @param ctx the context of the plugin that published the devices
     * @param executor an executor used to call the plugins' onShutdown() lifecycle method
     *
     * @since hobson-hub-api 0.1.6
     */
    void unpublishAllDevices(PluginContext ctx, EventLoopExecutor executor);

    /**
     * Stops and unpublishes a device associated with a specific plugin. This allows plugins that require it
     * (e.g. the RadioRA plugin) to unpublish individual devices.
     *
     * @param ctx the context of the target device
     * @param executor an executor used to call the plugin's onShutdown() lifecycle method
     *
     * @since hobson-hub-api 0.1.6
     */
    void unpublishDevice(DeviceContext ctx, EventLoopExecutor executor);

    /**
     * Verifies a device passport.
     *
     * @param hubContext the hub context
     * @param id the globally unique device ID
     * @param secret the secret to verify
     *
     * @return a boolean indicating whether the device ID and secret are valid
     */
    boolean verifyDevicePassport(HubContext hubContext, String id, String secret);
}
