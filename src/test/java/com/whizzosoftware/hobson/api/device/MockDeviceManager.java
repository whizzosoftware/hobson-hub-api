/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.variable.telemetry.TelemetryInterval;
import com.whizzosoftware.hobson.api.variable.telemetry.TemporalValue;

import java.util.*;

public class MockDeviceManager implements DeviceManager {
    public final MockDevicePublisher publisher;
    public final Map<String,Object> configuration = new HashMap<String,Object>();

    public MockDeviceManager() {
        this(new MockDevicePublisher());
    }

    public MockDeviceManager(MockDevicePublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public Collection<HobsonDevice> getAllDevices(String userId, String hubId) {
        return publisher.getPublishedDevices();
    }

    @Override
    public Collection<HobsonDevice> getAllPluginDevices(String userId, String hubId, String pluginId) {
        return null;
    }

    @Override
    public HobsonDevice getDevice(String userId, String hubId, String pluginId, String deviceId) {
        for (HobsonDevice device : publisher.getPublishedDevices()) {
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
    public Configuration getDeviceConfiguration(String userId, String hubId, String pluginId, String deviceId) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name) {
        return null;
    }

    @Override
    public DevicePublisher getPublisher() {
        return publisher;
    }

    @Override
    public void setDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name, Object value, boolean overwrite) {
        configuration.put(pluginId + "." + deviceId + "." + name, value);
    }

    @Override
    public void setDeviceName(String userId, String hubId, String pluginId, String deviceId, String name) {

    }

    @Override
    public Collection<HobsonDevice> getAllTelemetryEnabledDevices(String userId, String hubId) {
        return null;
    }

    @Override
    public boolean isDeviceTelemetryEnabled(String userId, String hubId, String pluginId, String deviceId) {
        return false;
    }

    @Override
    public void enableDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, boolean enabled) {

    }

    @Override
    public Map<String,Collection<TemporalValue>> getDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, long endTime, TelemetryInterval interval) {
        return null;
    }

    @Override
    public void writeDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, Map<String, TemporalValue> values) {

    }
}
