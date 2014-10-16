/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.device.HobsonDevice;
import org.osgi.service.event.Event;

import java.util.Map;

/**
 * Event that occurs when a device is started.
 *
 * @author Dan Noguerol
 */
public class DeviceStartedEvent extends HobsonEvent {
    public static final String ID = "deviceAdded";
    public static final String PROP_DEVICE = "device";

    private HobsonDevice device;

    public DeviceStartedEvent(HobsonDevice device) {
        super(EventTopics.DEVICES_TOPIC, ID);

        this.device = device;
    }

    public DeviceStartedEvent(Event event) {
        super(event);
    }

    public HobsonDevice getDevice() {
        return device;
    }

    public String getPluginId() {
        return device.getPluginId();
    }

    @Override
    void readProperties(Event event) {
        device = (HobsonDevice)event.getProperty(PROP_DEVICE);
    }

    @Override
    void writeProperties(Map properties) {
        properties.put(PROP_DEVICE, device);
    }
}
