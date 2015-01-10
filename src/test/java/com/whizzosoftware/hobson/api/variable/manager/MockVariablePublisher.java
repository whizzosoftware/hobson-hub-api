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
import com.whizzosoftware.hobson.api.variable.MockHobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariablePublisher;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.*;

public class MockVariablePublisher implements VariablePublisher {
    public final Map<String,HobsonVariable> publishedVariables = new HashMap<>();
    public final List<VariableUpdate> firedUpdates = new ArrayList<>();

    public Map<String,HobsonVariable> getPublishedVariables() {
        return publishedVariables;
    }

    public List<VariableUpdate> getFiredUpdates() {
        return firedUpdates;
    }

    @Override
    public void publishGlobalVariable(String userId, String hubId, String pluginId, String name, Object value, HobsonVariable.Mask mask) {

    }

    @Override
    public void unpublishGlobalVariable(String userId, String hubId, String pluginId, String name) {

    }

    @Override
    public void publishDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name, Object value, HobsonVariable.Mask mask) {
        publishedVariables.put(pluginId + "." + deviceId, new MockHobsonVariable(name, value, mask));
    }

    @Override
    public void unpublishDeviceVariable(String userId, String hubId, String pluginId, String deviceId, String name) {

    }

    @Override
    public void unpublishAllDeviceVariables(String userId, String hubId, String pluginId, String deviceId) {

    }

    @Override
    public void unpublishAllPluginVariables(String userId, String hubId, String pluginId) {

    }

    @Override
    public void fireVariableUpdateNotification(String userId, String hubId, HobsonPlugin plugin, VariableUpdate update) {
        firedUpdates.add(update);
    }

    @Override
    public void fireVariableUpdateNotifications(String userId, String hubId, HobsonPlugin plugin, List<VariableUpdate> updates) {
        for (VariableUpdate u : updates) {
            firedUpdates.add(u);
        }
    }
}
