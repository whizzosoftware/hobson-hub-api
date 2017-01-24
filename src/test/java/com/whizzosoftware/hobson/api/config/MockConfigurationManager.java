/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.config;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

public class MockConfigurationManager implements ConfigurationManager {
    private Map<PluginContext,Map<String,Object>> pluginConfigMap = new HashMap<>();

    @Override
    public void deleteHubConfiguration(HubContext ctx) {

    }

    @Override
    public Map<String,Object> getDeviceConfiguration(DeviceContext ctx) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name) {
        return null;
    }

    @Override
    public Map<String,Object> getHubConfiguration(HubContext ctx) {
        return null;
    }

    @Override
    public Object getHubConfigurationProperty(HubContext ctx, String name) {
        return null;
    }

    @Override
    public Map<String,Object> getLocalPluginConfiguration(PluginContext ctx) {
        return pluginConfigMap.get(ctx);
    }

    @Override
    public void setDeviceConfigurationProperties(DeviceContext ctx, Map<String, Object> values) throws NotSerializableException {

    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value) throws NotSerializableException {

    }

    @Override
    public void setHubConfiguration(HubContext ctx, Map<String,Object> config) throws NotSerializableException {

    }

    @Override
    public void setLocalPluginConfiguration(PluginContext ctx, Map<String,Object> config) throws NotSerializableException {
        pluginConfigMap.put(ctx, config);
    }

    @Override
    public void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value) throws NotSerializableException {

    }
}
