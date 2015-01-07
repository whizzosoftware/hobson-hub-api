/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import java.util.Dictionary;
import java.util.Map;

/**
 * Event that occurs when a device's configuration is updated.
 *
 * @author Dan Noguerol
 */
public class DeviceConfigurationUpdateEvent extends HobsonEvent {
    public static final String ID = "deviceConfigurationUpdate";

    private static final String PROP_PLUGIN_ID = "pluginId";
    private static final String PROP_DEVICE_ID = "deviceId";
    private static final String PROP_CONFIGURATION = "configuration";

    public DeviceConfigurationUpdateEvent(String pluginId, String deviceId, Dictionary configuration) {
        super(EventTopics.CONFIG_TOPIC, ID);
        setProperty(PROP_PLUGIN_ID, pluginId);
        setProperty(PROP_DEVICE_ID, deviceId);
        setProperty(PROP_CONFIGURATION, configuration);
    }

    public DeviceConfigurationUpdateEvent(Map<String,Object> props) {
        super(EventTopics.CONFIG_TOPIC, props);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public String getDeviceId() {
        return (String)getProperty(PROP_DEVICE_ID);
    }

    public Dictionary getConfiguration() {
        return (Dictionary)getProperty(PROP_CONFIGURATION);
    }
}
