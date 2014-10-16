/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import java.util.Dictionary;

/**
 * Encapsulates information about a device configuration update.
 *
 * @author Dan Noguerol
 */
public class DeviceConfigurationUpdate {
    private String deviceId;
    private Dictionary configuration;

    public DeviceConfigurationUpdate(String deviceId, Dictionary configuration) {
        this.deviceId = deviceId;
        this.configuration = configuration;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Dictionary getConfiguration() {
        return configuration;
    }
}
