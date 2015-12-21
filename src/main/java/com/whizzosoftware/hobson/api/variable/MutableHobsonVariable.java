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
 * Concrete implementation of HobsonVariable.
 *
 * @author Dan Noguerol
 */
public class MutableHobsonVariable extends ImmutableHobsonVariable {

    public MutableHobsonVariable(DeviceContext dctx, String name, Mask mask, Object value, VariableMediaType mediaType) {
        super(dctx, name, mask, value, mediaType, value != null ? System.currentTimeMillis() : null);
    }

    public MutableHobsonVariable(String pluginId, String deviceId, String name, Mask mask, Object value, VariableMediaType mediaType) {
        super(pluginId, deviceId, name, mask, value, mediaType, value != null ? System.currentTimeMillis() : null);
    }

    public MutableHobsonVariable(String pluginId, String name, Mask mask, Object value, VariableMediaType mediaType) {
        super(pluginId, name, mask, value, mediaType, null);
    }

    public void setValue(Object value) {
        this.value = value;
        setLastUpdate(System.currentTimeMillis());
    }

    public void setLastUpdate(Long time) {
        this.lastUpdate = time;
    }
}
