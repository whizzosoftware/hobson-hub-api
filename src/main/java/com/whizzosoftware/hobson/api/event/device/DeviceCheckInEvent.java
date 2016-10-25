/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
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

    private static final String PROP_PLUGIN_ID = "pluginId";
    private static final String PROP_DEVICE_ID = "deviceId";
    private static final String PROP_CHECKIN_TIME = "checkInTime";

    public DeviceCheckInEvent(DeviceContext ctx, Long timestamp) {
        super(timestamp, ID);
        setProperty(PROP_PLUGIN_ID, ctx.getPluginId());
        setProperty(PROP_DEVICE_ID, ctx.getDeviceId());
        setProperty(PROP_CHECKIN_TIME, timestamp);
    }

    public DeviceCheckInEvent(Map<String, Object> props) {
        super(props);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public String getDeviceId() {
        return (String)getProperty(PROP_DEVICE_ID);
    }

    public Long getCheckInTime() {
        return (Long)getProperty(PROP_CHECKIN_TIME);
    }
}
