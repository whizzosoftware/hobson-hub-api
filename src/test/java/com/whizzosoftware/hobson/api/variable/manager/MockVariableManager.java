/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable.manager;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.variable.*;

import java.util.*;

public class MockVariableManager implements VariableManager {
    public final Map<String,HobsonVariable> publishedVariables = new HashMap<>();
    public final List<VariableUpdate> firedUpdates = new ArrayList<>();

    public Map<String,HobsonVariable> getPublishedVariables() {
        return publishedVariables;
    }

    public List<VariableUpdate> getFiredUpdates() {
        return firedUpdates;
    }

    @Override
    public Collection<HobsonVariable> getAllVariables(HubContext ctx) {
        return null;
    }

    @Override
    public Collection<HobsonVariable> getAllVariables(HubContext ctx, VariableProxyValueProvider proxyProvider) {
        return null;
    }

    @Override
    public Collection<HobsonVariable> getGlobalVariables(HubContext ctx) {
        return null;
    }

    @Override
    public Collection<HobsonVariable> getGlobalVariables(HubContext ctx, VariableProxyValueProvider proxyProvider) {
        return null;
    }

    @Override
    public HobsonVariable getGlobalVariable(HubContext ctx, String name) {
        return null;
    }

    @Override
    public HobsonVariable getGlobalVariable(HubContext ctx, String name, VariableProxyValueProvider proxyProvider) {
        return null;
    }

    @Override
    public HobsonVariableCollection getDeviceVariables(DeviceContext ctx) {
        return null;
    }

    @Override
    public HobsonVariableCollection getDeviceVariables(DeviceContext ctx, VariableProxyValueProvider proxyProvider) {
        return null;
    }

    @Override
    public Collection<String> getDeviceVariableChangeIds(DeviceContext ctx) {
        return null;
    }

    @Override
    public HobsonVariable getDeviceVariable(DeviceContext ctx, String name) {
        return getPublishedVariables().get(ctx.getPluginId() + "." + ctx.getDeviceId());
    }

    @Override
    public HobsonVariable getDeviceVariable(DeviceContext ctx, String name, VariableProxyValueProvider proxyValueProvider) {
        return null;
    }

    @Override
    public boolean hasDeviceVariable(DeviceContext ctx, String name) {
        return false;
    }

    @Override
    public void publishGlobalVariable(PluginContext ctx, String name, Object value, HobsonVariable.Mask mask) {
        publishGlobalVariable(ctx, name, value, null);
    }

    @Override
    public void publishGlobalVariable(PluginContext ctx, String name, Object value, HobsonVariable.Mask mask, String proxyType) {

    }

    @Override
    public void unpublishGlobalVariable(PluginContext ctx, String name) {

    }

    @Override
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask) {
        publishDeviceVariable(ctx, name, value, mask, null);
    }

    @Override
    public void publishDeviceVariable(DeviceContext ctx, String name, Object value, HobsonVariable.Mask mask, String proxyType) {
        publishedVariables.put(ctx.getPluginId() + "." + ctx.getDeviceId(), new MockHobsonVariable(ctx.getPluginId(), ctx.getDeviceId(), name, value, mask));
    }

    @Override
    public void unpublishDeviceVariable(DeviceContext ctx, String name) {

    }

    @Override
    public void unpublishAllDeviceVariables(DeviceContext ctx) {

    }

    @Override
    public void unpublishAllPluginVariables(PluginContext ctx) {

    }

    @Override
    public Long setGlobalVariable(PluginContext ctx, String name, Object value) {
        return null;
    }

    @Override
    public Map<String, Long> setGlobalVariables(PluginContext ctx, Map<String, Object> values) {
        return null;
    }

    @Override
    public Long setDeviceVariable(DeviceContext ctx, String name, Object value) {
        return null;
    }

    @Override
    public Map<String, Long> setDeviceVariables(DeviceContext ctx, Map<String, Object> values) {
        return null;
    }

    @Override
    public void applyVariableUpdates(HubContext ctx, List<VariableUpdate> updates) {

    }
}
