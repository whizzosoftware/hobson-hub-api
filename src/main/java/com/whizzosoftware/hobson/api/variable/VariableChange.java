/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;

/**
 * A class representing a variable change - both the old value and new value.
 *
 * @author Dan Noguerol
 */
public class VariableChange {
    private DeviceContext ctx;
    private String name;
    private Object oldValue;
    private Object newValue;
    private long timestamp;

    /**
     * Constructor.
     *
     * @param ctx the device context
     * @param name the variable name
     * @param newValue the new variable value
     */
    public VariableChange(DeviceContext ctx, String name, Object oldValue, Object newValue) {
        this(ctx, name, oldValue, newValue, System.currentTimeMillis());
    }

    /**
     * Constructor.
     *
     * @param ctx the device context
     * @param name the variable name
     * @param newValue the new variable value
     * @param timestamp the time the variable was updated
     */
    public VariableChange(DeviceContext ctx, String name, Object oldValue, Object newValue, long timestamp) {
        this.ctx = ctx;
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
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
     * Returns the previous variable value.
     *
     * @return an Object
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Returns the new variable value.
     *
     * @return an Object
     */
    public Object getNewValue() {
        return newValue;
    }

    /**
     * Returns the time the variable update occurred.
     *
     * @return a long
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Indicates whether this change has an old value.
     *
     * @return a boolean
     */
    public boolean hasOldValue() {
        return (oldValue != null);
    }

    /**
     * Indicates whether this change has a new value.
     *
     * @return a boolean
     */
    public boolean hasNewValue() {
        return (newValue != null);
    }

    /**
     * Indicates if this is the first update for this variable (i.e. the first non-null value).
     *
     * @return a boolean
     */
    public boolean isInitial() {
        return (oldValue == null);
    }

    /**
     * Indicates if there is a difference between old and new values.
     *
     * @return a boolean
     */
    public boolean isChanged() {
        return (newValue != null && !newValue.equals(oldValue)) || (oldValue != null && !oldValue.equals(newValue));
    }

    public String toString() {
        return ctx.toString() + "." + name + "=" + oldValue + " to " + newValue;
    }
}
