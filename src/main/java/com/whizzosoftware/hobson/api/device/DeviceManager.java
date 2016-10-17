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

    boolean isDeviceAvailable(DeviceContext ctx);

    /**
     * Called by plugins to publish device proxies to the device registry and start them.
     *
     * @param device the DeviceProxy to publish
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
     * Sets the value of a device variable.
     *
     * @param vctx the variable context
     * @param value the value to set
     */
    Future setDeviceVariable(DeviceVariableContext vctx, Object value);

    /**
     * Sets the values of device variables.
     *
     * @param values a Map of variable name to value
     */
    Future setDeviceVariables(Map<DeviceVariableContext,Object> values);

    void updateDevice(HobsonDeviceDescriptor device);

    /**
     *  Deprecated methods
     */

    /**
     * Activates a device passport.
     *
     * @param hctx the hub context
     * @param deviceId the device ID
     *
     * @return a DevicePassport
     */
    DevicePassport activateDevicePassport(HubContext hctx, String deviceId);

    /**
     * Creates a new device passport with a globally unique ID.
     *
     * @param hctx the hub context
     * @param deviceId the globally unique device ID
     *
     * @return the device secret
     */
    DevicePassport createDevicePassport(HubContext hctx, String deviceId);

    /**
     * Deletes a device passport.
     *
     * @param hctx the hub context
     * @param id the passport ID
     */
    void deleteDevicePassport(HubContext hctx, String id);

    /**
     * Verifies a device passport.
     *
     * @param hctx the hub context
     * @param id the globally unique device ID
     * @param secret the secret to verify
     *
     * @return a boolean indicating whether the device ID and secret are valid
     */
    boolean verifyDevicePassport(HubContext hctx, String id, String secret);

    /**
     * Returns a collection of all created device passports.
     *
     * @param hctx the hub context
     *
     * @return a Collection of DevicePassport instances
     */
    Collection<DevicePassport> getDevicePassports(HubContext hctx);

    /**
     * Retrieves a device passport.
     *
     * @param hctx the hub context
     * @param id the passport ID
     *
     * @return a DevicePassport instance
     */
    DevicePassport getDevicePassport(HubContext hctx, String id);

    /**
     * Resets a device passport to its initially created state. This will allow a device to successfully
     * re-activate the passport.
     *
     * @param hctx the hub context
     * @param id the globally unique device ID
     */
    void resetDevicePassport(HubContext hctx, String id);
}
