/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * A class representing a device variable update including both the old and new values.
 *
 * @author Dan Noguerol
 */
public class DeviceVariableUpdate {
    private DeviceVariableContext ctx;
    private Object oldValue;
    private Object newValue;
    private long timestamp;

    public DeviceVariableUpdate(DeviceVariableContext ctx, Object newValue) {
        this(ctx, null, newValue);
    }

    /**
     * Constructor.
     *
     * @param ctx the device context
     * @param oldValue the previous variable value
     * @param newValue the new variable value
     */
    public DeviceVariableUpdate(DeviceVariableContext ctx, Object oldValue, Object newValue) {
        this(ctx, oldValue, newValue, System.currentTimeMillis());
    }

    /**
     * Constructor.
     *
     * @param ctx the device context
     * @param oldValue the previous variable value
     * @param newValue the new variable value
     * @param timestamp the time the variable was updated
     */
    public DeviceVariableUpdate(DeviceVariableContext ctx, Object oldValue, Object newValue, long timestamp) {
        this.ctx = ctx;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.timestamp = timestamp;
    }

    /**
     * Returns the device context associated with the update.
     *
     * @return a DeviceContext instance
     */
    public DeviceVariableContext getContext() {
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
        return ctx.getName();
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
        return ctx.toString() + "=" + oldValue + " to " + newValue;
    }
}
