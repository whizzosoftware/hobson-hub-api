/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;
import io.netty.util.concurrent.Future;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An interface for managing Hobson devices.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DeviceManager {
    /**
     * Deletes a specific device.
     *
     * @param dctx the context of the device to delete
     */
    void deleteDevice(DeviceContext dctx);

    /**
     * Returns a specific device.
     *
     * @param dctx the context of the device to retrieve
     *
     * @return a HobsonDeviceDescription instance (or null if it wasn't found)
     *
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    HobsonDeviceDescriptor getDevice(DeviceContext dctx);

    /**
     * Returns a specific device's configuration.
     *
     * @param dctx the context of the desired device
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    PropertyContainer getDeviceConfiguration(DeviceContext dctx);

    /**
     * Returns a device configuration property.
     *
     * @param dctx the context of the device to retrieve configuration for
     * @param name the configuration property name
     *
     * @return the property value (or null if not set)
     */
    Object getDeviceConfigurationProperty(DeviceContext dctx, String name);

    /**
     * Returns the last time a device checked in.
     *
     * @param dctx the device context
     *
     * @return the last check in time (or null if it has never checked in)
     */
    Long getDeviceLastCheckin(DeviceContext dctx);

    /**
     * Returns all devices published by a plugin.
     *
     * @param pctx the context of the plugin that published the devices
     *
     * @return a Collection of HobsonDeviceDescription instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonDeviceDescriptor> getDevices(PluginContext pctx);

    /**
     * Returns all devices published by a hub.
     *
     * @param hctx the context of the hub that published the devices
     *
     * @return a Collection of HobsonDeviceDescription instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<HobsonDeviceDescriptor> getDevices(HubContext hctx);

    /**
     * Returns the state of a device variable.
     *
     * @param ctx the variable context
     *
     * @return a DeviceVariableState instance
     */
    DeviceVariableState getDeviceVariable(DeviceVariableContext ctx);

    /**
     * Returns all variable names published by all devices.
     *
     * @param hctx the hub context
     *
     * @return a set of Strings
     */
    Collection<String> getDeviceVariableNames(HubContext hctx);

    /**
     * Indicates whether a device has been published.
     *
     * @param dctx the context of the device to check
     *
     * @return a boolean
     *
     * @since hobson-hub-api 0.1.6
     */
    boolean hasDevice(DeviceContext dctx);

    /**
     * Indicates whether a device is currently available.
     *
     * @param ctx the device context
     *
     * @return a boolean
     */
    boolean isDeviceAvailable(DeviceContext ctx);

    /**
     * Called by plugins to publish device proxies to the device registry and start them.
     *
     * @param device the DeviceProxy to publish
     * @param config the device configuration (or null if it is not known)
     * @param runnable a Runnable to execute after the device has been successfully published
     *
     * @return a Future representing the result of publishing the device
     *
     * @since hobson-hub-api 0.1.6
     */
    Future publishDevice(HobsonDeviceProxy device, Map<String, Object> config, Runnable runnable);

    /**
     * Sets device configuration properties.
     *
     * @param dctx the context of the target device
     * @param configClass the configuration class
     * @param values a map of configuration property name to values
     */
    void setDeviceConfiguration(DeviceContext dctx, PropertyContainerClass configClass, Map<String, Object> values);

    /**
     * Set a device configuration property.
     *
     * @param dctx the context of the target device
     * @param configClass the configuration class
     * @param name the configuration property name
     * @param value the configuration property value
     */
    void setDeviceConfigurationProperty(DeviceContext dctx, PropertyContainerClass configClass, String name, Object value);

    /**
     * Sets the name of a device.
     *
     * @param dctx the device context
     * @param name the new name of the device
     */
    void setDeviceName(DeviceContext dctx, String name);

    /**
     * Sets the tag(s) for a device.
     *
     * @param dctx the device context
     * @param tags the tags
     */
    void setDeviceTags(DeviceContext dctx, Set<String> tags);

    /**
     * Updates information about a previously published device.
     *
     * @param device the device descriptor information
     */
    void updateDevice(HobsonDeviceDescriptor device);
}
