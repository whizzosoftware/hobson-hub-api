/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.manager;

import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockDeviceManager implements DeviceManager {
    public final List<HobsonDevice> publishedDevices = new ArrayList<HobsonDevice>();

    @Override
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device) {
        publishedDevices.add(device);
    }

    @Override
    public Collection<HobsonDevice> getAllDevices() {
        return publishedDevices;
    }

    @Override
    public Collection<HobsonDevice> getAllPluginDevices(String pluginId) {
        return null;
    }

    @Override
    public HobsonDevice getDevice(String pluginId, String deviceId) {
        for (HobsonDevice device : publishedDevices) {
            if (device.getPluginId().equals(pluginId) && device.getId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    @Override
    public boolean hasDevice(String pluginId, String deviceId) {
        return (getDevice(pluginId, deviceId) != null);
    }

    @Override
    public void setDeviceName(String pluginId, String deviceId, String name) {

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
}
