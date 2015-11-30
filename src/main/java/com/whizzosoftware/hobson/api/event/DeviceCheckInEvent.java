/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.device.DeviceContext;

/**
 * Event that occurs when Hobson successfully "checks in" with a device.
 *
 * @author Dan Noguerol
 */
public class DeviceCheckInEvent extends HobsonEvent {
    public static final String ID = "deviceCheckIn";

    private static final String PROP_DEVICE_CTX = "deviceCtx";
    private static final String PROP_CHECKIN_TIME = "checkInTime";

    public DeviceCheckInEvent(DeviceContext ctx, Long timestamp) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
        setProperty(PROP_DEVICE_CTX, ctx);
        setProperty(PROP_CHECKIN_TIME, timestamp);
    }

    public DeviceContext getDeviceContext() {
        return (DeviceContext)getProperty(PROP_DEVICE_CTX);
    }

    public Long getCheckInTime() {
        return (Long)getProperty(PROP_CHECKIN_TIME);
    }
}
