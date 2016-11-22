/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the fully-qualified context of a variable (global or device-level).
 *
 * @author Dan Noguerol
 */
public class DeviceVariableContext {
    private DeviceContext ctx;
    private String name;

    public static DeviceVariableContext create(DeviceContext ctx, String name) {
        return new DeviceVariableContext(ctx, name);
    }

    public static DeviceVariableContext create(PluginContext ctx, String deviceId, String name) {
        return DeviceVariableContext.create(DeviceContext.create(ctx, deviceId), name);
    }

    public static DeviceVariableContext create(HubContext ctx, String pluginId, String deviceId, String name) {
        return DeviceVariableContext.create(DeviceContext.create(ctx, pluginId, deviceId), name);
    }

    public static DeviceVariableContext create(String variableId) {
        String[] comps = StringUtils.split(variableId, HubContext.DELIMITER);
        if (comps != null && comps.length > 3) {
            return DeviceVariableContext.create(DeviceContext.create(HubContext.create(comps[0]), comps[1], comps[2]), comps[3]);
        } else {
            return null;
        }
    }

    public static DeviceVariableContext createGlobal(HubContext ctx, String pluginId, String name) {
        return DeviceVariableContext.createGlobal(PluginContext.create(ctx, pluginId), name);
    }

    public static DeviceVariableContext createGlobal(PluginContext ctx, String name) {
        return DeviceVariableContext.create(DeviceContext.createGlobal(ctx), name);
    }

    public static DeviceVariableContext createLocal(String pluginId, String deviceId, String name) {
        return DeviceVariableContext.create(DeviceContext.createLocal(pluginId, deviceId), name);
    }

    protected DeviceVariableContext(DeviceContext ctx, String name) {
        this.ctx = ctx;
        this.name = name;
    }

    public boolean hasDeviceContext() {
        return (ctx != null);
    }

    public DeviceContext getDeviceContext() {
        return ctx;
    }

    public boolean hasDeviceId() {
        return (getDeviceId() != null);
    }

    public String getDeviceId() {
        return ctx != null ? ctx.getDeviceId() : null;
    }

    public HubContext getHubContext() {
        return ctx != null ? ctx.getHubContext() : null;
    }

    public String getHubId() {
        return ctx != null ? ctx.getHubId() : null;
    }

    public String getName() {
        return name;
    }

    public PluginContext getPluginContext() {
        return ctx != null ? ctx.getPluginContext() : null;
    }

    public String getPluginId() {
        return (ctx != null && ctx.getPluginContext() != null) ? ctx.getPluginContext().getPluginId() : null;
    }

    public boolean hasName() {
        return (name != null);
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getDeviceContext()).append(name).toHashCode();
    }

    public boolean equals(Object o) {
        return (
            o instanceof DeviceVariableContext &&
                ((DeviceVariableContext)o).getDeviceContext().equals(getDeviceContext()) &&
                ((DeviceVariableContext)o).name.equals(name)
        );
    }

    public String toString() {
        return getDeviceContext() + HubContext.DELIMITER + name;
    }
}
