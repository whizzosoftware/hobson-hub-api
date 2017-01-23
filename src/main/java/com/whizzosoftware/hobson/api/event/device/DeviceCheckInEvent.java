/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
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
 * Event that occurs when Hobson successfully "checks in" with a device.
 *
 * @author Dan Noguerol
 */
public class DeviceCheckInEvent extends DeviceEvent {
    public static final String ID = "deviceCheckIn";

    private static final String PROP_CHECKIN_TIME = "checkInTime";

    public DeviceCheckInEvent(Long timestamp, DeviceContext ctx) {
        super(timestamp, ID, ctx);
        setProperty(PROP_CHECKIN_TIME, timestamp);
    }

    public DeviceCheckInEvent(Map<String, Object> props) {
        super(props);
    }

    public Long getCheckInTime() {
        return (Long)getProperty(PROP_CHECKIN_TIME);
    }
}
