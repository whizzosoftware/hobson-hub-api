/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.device.proxy.DeviceProxy;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariable;
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
     * Returns all devices published by a hub.
     *
     * @param hctx the context of the hub that published the devices
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<DeviceDescription> getAllDeviceDescriptions(HubContext hctx);

    /**
     * Returns all devices published by a plugin.
     *
     * @param pctx the context of the plugin that published the devices
     *
     * @return a Collection of HobsonDevice instances
     *
     * @since hobson-hub-api 0.1.6
     */
    Collection<DeviceDescription> getAllDeviceDescriptions(PluginContext pctx);

    /**
     * Returns all variable names published by all devices.
     *
     * @param hctx the hub context
     *
     * @return a set of Strings
     */
    Collection<String> getAllDeviceVariableNames(HubContext hctx);

    /**
     * Returns a specific device.
     *
     * @param dctx the context of the device to retrieve
     *
     * @return a HobsonDevice instance (or null if it wasn't found)
     * @throws DeviceNotFoundException if device isn't found
     *
     * @since hobson-hub-api 0.1.6
     */
    DeviceDescription getDeviceDescription(DeviceContext dctx);

    /**
     * Returns the value of a specific device variable.
     *
     * @param vctx the variable context
     *
     * @return the value of the variable
     */
    DeviceVariable getDeviceVariable(DeviceVariableContext vctx);

    /**
     * Returns the values of all device variables.
     *
     * @param dctx the device context
     *
     * @return a Collection of VariableValue objects
     */
    Collection<DeviceVariable> getDeviceVariables(DeviceContext dctx);

    /**
     * Returns a list of device types that a plugin supports.
     *
     * @param pctx the plugin context
     *
     * @return a Collection of DeviceType objects
     */
    Collection<DeviceType> getPluginDeviceTypes(PluginContext pctx);

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
     * Returns a specific device's configuration.
     *
     * @param dctx the context of the desired device
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    PropertyContainer getDeviceConfiguration(DeviceContext dctx);

    /**
     * Returns a specific device's configuration class.
     *
     * @param dctx the context of the desired device
     *
     * @return a PropertyContainerClass object
     */
    PropertyContainerClass getDeviceConfigurationClass(DeviceContext dctx);

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
     * Returns the last time the device was contacted.
     *
     * @param dctx the context of the device
     *
     * @return a Long value (or null if it has never been contacted)
     */
    Long getDeviceLastCheckIn(DeviceContext dctx);

    /**
     * Returns the tags for a device.
     *
     * @param dctx the device context
     *
     * @return a set of String tags (or null if none are defined)
     */
    Set<String> getDeviceTags(DeviceContext dctx);

    /**
     * Returns a specific device type's configuration class.
     *
     * @param pctx the context of the desired device
     * @param type the device type
     *
     * @return a PropertyContainerClass object
     */
    PropertyContainerClass getDeviceTypeConfigurationClass(PluginContext pctx, DeviceType type);

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
     * Indicates whether a device has a specific variable.
     *
     * @param vctx the variable context
     *
     * @return a boolean
     */
    boolean hasDeviceVariableValue(DeviceVariableContext vctx);

    /**
     * Returns whether a device is available.
     *
     * @param dctx the context of the device
     *
     * @return a boolean
     */
    boolean isDeviceAvailable(DeviceContext dctx);

    /**
     * Publishes a type of device a plugin knows how to create.
     *
     * @param pctx
     * @param type
     * @param configProperties
     */
    void publishDeviceType(PluginContext pctx, DeviceType type, TypedProperty[] configProperties);

    /**
     * Called by plugins to publish device proxies to the device registry and start them.
     *
     * @param device the DeviceProxy to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    Future publishDevice(PluginContext pctx, DeviceProxy device, Map<String,Object> config);

    /**
     * Resets a device passport to its initially created state. This will allow a device to successfully
     * re-activate the passport.
     *
     * @param hctx the hub context
     * @param id the globally unique device ID
     */
    void resetDevicePassport(HubContext hctx, String id);

    /**
     * Sends a hint to the appropriate plugin to attempt to add a new device. This is
     * typically used when a plugin is interfacing with hardware that it isn't able
     * to discover on its own.
     *
     * @param desc the description of the device that is being requested
     * @param config the desired configuration of the new device
     */
    Future sendDeviceHint(DeviceDescription desc, PropertyContainer config);

    /**
     * Updates a device's availability.
     *
     * @param dctx the device context
     * @param checkInTime the check-in time
     */
    void setDeviceAvailability(DeviceContext dctx, boolean available, Long checkInTime);

    /**
     * Set a device configuration property.
     *
     * @param dctx the context of the target device
     * @param name the configuration property name
     * @param value the configuration property value
     * @param overwrite indicates whether an existing configuration value should be overwritten
     */
    void setDeviceConfigurationProperty(DeviceContext dctx, String name, Object value, boolean overwrite);

    /**
     * Sets device configuration properties.
     *
     * @param dctx the context of the target device
     * @param values a map of configuration property name to values
     * @param overwrite indicates whether an existing configuration value should be overwritten
     */
    void setDeviceConfigurationProperties(DeviceContext dctx, Map<String,Object> values, boolean overwrite);

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
}
