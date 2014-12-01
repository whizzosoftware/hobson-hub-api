/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.device.HobsonDevice;

import java.util.Map;

/**
 * Event that occurs when a device is stopped.
 *
 * @author Dan Noguerol
 */
public class DeviceStoppedEvent extends HobsonEvent {
    public static final String ID = "deviceRemoved";
    public static final String PROP_DEVICE = "device";

    public DeviceStoppedEvent(HobsonDevice device) {
        super(EventTopics.DEVICES_TOPIC, ID);
        setProperty(PROP_DEVICE, device);
    }

    public DeviceStoppedEvent(Map<String,Object> properties) {
        super(EventTopics.DEVICES_TOPIC, properties);
    }

    public HobsonDevice getDevice() {
        return (HobsonDevice)getProperty(PROP_DEVICE);
    }

    public String getPluginId() {
        return getDevice().getPluginId();
    }
}
