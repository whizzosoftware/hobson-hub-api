/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.event.device;

import com.whizzosoftware.hobson.api.device.DeviceContext;

import java.util.Map;

/**
 * Event that occurs when a the Hub determines an unavailable device has become available.
 *
 * @author Dan Noguerol
 */
public class DeviceAvailableEvent extends DeviceEvent {
    public static final String ID = "deviceAvailable";

    public DeviceAvailableEvent(long timestamp, DeviceContext dctx) {
        super(timestamp, ID, dctx);
    }

    public DeviceAvailableEvent(Map<String, Object> properties) {
        super(properties);
    }
}
