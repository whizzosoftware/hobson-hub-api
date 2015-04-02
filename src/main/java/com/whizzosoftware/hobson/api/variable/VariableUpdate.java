/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a variable update.
 *
 * @author Dan Noguerol
 */
public class VariableUpdate {
    private String pluginId;
    private String deviceId;
    private String name;
    private Object value;
    private long updateTime;

    /**
     * Constructor for global variable update.
     *
     * @param pluginId the plugin ID associated with the variable
     * @param name the variable name
     * @param value the variable value
     */
    public VariableUpdate(String pluginId, String name, Object value) {
        this(pluginId, null, name, value);
    }

    /**
     * Constructor for global variable update.
     *
     * @param pluginId the plugin ID associated with the variable
     * @param name the variable name
     * @param value the variable value
     * @param updateTime the time the variable was updated
     */
    public VariableUpdate(String pluginId, String name, Object value, long updateTime) {
        this(pluginId, null, name, value, updateTime);
    }

    /**
     * Constructor for device variable update.
     *
     * @param pluginId the plugin ID associated with the variable
     * @param deviceId the device ID associated with the variable
     * @param name the variable name
     * @param value the variable value
     */
    public VariableUpdate(String pluginId, String deviceId, String name, Object value) {
        this(pluginId, deviceId, name, value, System.currentTimeMillis());
    }

    /**
     * Constructor for device variable update.
     *
     * @param pluginId the plugin ID associated with the variable
     * @param deviceId the device ID associated with the variable
     * @param name the variable name
     * @param value the variable value
     * @param updateTime the time the variable was updated
     */
    public VariableUpdate(String pluginId, String deviceId, String name, Object value, long updateTime) {
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.name = name;
        this.value = value;
        this.updateTime = updateTime;
    }

    /**
     * Returns whether this is a global variable update.
     *
     * @return a boolean
     */
    public boolean isGlobal() {
        return (deviceId == null);
    }

    /**
     * Returns the plugin ID that updated the variable.
     *
     * @return a plugin ID
     */
    public String getPluginId() {
        return pluginId;
    }

    /**
     * Returns the device ID that updated the variable.
     *
     * @return a device ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Returns the variable name that has changed.
     *
     * @return a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the new variable value.
     *
     * @return an Object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the time the variable update occurred.
     *
     * @return a long
     */
    public long getUpdateTime() {
        return updateTime;
    }

    public String toString() {
        return pluginId + "." + deviceId + "." + name + "=" + value;
    }
}
