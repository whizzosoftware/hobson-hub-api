/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.event.device;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.event.HobsonEvent;

import java.util.Map;

/**
 * A base class for all device events.
 *
 * @author Dan Noguerol
 */
abstract public class DeviceEvent extends HobsonEvent {
    public static final String PROP_DEVICE_CONTEXT = "deviceCtx";

    DeviceEvent(Long timestamp, String eventId, DeviceContext dctx) {
        super(timestamp, eventId);
        setProperty(PROP_DEVICE_CONTEXT, dctx);
    }

    DeviceEvent(Map<String, Object> properties) {
        super(properties);
    }

    public DeviceContext getDeviceContext() {
        return (DeviceContext)getProperty(PROP_DEVICE_CONTEXT);
    }
}
