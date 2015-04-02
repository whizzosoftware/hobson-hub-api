/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.config.ConfigurationProperty;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.telemetry.TelemetryInterval;
import com.whizzosoftware.hobson.api.telemetry.TemporalValue;

import java.util.*;

public class MockDeviceManager implements DeviceManager {
    public final Map<String,Object> configuration = new HashMap<>();
    public final List<HobsonDevice> publishedDevices = new ArrayList<>();

    @Override
    public Collection<HobsonDevice> getAllDevices(String userId, String hubId) {
        return getPublishedDevices();
    }

    @Override
    public Collection<HobsonDevice> getAllPluginDevices(String userId, String hubId, String pluginId) {
        return null;
    }

    @Override
    public HobsonDevice getDevice(String userId, String hubId, String pluginId, String deviceId) {
        for (HobsonDevice device : getPublishedDevices()) {
            if (device.getPluginId().equals(pluginId) && device.getId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    @Override
    public boolean hasDevice(String userId, String hubId, String pluginId, String deviceId) {
        return (getDevice(userId, hubId, pluginId, deviceId) != null);
    }

    @Override
    public boolean isDeviceTelemetryEnabled(String userId, String hubId, String pluginId, String deviceId) {
        return false;
    }

    @Override
    public Configuration getDeviceConfiguration(String userId, String hubId, String pluginId, String deviceId) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name) {
        return null;
    }

    @Override
    public Collection<HobsonDevice> getAllTelemetryEnabledDevices(String userId, String hubId) {
        return null;
    }

    @Override
    public void publishDevice(String userId, String hubId, HobsonPlugin plugin, HobsonDevice device) {
        publishDevice(userId, hubId, plugin, device, false);
    }

    @Override
    public void publishDevice(String userId, String hubId, HobsonPlugin plugin, HobsonDevice device, boolean republish) {
        publishedDevices.add(device);
    }

    @Override
    public void unpublishDevice(String userId, String hubId, HobsonPlugin plugin, String deviceId) {
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
    public void unpublishAllDevices(String userId, String hubId, HobsonPlugin plugin) {
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
    public void setDeviceConfiguration(String userId, String hubId, String pluginId, String deviceId, Configuration config) {
        for (ConfigurationProperty prop : config.getProperties()) {
            setDeviceConfigurationProperty(userId, hubId, pluginId, deviceId, prop.getName(), prop.getValue(), true);
        }
    }

    @Override
    public void setDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name, Object value, boolean overwrite) {
        configuration.put(pluginId + "." + deviceId + "." + name, value);
    }

    @Override
    public void setDeviceName(String userId, String hubId, String pluginId, String deviceId, String name) {

    }

    public void addPublishedDevice(HobsonDevice device) {
        publishedDevices.add(device);
    }

    public List<HobsonDevice> getPublishedDevices() {
        return publishedDevices;
    }
}
