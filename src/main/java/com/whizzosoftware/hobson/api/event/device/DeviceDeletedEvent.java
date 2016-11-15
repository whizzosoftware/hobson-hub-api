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

import java.util.Map;

/**
 * Event that occurs when a device is deleted.
 *
 * @author Dan Noguerol
 */
public class DeviceDeletedEvent extends DeviceEvent {
    public static final String ID = "deviceDeleted";
    public static final String PROP_DEVICE_CONTEXT = "deviceCtx";

    public DeviceDeletedEvent(long timestamp, DeviceContext deviceCtx) {
        super(timestamp, ID);
        setProperty(PROP_DEVICE_CONTEXT, deviceCtx);
    }

    public DeviceDeletedEvent(Map<String,Object> properties) {
        super(properties);
    }

    public DeviceContext getDeviceContext() {
        return (DeviceContext)getProperty(PROP_DEVICE_CONTEXT);
    }
}
