/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.ConfigurationPropertyMetaData;

import java.util.Collection;
import java.util.Dictionary;

/**
 * Interface representing a Hobson device.
 *
 * @author Dan Noguerol
 */
public interface HobsonDevice {
    /**
     * Returns the ID of the plugin which created this device.
     *
     * @return a plugin ID
     */
    public String getPluginId();

    /**
     * Returns the device ID.
     *
     * @return the device ID
     */
    public String getId();

    /**
     * Returns the device name.
     *
     * @return the device name
     */
    public String getName();

    /**
     * Returns the device's type.
     *
     * @return the device type
     */
    public DeviceType getType();

    /**
     * Returns the name of the device's "preferred variable" -- the one variable it deems the most important to expose
     * to the user.
     *
     * @return the variable name (or null if it doesn't have one)
     */
    public String getPreferredVariableName();

    /**
     * Returns meta data about the configurable aspects of the device.
     *
     * @return a Collection of ConfigurationMetaData objects (or null if there is none)
     */
    public Collection<ConfigurationPropertyMetaData> getConfigurationPropertyMetaData();

    /**
     * Sets a device configuration property.
     *
     * @param name the configuration property name
     * @param value the value
     * @param overwrite whether to overwrite a previously set value
     */
    public void setConfigurationProperty(String name, Object value, boolean overwrite);

    /**
     * Indicates whether this device can provide telemetry data.
     *
     * @return a boolean
     */
    public boolean hasTelemetry();

    /**
     * Returns the name(s) of the variables that should be used if telemetry is enabled for this device.
     *
     * @return an array of Strings (or null if telemetry should never be enabled for this device)
     */
    public String[] getTelemetryVariableNames();

    /**
     * Callback method invoked when the device is started.
     */
    public void onStartup();

    /**
     * Callback method invoked when the device is stopped.
     */
    public void onShutdown();

    /**
     * Called when the device's configuration has changed.
     *
     * @param config the updated configuration
     */
    public void onDeviceConfigurationUpdate(Dictionary config);

    /**
     * Called when a device should set one of its variables. The actual setting of the variable should be performed
     * asynchronously and this method must return before the variable change has taken effect. It is the responsibility
     * of the device to fire a VariableUpdateEvent when the variable change has been confirmed.
     *
     * Note: This method should only be called by the framework and never directly by third-party plugin code. To
     * update a device variable from a plugin, use the VariableManager.setDeviceVariable() method.
     *
     * @param variableName the name of the variable
     * @param value the value of the variable
     */
    public void onSetVariable(String variableName, Object value);
}
