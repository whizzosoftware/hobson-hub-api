/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;

/**
 * A class representing a variable update.
 *
 * @author Dan Noguerol
 */
public class VariableUpdate {
    private DeviceContext ctx;
    private String name;
    private Object value;
    private long timestamp;

    /**
     * Constructor.
     *
     * @param ctx the device context
     * @param name the variable name
     * @param value the variable value
     */
    public VariableUpdate(DeviceContext ctx, String name, Object value) {
        this(ctx, name, value, System.currentTimeMillis());
    }

    /**
     * Constructor.
     *
     * @param ctx the device context
     * @param name the variable name
     * @param value the variable value
     * @param timestamp the time the variable was updated
     */
    public VariableUpdate(DeviceContext ctx, String name, Object value, long timestamp) {
        this.ctx = ctx;
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }

    /**
     * Returns whether this is a global variable update.
     *
     * @return a boolean
     */
    public boolean isGlobal() {
        return ctx.isGlobal();
    }

    /**
     * Returns the device context associated with the update.
     *
     * @return a DeviceContext instance
     */
    public DeviceContext getDeviceContext() {
        return ctx;
    }

    /**
     * Returns the plugin ID that updated the variable.
     *
     * @return a plugin ID
     */
    public String getPluginId() {
        return ctx.getPluginId();
    }

    /**
     * Returns the device ID that updated the variable.
     *
     * @return a device ID
     */
    public String getDeviceId() {
        return ctx.getDeviceId();
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
    public long getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return ctx.toString() + "." + name + "=" + value;
    }
}
