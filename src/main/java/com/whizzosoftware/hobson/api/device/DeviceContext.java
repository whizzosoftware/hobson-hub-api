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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * Creates a device context.
     *
     * @param s a String generated from DeviceContext's toString() method
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext create(String s) {
        String[] comps = StringUtils.split(s, HubContext.DELIMITER);
        return DeviceContext.create(HubContext.create(comps[0], comps[1]), comps[2], comps[3]);
    }

    /**
     * Creates a collection of device contexts.
     *
     * @param s a comma-separated list of Strings generated from DeviceContext's toString() method
     *
     * @return a Collection of DeviceContext instances
     */
    public static Collection<DeviceContext> createCollection(String s) {
        List<DeviceContext> results = new ArrayList<>();
        for (String ctx : StringUtils.split(s, ',')) {
            String[] comps = StringUtils.split(ctx, HubContext.DELIMITER);
            results.add(DeviceContext.create(HubContext.create(comps[0], comps[1]), comps[2], comps[3]));
        }
        return results;
    }

    /**
     * Creates a global device context.
     *
     * @param hubContext the context of the hub
     * @param pluginId the plugin associated with the global device
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext createGlobal(HubContext hubContext, String pluginId) {
        return create(PluginContext.create(hubContext, pluginId), null);
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

    /**
     * Creates a local global device context.
     *
     * @param pluginId the plugin associated with the global device
     *
     * @return a DeviceContext instance
     */
    public static DeviceContext createLocalGlobal(String pluginId) {
        return create(PluginContext.createLocal(pluginId), null);
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

    public boolean hasDeviceId() {
        return (deviceId != null);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isGlobal() {
        return (deviceId == null);
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getPluginContext()).append(deviceId).toHashCode();
    }

    public boolean equals(Object o) {
        return (
            o instanceof DeviceContext &&
                ((DeviceContext)o).getPluginContext().equals(getPluginContext()) &&
                ((DeviceContext)o).deviceId.equals(deviceId)
        );
    }

    public String toString() {
        return getPluginContext() + HubContext.DELIMITER + deviceId;
    }
}
