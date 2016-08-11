/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * An immutable implementation of HobsonVariable.
 *
 * @author Dan Noguerol
 */
public class ImmutableHobsonVariable implements HobsonVariable {
    protected DeviceVariableContext ctx;
    protected Mask mask;
    protected Object value;
    protected Long lastUpdate;
    protected VariableMediaType mediaType;

    public ImmutableHobsonVariable(DeviceVariableContext ctx, Mask mask, Object value, Long lastUpdate, VariableMediaType mediaType) {
        this.ctx = ctx;
        this.mask = mask;
        this.lastUpdate = lastUpdate;
        this.value = value;
        this.mediaType = mediaType;
    }

    @Override
    public DeviceVariableContext getContext() {
        return ctx;
    }

    @Override
    public String getDeviceId() {
        return ctx.hasDeviceContext() ? ctx.getDeviceContext().getDeviceId() : null;
    }

    @Override
    public Mask getMask() {
        return mask;
    }

    @Override
    public String getName() {
        return ctx.getName();
    }

    @Override
    public Long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean hasMediaType() {
        return (mediaType != null);
    }

    @Override
    public VariableMediaType getMediaType() {
        return mediaType;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
