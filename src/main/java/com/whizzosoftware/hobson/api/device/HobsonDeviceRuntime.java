/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.property.PropertyContainer;

/**
 * Interface for invoking methods related to device runtime functions including lifecycle callbacks.
 *
 * @author Dan Noguerol
 */
public interface HobsonDeviceRuntime {
    /**
     * Updates a device's availability information.
     *
     * @param available the device's current availability
     * @param checkInTime the last time the device checked in (or null to leave the value unchanged)
     */
    void setDeviceAvailability(boolean available, Long checkInTime);

    /**
     * Sets a device configuration property.
     *
     * @param name the configuration property name
     * @param value the value
     * @param overwrite whether to overwrite a previously set value
     */
    void setConfigurationProperty(String name, Object value, boolean overwrite);

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
     * Note: This method should only be called by the framework and never directly by third-party plugin code. To
     * update a device variable from a plugin, use the VariableManager.setDeviceVariable() method.
     *
     * @param variableName the name of the variable
     * @param value the value of the variable
     */
    void onSetVariable(String variableName, Object value);
}
