/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.util.*;

public class MockDeviceManager implements DeviceManager {
    public final Map<String,Object> configuration = new HashMap<>();
    public final List<HobsonDeviceProxy> publishedDevices = new ArrayList<>();
    protected EventLoopGroup eventLoop = new LocalEventLoopGroup(1);

    @Override
    public void setDeviceName(DeviceContext dctx, String name) {

    }

    @Override
    public void updateDevice(HobsonDeviceDescriptor device) {

    }

    @Override
    public Collection<HobsonDeviceDescriptor> getDevices(HubContext ctx) {
        return getPublishedDevices();
    }

    @Override
    public Collection<HobsonDeviceDescriptor> getDevices(PluginContext ctx) {
        return null;
    }

    @Override
    public Collection<String> getDeviceVariableNames(HubContext hctx) {
        return null;
    }

    @Override
    public void deleteDevice(DeviceContext dctx) {

    }

    @Override
    public HobsonDeviceDescriptor getDevice(DeviceContext ctx) {
        for (HobsonDeviceDescriptor device : getPublishedDevices()) {
            if (device.getContext().getPluginId().equals(ctx.getPluginId()) && device.getContext().getDeviceId().equals(ctx.getDeviceId())) {
                return device;
            }
        }
        return null;
    }

    @Override
    public Future setDeviceVariable(DeviceVariableContext ctx, Object value) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public Future setDeviceVariables(Map<DeviceVariableContext, Object> values) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public boolean hasDevice(DeviceContext ctx) {
        return (getDevice(ctx) != null);
    }

    @Override
    public boolean isDeviceAvailable(DeviceContext ctx) {
        return false;
    }

    @Override
    public Future publishDevice(final HobsonDeviceProxy device, final Map<String, Object> config, final Runnable runnable) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                publishedDevices.add(device);
                if (config != null) {
                    setDeviceConfiguration(device.getContext(), device.getConfigurationClass(), config);
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    @Override
    public PropertyContainer getDeviceConfiguration(DeviceContext ctx) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name) {
        return null;
    }

    @Override
    public Long getDeviceLastCheckin(DeviceContext dctx) {
        return null;
    }

    @Override
    public DeviceVariableState getDeviceVariable(DeviceVariableContext ctx) {
        return null;
    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext dctx, PropertyContainerClass configClass, String name, Object value) {
        configuration.put(configClass.getContext().getPluginId() + "." + configClass.getContext().getDeviceId() + "." + name, value);
    }

    @Override
    public void setDeviceConfiguration(DeviceContext dctx, PropertyContainerClass configClass, Map<String, Object> values) {
        if (values != null) {
            for (String name : values.keySet()) {
                setDeviceConfigurationProperty(dctx, configClass, name, values.get(name));
            }
        }
    }

    @Override
    public void setDeviceTags(DeviceContext ctx, Set<String> tags) {

    }

    public List<HobsonDeviceDescriptor> getPublishedDevices() {
        List<HobsonDeviceDescriptor> results = new ArrayList<>();
        for (HobsonDeviceProxy d : publishedDevices) {
            results.add(d.getDescriptor());
        }
        return results;
    }
}
