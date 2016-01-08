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
    public Collection<String> getPublishedVariableNames(HubContext ctx) {
        return null;
    }

    @Override
    public Collection<HobsonVariable> getGlobalVariables(HubContext ctx) {
        return null;
    }

    @Override
    public Collection<HobsonVariable> getDeviceVariables(DeviceContext ctx) {
        return null;
    }

    @Override
    public HobsonVariable getVariable(VariableContext ctx) {
        return getPublishedVariables().get(ctx.getPluginId() + "." + ctx.getDeviceId());
    }

    @Override
    public boolean hasVariable(VariableContext ctx) {
        return false;
    }

    @Override
    public void publishVariable(VariableContext ctx, Object value, HobsonVariable.Mask mask) {
        publishVariable(ctx, value, mask, null);
    }

    @Override
    public void publishVariable(VariableContext ctx, Object value, HobsonVariable.Mask mask, VariableMediaType mediaType) {
        publishedVariables.put(ctx.getPluginId() + "." + ctx.getDeviceId(), new ImmutableHobsonVariable(ctx, mask, value, mediaType, 0L));
    }

    @Override
    public void unpublishVariable(VariableContext ctx) {

    }

    @Override
    public void unpublishAllVariables(DeviceContext ctx) {

    }

    @Override
    public void unpublishAllVariables(PluginContext ctx) {

    }

    @Override
    public Long setVariable(VariableContext ctx, Object value) {
        return null;
    }

    @Override
    public Map<String, Long> setDeviceVariables(DeviceContext ctx, Map<String, Object> values) {
        return null;
    }

    @Override
    public void fireVariableUpdateNotifications(HubContext ctx, List<VariableUpdate> updates) {

    }
}
