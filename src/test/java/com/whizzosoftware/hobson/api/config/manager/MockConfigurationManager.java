/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config.manager;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class MockConfigurationManager implements ConfigurationManager {
    public Map<String,Object> deviceProperties = new HashMap<String,Object>();

    @Override
    public Dictionary getPluginConfiguration(String pluginId) {
        return null;
    }

    @Override
    public void setPluginConfigurationProperty(String pluginId, String name, Object value) {

    }

    @Override
    public Dictionary getDeviceConfiguration(String pluginId, String deviceId) {
        return null;
    }

    @Override
    public void setDeviceConfigurationProperty(String pluginId, String deviceId, String name, Object value, boolean overwrite) {
        deviceProperties.put(pluginId + "." + deviceId + "." + name, value);
    }

    @Override
    public void registerForPluginConfigurationUpdates(String pluginId, PluginConfigurationListener listener) {

    }

    @Override
    public void registerForDeviceConfigurationUpdates(String pluginId, String deviceId, DeviceConfigurationListener listener) {

    }

    @Override
    public void unregisterForConfigurationUpdates(String pluginId, PluginConfigurationListener listener) {

    }
}
