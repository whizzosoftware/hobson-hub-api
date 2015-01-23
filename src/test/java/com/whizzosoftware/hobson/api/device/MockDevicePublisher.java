/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

import java.util.ArrayList;
import java.util.List;

public class MockDevicePublisher implements DevicePublisher {
    public final List<HobsonDevice> publishedDevices = new ArrayList<HobsonDevice>();

    @Override
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device) {
        publishDevice(plugin, device, false);
    }

    @Override
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device, boolean republish) {
        publishedDevices.add(device);
    }

    @Override
    public void unpublishDevice(HobsonPlugin plugin, String deviceId) {
        HobsonDevice unpublishDevice = null;
        for (HobsonDevice device : publishedDevices) {
            if (device.getPluginId().equals(plugin.getId()) && device.getId().equals(deviceId)) {
                unpublishDevice = device;
            }
        }
        if (unpublishDevice != null) {
            publishedDevices.remove(unpublishDevice);
        }
    }

    @Override
    public void unpublishAllDevices(HobsonPlugin plugin) {
        List<HobsonDevice> unpublishList = new ArrayList<HobsonDevice>();
        for (HobsonDevice device : publishedDevices) {
            if (device.getPluginId().equals(plugin.getId())) {
                unpublishList.add(device);
            }
        }
        for (HobsonDevice device : unpublishList) {
            publishedDevices.remove(device);
        }
    }

    public void addPublishedDevice(HobsonDevice device) {
        publishedDevices.add(device);
    }

    public List<HobsonDevice> getPublishedDevices() {
        return publishedDevices;
    }
}
