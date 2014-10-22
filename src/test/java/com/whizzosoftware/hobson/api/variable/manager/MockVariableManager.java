/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable.manager;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.*;

public class MockVariableManager implements VariableManager {
    public final Map<String,HobsonVariable> publishedVariables = new HashMap<String,HobsonVariable>();
    public final List<VariableUpdate> firedUpdates = new ArrayList<VariableUpdate>();

    @Override
    public void publishGlobalVariable(String pluginId, HobsonVariable var) {

    }

    @Override
    public Collection<HobsonVariable> getGlobalVariables() {
        return null;
    }

    @Override
    public void unpublishGlobalVariable(String pluginId, String name) {

    }

    @Override
    public void publishDeviceVariable(String pluginId, String deviceId, HobsonVariable var) {
        publishedVariables.put(pluginId + "." + deviceId, var);
    }

    @Override
    public void unpublishDeviceVariable(String pluginId, String deviceId, String name) {

    }

    @Override
    public void unpublishAllDeviceVariables(String pluginId, String deviceId) {

    }

    @Override
    public void unpublishAllPluginVariables(String pluginId) {

    }

    @Override
    public Collection<HobsonVariable> getDeviceVariables(String pluginId, String deviceId) {
        return null;
    }

    @Override
    public HobsonVariable getDeviceVariable(String pluginId, String deviceId, String name) {
        return publishedVariables.get(pluginId + "." + deviceId);
    }

    @Override
    public boolean hasDeviceVariable(String pluginId, String deviceId, String name) {
        return false;
    }

    @Override
    public Long setDeviceVariable(String pluginId, String deviceId, String name, Object value) {
        return null;
    }

    @Override
    public void fireVariableUpdateNotification(HobsonPlugin plugin, VariableUpdate update) {
        firedUpdates.add(update);
    }

    @Override
    public void fireVariableUpdateNotifications(HobsonPlugin plugin, List<VariableUpdate> updates) {
        for (VariableUpdate u : updates) {
            firedUpdates.add(u);
        }
    }
}
