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
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.*;

public class MockDeviceManager implements DeviceManager {
    public final Map<String,Object> configuration = new HashMap<>();
    public final List<HobsonDevice> publishedDevices = new ArrayList<>();

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
    public boolean isDeviceTelemetryEnabled(DeviceContext ctx) {
        return false;
    }

    @Override
    public Configuration getDeviceConfiguration(DeviceContext ctx) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name) {
        return null;
    }

    @Override
    public Collection<HobsonDevice> getAllTelemetryEnabledDevices(HubContext ctx) {
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
    public void setDeviceConfiguration(DeviceContext ctx, Configuration config) {
        for (ConfigurationProperty prop : config.getProperties()) {
            setDeviceConfigurationProperty(ctx, prop.getName(), prop.getValue(), true);
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

    @Override
    public void setDeviceName(DeviceContext ctx, String name) {

    }

    public void addPublishedDevice(HobsonDevice device) {
        publishedDevices.add(device);
    }

    public List<HobsonDevice> getPublishedDevices() {
        return publishedDevices;
    }
}
