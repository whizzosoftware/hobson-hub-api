/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

/**
 * Interface representing a Hobson device.
 *
 * @author Dan Noguerol
 */
public interface HobsonDevice {
    /**
     * Returns the context of this device.
     *
     * @return a DeviceContext instance
     */
    public DeviceContext getContext();

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
     * Indicates whether this device has a preferred variable.
     *
     * @return a boolean
     */
    public boolean hasPreferredVariableName();

    /**
     * Returns the name of the device's "preferred variable" -- the one variable it deems the most important to expose
     * to the user.
     *
     * @return the variable name (or null if it doesn't have one)
     */
    public String getPreferredVariableName();

    /**
     * Returns information about the configurable aspects of the device.
     *
     * @return a PropertyContainerClass object (or null if there is none)
     */
    public PropertyContainerClass getConfigurationClass();

    /**
     * Indicates whether this device can provide telemetry data.
     *
     * @return a boolean
     */
    public boolean isTelemetryCapable();

    /**
     * Returns a an object that can be used to invoke methods related to device runtime execution.
     *
     * @return a HobsonPluginExecution instance (or null if this object doesn't support a runtime)
     */
    public HobsonDeviceRuntime getRuntime();
}
