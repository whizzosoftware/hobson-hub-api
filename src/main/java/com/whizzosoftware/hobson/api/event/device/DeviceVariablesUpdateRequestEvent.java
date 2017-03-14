/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.event.device;

import com.whizzosoftware.hobson.api.device.DeviceContext;

import java.util.Collections;
import java.util.Map;

public class DeviceVariablesUpdateRequestEvent extends DeviceEvent {
    public static final String ID = "devVarsUpdateRequest";
    public static final String PROP_VALUES = "values";

    public DeviceVariablesUpdateRequestEvent(long timestamp, DeviceContext dctx, String name, Object value) {
        this(timestamp, dctx, Collections.singletonMap(name, value));
    }

    public DeviceVariablesUpdateRequestEvent(long timestamp, DeviceContext dctx, Map<String,Object> values) {
        super(timestamp, ID, dctx);
        setProperty(PROP_VALUES, values);
    }

    public DeviceVariablesUpdateRequestEvent(Map<String,Object> properties) {
        super(properties);
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getValues() {
        return (Map<String,Object>)getProperty(PROP_VALUES);
    }
}
