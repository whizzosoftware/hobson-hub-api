/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.device.proxy.DeviceProxy;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariable;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.util.*;

public class MockDeviceManager implements DeviceManager {
    public final Map<String,Object> configuration = new HashMap<>();
    public final List<DeviceDescription> publishedDevices = new ArrayList<>();
    protected EventLoopGroup eventLoop = new LocalEventLoopGroup(1);

    @Override
    public DevicePassport createDevicePassport(HubContext hubContext, String deviceId) {
        return null;
    }

    @Override
    public Collection<DevicePassport> getDevicePassports(HubContext hubContext) {
        return null;
    }

    @Override
    public DevicePassport getDevicePassport(HubContext hubContext, String id) {
        return null;
    }

    @Override
    public DevicePassport activateDevicePassport(HubContext hubContext, String deviceId) {
        return null;
    }

    @Override
    public void setDeviceName(DeviceContext dctx, String name) {

    }

    @Override
    public Future sendDeviceHint(DeviceDescription device, PropertyContainer config) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void deleteDevicePassport(HubContext hubContext, String id) {

    }

    @Override
    public boolean verifyDevicePassport(HubContext hubContext, String id, String secret) {
        return false;
    }

    @Override
    public void resetDevicePassport(HubContext hubContext, String id) {

    }

    @Override
    public void setDeviceAvailability(DeviceContext ctx, boolean available, Long checkInTime) {

    }

    @Override
    public Collection<DeviceDescription> getAllDeviceDescriptions(HubContext ctx) {
        return getPublishedDevices();
    }

    @Override
    public Collection<DeviceDescription> getAllDeviceDescriptions(PluginContext ctx) {
        return null;
    }

    @Override
    public Collection<String> getDeviceVariableNames(HubContext hctx) {
        return null;
    }

    @Override
    public DeviceDescription getDeviceDescription(DeviceContext ctx) {
        for (DeviceDescription device : getPublishedDevices()) {
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
    public boolean hasDeviceVariableValue(DeviceVariableContext vctx) {
        return false;
    }

    @Override
    public DeviceVariable getDeviceVariable(DeviceVariableContext ctx) {
        return null;
    }

    @Override
    public Collection<DeviceVariable> getDeviceVariables(DeviceContext ctx) {
        return null;
    }

    @Override
    public Collection<DeviceType> getPluginDeviceTypes(PluginContext pctx) {
        return null;
    }

    @Override
    public boolean hasDevice(DeviceContext ctx) {
        return (getDeviceDescription(ctx) != null);
    }

    @Override
    public PropertyContainer getDeviceConfiguration(DeviceContext ctx) {
        return null;
    }

    @Override
    public PropertyContainerClass getDeviceTypeConfigurationClass(PluginContext ctx, DeviceType type) {
        return null;
    }

    @Override
    public PropertyContainerClass getDeviceConfigurationClass(DeviceContext dctx) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name) {
        return null;
    }

    @Override
    public boolean isDeviceAvailable(DeviceContext ctx) {
        return false;
    }

    @Override
    public void publishDeviceType(PluginContext pctx, DeviceType type, TypedProperty[] configProperties) {

    }

    @Override
    public Future publishDevice(final PluginContext pctx, final DeviceProxy proxy, final Map<String,Object> config) {
        return eventLoop.submit(new Runnable() {
            @Override
            public void run() {
                publishedDevices.add(createDeviceDescription(pctx, proxy));
                if (config != null) {
                    setDeviceConfigurationProperties(DeviceContext.create(pctx, proxy.getDeviceId()), config, true);
                }
            }
        });
    }

    @Override
    public Long getDeviceLastCheckIn(DeviceContext ctx) {
        return null;
    }

    @Override
    public Set<String> getDeviceTags(DeviceContext ctx) {
        return null;
    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value, boolean overwrite) {
        configuration.put(ctx.getPluginId() + "." + ctx.getDeviceId() + "." + name, value);
    }

    @Override
    public void setDeviceConfigurationProperties(DeviceContext ctx, Map<String, Object> values, boolean overwrite) {
        if (values != null) {
            for (String name : values.keySet()) {
                setDeviceConfigurationProperty(ctx, name, values.get(name), overwrite);
            }
        }
    }

    @Override
    public void setDeviceTags(DeviceContext ctx, Set<String> tags) {

    }

    public void addPublishedDevice(DeviceDescription device) {
        publishedDevices.add(device);
    }

    public List<DeviceDescription> getPublishedDevices() {
        return publishedDevices;
    }

    protected DeviceDescription createDeviceDescription(PluginContext pctx, DeviceProxy proxy) {
        return new DeviceDescription.Builder(DeviceContext.create(pctx, proxy.getDeviceId())).build();
    }
}
