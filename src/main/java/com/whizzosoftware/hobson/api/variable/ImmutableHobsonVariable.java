/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;

/**
 * An immutable implementation of HobsonVariable.
 *
 * @author Dan Noguerol
 */
public class ImmutableHobsonVariable implements HobsonVariable {
    protected String pluginId;
    protected String deviceId;
    protected String name;
    protected Mask mask;
    protected Long lastUpdate;
    protected Object value;
    protected VariableMediaType mediaType;

    public ImmutableHobsonVariable(DeviceContext dctx, String name, Mask mask, Object value, VariableMediaType mediaType, Long lastUpdate) {
        this(dctx.getPluginId(), dctx.getDeviceId(), name, mask, value, mediaType, lastUpdate);
    }

    public ImmutableHobsonVariable(String pluginId, String deviceId, String name, Mask mask, Object value, VariableMediaType mediaType, Long lastUpdate) {
        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.name = name;
        this.mask = mask;
        this.lastUpdate = lastUpdate;
        this.value = value;
        this.mediaType = mediaType;
    }

    public ImmutableHobsonVariable(String pluginId, String name, Mask mask, Object value, VariableMediaType mediaType, Long lastUpdate) {
        this(pluginId, null, name, mask, value, mediaType, lastUpdate);
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Mask getMask() {
        return mask;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String getPluginId() {
        return pluginId;
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

    @Override
    public boolean isGlobal() {
        return (deviceId == null);
    }
}
