/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.device.DeviceError;
import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariable;

import java.util.Collection;

/**
 * Device proxies are created by plugins and represent the link between the Hobson runtime and
 * the physical hardware being controlled.
 *
 * @author Dan Noguerol
 */
public interface DeviceProxy {
    int AVAILABILITY_TIMEOUT_INTERVAL = 600000; // default to 10 minutes

    PropertyContainerClass getConfigurationClass();

    /**
     * Returns the device ID of this device proxy.
     *
     * @return a String
     */
    String getDeviceId();

    String getDefaultName();

    /**
     * Returns the device's type.
     *
     * @return the device type
     */
    DeviceType getDeviceType();

    /**
     * Returns the name of the manufacturer of the device.
     *
     * @return the device manufacturer
     */
    String getManufacturerName();

    /**
     * Returns the manufacturer's version of the device.
     *
     * @return a version string
     */
    String getManufacturerVersion();

    /**
     * Returns the model of the device.
     *
     * @return the device model
     */
    String getModelName();


    /**
     * Returns the name of the device's "preferred variable" -- the one variable it deems the most important to expose
     * to the user.
     *
     * @return the variable name (or null if it doesn't have one)
     */
    String getPreferredVariableName();

    boolean hasVariables();
//    boolean hasVariableDescriptions();

    DeviceVariable getVariable(String name);
//    DeviceVariableDescription getVariableDescription(DeviceVariableContext vctx);

    Collection<DeviceVariable> getVariables();
//    Collection<DeviceVariableDescription> getVariableDescriptions();

//    DeviceProxyVariable getVariableValue(String name);

//    Collection<DeviceProxyVariable> getVariableValues();

    boolean hasPreferredVariableName();

    boolean hasVariableValue(String name);

    /**
     * Indicates if the device has been started by the runtime.
     *
     * @return a boolean
     */
    boolean isStarted();

    /**
     * Indicates whether this device is in an error state.
     *
     * @return a boolean
     */
    boolean hasError();

    /**
     * Returns error information if the device is in an error state (or null otherwise).
     *
     * @return a String containing an error message
     */
    DeviceError getError();

    // LIFECYCLE CALLBACKS

    /**
     * Callback method invoked when the device is started.
     *
     * @param config the current device configuration
     */
    void onStartup(PropertyContainer config);

    /**
     * Callback method invoked when the device is stopped.
     */
    void onShutdown();

    /**
     * Called when the device's configuration has changed.
     *
     * @param config the updated new configuration
     */
    void onDeviceConfigurationUpdate(PropertyContainer config);

    /**
     * Called when a device should set one of its variables. The actual setting of the variable should be performed
     * asynchronously and this method must return before the variable change has taken effect. It is the responsibility
     * of the device to fire a VariableUpdateEvent when the variable change has been confirmed.
     *
     * Note: This method should only be called by the framework and never directly by third-party plugin code.
     *
     * @param variableName the name of the variable
     * @param value the value of the variable
     */
    void onSetVariable(String variableName, Object value);
}
