/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.util.UserUtil;

/**
 * Encapsulates contextual information for a device reference.
 *
 * @author Dan Noguerol
 */
public class DeviceContext {
    private String userId;
    private String hubId;
    private String pluginId;
    private String deviceId;

    /**
     * Creates a local device context.
     *
     * @param pluginId the plugin ID associated with the device
     * @param deviceId the device ID
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext createLocal(String pluginId, String deviceId) {
        return new DeviceContext(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, pluginId, deviceId);
    }

    public DeviceContext(String userId, String hubId, String pluginId, String deviceId) {
        this.userId = userId;
        this.hubId = hubId;
        this.pluginId = pluginId;
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHubId() {
        return hubId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
