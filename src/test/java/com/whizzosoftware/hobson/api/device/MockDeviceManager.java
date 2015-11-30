/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

import java.util.*;

public class MockDeviceManager implements DeviceManager {
    public final Map<String,Object> configuration = new HashMap<>();
    public final List<HobsonDevice> publishedDevices = new ArrayList<>();

    @Override
    public DeviceBootstrap createDeviceBootstrap(HubContext hubContext, String deviceId) {
        return null;
    }

    @Override
    public Collection<DeviceBootstrap> getDeviceBootstraps(HubContext hubContext) {
        return null;
    }

    @Override
    public DeviceBootstrap getDeviceBootstrap(HubContext hubContext, String id) {
        return null;
    }

    @Override
    public DeviceBootstrap registerDeviceBootstrap(HubContext hubContext, String deviceId) {
        return null;
    }

    @Override
    public void deleteDeviceBootstrap(HubContext hubContext, String id) {

    }

    @Override
    public boolean verifyDeviceBootstrap(HubContext hubContext, String id, String secret) {
        return false;
    }

    @Override
    public void resetDeviceBootstrap(HubContext hubContext, String id) {

    }

    @Override
    public Collection<HobsonDevice> getAllDevices(HubContext ctx) {
        return getPublishedDevices();
    }

    @Override
    public Collection<HobsonDevice> getAllDevices(PluginContext ctx) {
        return null;
    }

    @Override
    public HobsonDevice getDevice(DeviceContext ctx) {
        for (HobsonDevice device : getPublishedDevices()) {
            if (device.getContext().getPluginId().equals(ctx.getPluginId()) && device.getContext().getDeviceId().equals(ctx.getDeviceId())) {
                return device;
            }
        }
        return null;
    }

    @Override
    public boolean hasDevice(DeviceContext ctx) {
        return (getDevice(ctx) != null);
    }

    @Override
    public PropertyContainer getDeviceConfiguration(DeviceContext ctx) {
        return null;
    }

    @Override
    public PropertyContainerClass getDeviceConfigurationClass(DeviceContext ctx) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name) {
        return null;
    }

    @Override
    public Long getDeviceLastCheckIn(DeviceContext ctx) {
        return null;
    }

    @Override
    public void publishDevice(HobsonDevice device) {
        publishDevice(device, false);
    }

    @Override
    public void publishDevice(HobsonDevice device, boolean republish) {
        publishedDevices.add(device);
    }

    @Override
    public void checkInDevice(DeviceContext ctx, Long checkInTime) {

    }

    @Override
    public void unpublishDevice(DeviceContext ctx, EventLoopExecutor executor) {
        HobsonDevice unpublishDevice = null;
        for (HobsonDevice device : publishedDevices) {
            if (device.getContext().getPluginId().equals(ctx.getPluginId()) && device.getContext().getDeviceId().equals(ctx.getDeviceId())) {
                unpublishDevice = device;
            }
        }
        if (unpublishDevice != null) {
            publishedDevices.remove(unpublishDevice);
        }
    }


    @Override
    public void unpublishAllDevices(PluginContext ctx, EventLoopExecutor executor) {
        List<HobsonDevice> unpublishList = new ArrayList<HobsonDevice>();
        for (HobsonDevice device : publishedDevices) {
            if (device.getContext().getPluginId().equals(ctx.getPluginId())) {
                unpublishList.add(device);
            }
        }
        for (HobsonDevice device : unpublishList) {
            publishedDevices.remove(device);
        }
    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite) {
        configuration.put(ctx.getPluginId() + "." + ctx.getDeviceId() + "." + name, value);
    }

    @Override
    public void setDeviceConfigurationProperties(DeviceContext ctx, Map<String, Object> values, boolean overwrite) {
        for (String name : values.keySet()) {
            setDeviceConfigurationProperty(ctx, name, values.get(name), overwrite);
        }
    }

    public void addPublishedDevice(HobsonDevice device) {
        publishedDevices.add(device);
    }

    public List<HobsonDevice> getPublishedDevices() {
        return publishedDevices;
    }
}
