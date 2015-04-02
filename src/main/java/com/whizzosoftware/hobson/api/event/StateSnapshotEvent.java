/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.hub.HobsonHub;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;

import java.util.*;

/**
 * An event that encapsulates the current state of a hub.
 *
 * @author Dan Noguerol
 */
public class StateSnapshotEvent extends HobsonEvent {
    public final static String ID = "stateSnapshot";

    private HobsonHub hub;
    private Collection<HobsonPlugin> plugins;
    private Map<String,List<HobsonDevice>> deviceMap = new HashMap<>();
    private Map<String,List<HobsonVariable>> variableMap = new HashMap<>();

    public StateSnapshotEvent() {
        super(EventTopics.STATE_TOPIC, ID);
    }

    public boolean hasHub() {
        return (hub != null);
    }

    public HobsonHub getHub() {
        return hub;
    }

    public void setHub(HobsonHub hubDetails) {
        this.hub = hubDetails;
    }

    public boolean hasPlugins() {
        return (plugins != null);
    }

    public Collection<HobsonPlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(Collection<HobsonPlugin> plugins) {
        this.plugins = plugins;
    }

    public void addPlugin(HobsonPlugin plugin) {
        if (plugins == null) {
            plugins = new ArrayList<>();
        }
        plugins.add(plugin);
    }

    public boolean hasDevices(String pluginId) {
        return (deviceMap.containsKey(pluginId));
    }

    public Collection<HobsonDevice> getDevices(String pluginId) {
        return deviceMap.get(pluginId);
    }

    public void addDevice(HobsonDevice device) {
        List<HobsonDevice> devices = deviceMap.get(device.getPluginId());
        if (devices == null) {
            devices = new ArrayList<>();
            deviceMap.put(device.getPluginId(), devices);
        }
        devices.add(device);
    }

    public boolean hasVariables(String pluginId, String deviceId) {
        return (variableMap.containsKey(pluginId + ":" + deviceId));
    }

    public Collection<HobsonVariable> getVariables(String pluginId, String deviceId) {
        return variableMap.get(pluginId + ":" + deviceId);
    }

    public void addVariable(HobsonVariable variable) {
        String key = variable.getPluginId() + ":" + variable.getDeviceId();
        List<HobsonVariable> variables = variableMap.get(key);
        if (variables == null) {
            variables = new ArrayList<>();
            variableMap.put(key, variables);
        }
        variables.add(variable);
    }
}
