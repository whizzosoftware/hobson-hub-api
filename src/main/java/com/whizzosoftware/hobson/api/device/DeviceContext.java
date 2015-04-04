/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

/**
 * A class that encapsulates the fully-qualified context of a device.
 *
 * @author Dan Noguerol
 */
public class DeviceContext {
    private PluginContext ctx;
    private String deviceId;

    /**
     * Creates a device context.
     *
     * @param ctx the plugin context associated with the device
     * @param deviceId the device ID
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext create(PluginContext ctx, String deviceId) {
        return new DeviceContext(ctx, deviceId);
    }

    /**
     * Creates a device context.
     *
     * @param hubContext the context of the hub that published the device
     * @param pluginId the ID of the plugin that published the device
     * @param deviceId the ID of the device
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext create(HubContext hubContext, String pluginId, String deviceId) {
        return create(PluginContext.create(hubContext, pluginId), deviceId);
    }

    /**
     * Creates a local device context.
     *
     * @param pluginId the ID of the plugin that published the device
     * @param deviceId the ID of the device
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext createLocal(String pluginId, String deviceId) {
        return create(PluginContext.createLocal(pluginId), deviceId);
    }

    private DeviceContext(PluginContext ctx, String deviceId) {
        this.ctx = ctx;
        this.deviceId = deviceId;
    }

    public HubContext getHubContext() {
        return ctx.getHubContext();
    }

    public PluginContext getPluginContext() {
        return ctx;
    }

    public String getUserId() {
        return ctx.getUserId();
    }

    public String getHubId() {
        return ctx.getHubId();
    }

    public String getPluginId() {
        return ctx.getPluginId();
    }

    public String getDeviceId() {
        return deviceId;
    }
}
