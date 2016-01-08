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
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the fully-qualified context of a variable (global or device-level).
 *
 * @author Dan Noguerol
 */
public class VariableContext {
    private DeviceContext ctx;
    private String name;

    public static VariableContext create(DeviceContext ctx, String name) {
        return new VariableContext(ctx, name);
    }

    public static VariableContext create(PluginContext ctx, String deviceId, String name) {
        return VariableContext.create(DeviceContext.create(ctx, deviceId), name);
    }

    public static VariableContext create(HubContext ctx, String pluginId, String deviceId, String name) {
        return VariableContext.create(DeviceContext.create(ctx, pluginId, deviceId), name);
    }

    public static VariableContext createGlobal(HubContext ctx, String pluginId, String name) {
        return VariableContext.createGlobal(PluginContext.create(ctx, pluginId), name);
    }

    public static VariableContext createGlobal(PluginContext ctx, String name) {
        return VariableContext.create(DeviceContext.createGlobal(ctx), name);
    }

    public static VariableContext createLocal(String pluginId, String deviceId, String name) {
        return VariableContext.create(DeviceContext.createLocal(pluginId, deviceId), name);
    }

    protected VariableContext(DeviceContext ctx, String name) {
        this.ctx = ctx;
        this.name = name;
    }

    public HubContext getHubContext() {
        return ctx.getHubContext();
    }

    public PluginContext getPluginContext() {
        return ctx.getPluginContext();
    }

    public DeviceContext getDeviceContext() {
        return ctx;
    }

    public String getUserId() {
        return ctx.getUserId();
    }

    public String getHubId() {
        return ctx.getHubId();
    }

    public boolean hasPluginId() {
        return (ctx.getPluginId() != null);
    }

    public String getPluginId() {
        return ctx.getPluginId();
    }

    public boolean hasDeviceId() {
        return (ctx.getDeviceId() != null);
    }

    public String getDeviceId() {
        return ctx.getDeviceId();
    }

    public boolean hasName() {
        return (name != null);
    }

    public String getName() {
        return name;
    }

    public boolean isGlobal() {
        return (ctx.isGlobal());
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getDeviceContext()).append(name).toHashCode();
    }

    public boolean equals(Object o) {
        return (
            o instanceof VariableContext &&
                ((VariableContext)o).getDeviceContext().equals(getDeviceContext()) &&
                ((VariableContext)o).name.equals(name)
        );
    }

    public String toString() {
        return getDeviceContext() + HubContext.DELIMITER + name;
    }
}
