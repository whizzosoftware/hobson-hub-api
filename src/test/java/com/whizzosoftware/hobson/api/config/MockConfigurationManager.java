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
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;

public class MockConfigurationManager implements ConfigurationManager {
    private Map<PluginContext,PropertyContainer> pluginConfigMap = new HashMap<>();

    @Override
    public void deleteHubConfiguration(HubContext ctx) {

    }

    @Override
    public PropertyContainer getDeviceConfiguration(DeviceContext ctx, PropertyContainerClass metas) {
        return null;
    }

    @Override
    public Object getDeviceConfigurationProperty(DeviceContext ctx, String name) {
        return null;
    }

    @Override
    public PropertyContainer getHubConfiguration(HubContext ctx) {
        return null;
    }

    @Override
    public Object getHubConfigurationProperty(HubContext ctx, String name) {
        return null;
    }

    @Override
    public PropertyContainer getLocalPluginConfiguration(PluginContext ctx, PropertyContainerClass configurationClass) {
        return pluginConfigMap.get(ctx);
    }

    @Override
    public void setDeviceConfigurationProperties(DeviceContext ctx, Map<String, Object> values) throws NotSerializableException {

    }

    @Override
    public void setDeviceConfigurationProperty(DeviceContext ctx, String name, Object value) throws NotSerializableException {

    }

    @Override
    public void setHubConfiguration(HubContext ctx, PropertyContainer configuration) throws NotSerializableException {

    }

    @Override
    public void setLocalPluginConfiguration(PluginContext ctx, PropertyContainer newConfig) throws NotSerializableException {
        pluginConfigMap.put(ctx, newConfig);
    }

    @Override
    public void setLocalPluginConfigurationProperty(PluginContext ctx, String name, Object value) throws NotSerializableException {

    }
}
