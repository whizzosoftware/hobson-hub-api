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
     * Indicates whether this device is in an error state.
     *
     * @return a boolean
     */
    public boolean hasError();

    /**
     * Returns error information if the device is in an error state (or null otherwise).
     *
     * @return a String containing an error message
     */
    public DeviceError getError();

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
     * Indicates whether this device can provide telemetry data.
     *
     * @return a boolean
     */
    public boolean hasTelemetry();

    /**
     * Returns a an object that can be used to invoke methods related to device runtime execution.
     *
     * @return a HobsonPluginExecution instance (or null if this object doesn't support a runtime)
     */
    public HobsonDeviceRuntime getRuntime();
}
