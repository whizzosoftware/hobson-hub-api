/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * A reference to a device variable.
 *
 * @author Dan Noguerol
 */
public class DeviceVariableRef {
    private String pluginId;
    private String deviceId;
    private String name;

    public DeviceVariableRef(String pluginId, String deviceId, String name) {
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.name = name;
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

    public String toString() {
        return pluginId + "." + deviceId + "." + name;
    }
}
