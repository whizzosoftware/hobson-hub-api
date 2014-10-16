/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.HobsonDevice;

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

    public String getPluginId() {
        return pluginId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
