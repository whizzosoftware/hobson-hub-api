/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.property.PropertyContainer;

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
    private static final String PROP_CONFIGURATION_CLASS_ID = "configurationClassId";
    private static final String PROP_CONFIGURATION = "configuration";

    public DeviceConfigurationUpdateEvent(long timestamp, String pluginId, String deviceId, PropertyContainer configuration) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
        setProperty(PROP_PLUGIN_ID, pluginId);
        setProperty(PROP_DEVICE_ID, deviceId);
        setProperty(PROP_CONFIGURATION_CLASS_ID, configuration.getContainerClassContext().getContainerClassId());
        setProperty(PROP_CONFIGURATION, configuration);
    }

    public DeviceConfigurationUpdateEvent(Map<String,Object> props) {
        super(EventTopics.STATE_TOPIC, props);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public String getDeviceId() {
        return (String)getProperty(PROP_DEVICE_ID);
    }

    public String getConfigurationClassId() {
        return (String)getProperty(PROP_CONFIGURATION_CLASS_ID);
    }

    public PropertyContainer getConfiguration() {
        return (PropertyContainer)getProperty(PROP_CONFIGURATION);
    }
}
