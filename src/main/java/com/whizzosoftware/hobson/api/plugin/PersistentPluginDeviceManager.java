/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.device.DeviceManager;
import com.whizzosoftware.hobson.api.device.DevicePublisher;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.device.PersistentDevicePublisher;
import com.whizzosoftware.hobson.api.variable.telemetry.TelemetryInterval;
import com.whizzosoftware.hobson.api.variable.telemetry.TemporalValue;

import java.util.Collection;
import java.util.Map;

/**
 * A wrapper DeviceManager implementation that wrappers another DeviceManager and persists the devices that
 * it publishes by overriding its DevicePublisher implementation.
 *
 * @author Dan Noguerol
 */
public class PersistentPluginDeviceManager implements DeviceManager {
    private DeviceManager manager;
    private PersistentDevicePublisher publisher;

    public PersistentPluginDeviceManager(DeviceManager manager) {
        this.manager = manager;
        this.publisher = new PersistentDevicePublisher(manager.getPublisher());
    }

    /**
     * Publishes all devices that have been persisted.
     */
    public void publishAll() {
        publisher.publishAll();
    }

    @Override
    public void enableDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, boolean enabled) {
        manager.enableDeviceTelemetry(userId, hubId, pluginId, deviceId, enabled);
    }

    @Override
    public Collection<HobsonDevice> getAllDevices(String userId, String hubId) {
        return manager.getAllDevices(userId, hubId);
    }

    @Override
    public Collection<HobsonDevice> getAllPluginDevices(String userId, String hubId, String pluginId) {
        return manager.getAllPluginDevices(userId, hubId, pluginId);
    }

    @Override
    public Collection<HobsonDevice> getAllTelemetryEnabledDevices(String userId, String hubId) {
        return manager.getAllTelemetryEnabledDevices(userId, hubId);
    }

    @Override
    public HobsonDevice getDevice(String userId, String hubId, String pluginId, String deviceId) {
        return manager.getDevice(userId, hubId, pluginId, deviceId);
    }

    @Override
    public Configuration getDeviceConfiguration(String userId, String hubId, String pluginId, String deviceId) {
        return manager.getDeviceConfiguration(userId, hubId, pluginId, deviceId);
    }

    @Override
    public Object getDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name) {
        return manager.getDeviceConfigurationProperty(userId, hubId, pluginId, deviceId, name);
    }

    @Override
    public DevicePublisher getPublisher() {
        return publisher;
    }

    @Override
    public Map<String, Collection<TemporalValue>> getDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, long endTime, TelemetryInterval interval) {
        return manager.getDeviceTelemetry(userId, hubId, pluginId, deviceId, endTime, interval);
    }

    @Override
    public boolean hasDevice(String userId, String hubId, String pluginId, String deviceId) {
        return manager.hasDevice(userId, hubId, pluginId, deviceId);
    }

    @Override
    public boolean isDeviceTelemetryEnabled(String userId, String hubId, String pluginId, String deviceId) {
        return manager.isDeviceTelemetryEnabled(userId, hubId, pluginId, deviceId);
    }

    @Override
    public void setDeviceConfigurationProperty(String userId, String hubId, String pluginId, String deviceId, String name, Object value, boolean overwrite) {
        manager.setDeviceConfigurationProperty(userId, hubId, pluginId, deviceId, name, value, overwrite);
    }

    @Override
    public void setDeviceName(String userId, String hubId, String pluginId, String deviceId, String name) {
        manager.setDeviceName(userId, hubId, pluginId, deviceId, name);
    }

    @Override
    public void writeDeviceTelemetry(String userId, String hubId, String pluginId, String deviceId, Map<String, TemporalValue> values) {
        manager.writeDeviceTelemetry(userId, hubId, pluginId, deviceId, values);
    }
}
