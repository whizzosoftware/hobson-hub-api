/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import org.osgi.service.event.Event;

import java.util.Map;

/**
 * An event used to request a change to a global or device variable.
 *
 * @author Dan Noguerol
 */
public class VariableUpdateRequestEvent extends HobsonEvent {
    public static final String ID = "setVariable";
    public static final String PROP_PLUGIN_ID = "pluginId";
    public static final String PROP_DEVICE_ID = "deviceId";
    public static final String PROP_NAME = "name";
    public static final String PROP_VALUE = "value";

    private String pluginId;
    private String deviceId;
    private String name;
    private Object value;

    public VariableUpdateRequestEvent(String pluginId, String name, Object value) {
        this(pluginId, null, name, value);
    }

    public VariableUpdateRequestEvent(String pluginId, String deviceId, String name, Object value) {
        super(EventTopics.VARIABLES_TOPIC, ID);

        this.pluginId = pluginId;
        this.deviceId = deviceId;
        this.name = name;
        this.value = value;
    }

    public VariableUpdateRequestEvent(Event event) {
        super(event);
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean isGlobal() {
        return (deviceId == null);
    }

    @Override
    void readProperties(Event event) {
        pluginId = (String)event.getProperty(PROP_PLUGIN_ID);
        deviceId = (String)event.getProperty(PROP_DEVICE_ID);
        name = (String)event.getProperty(PROP_NAME);
        value = event.getProperty(PROP_VALUE);
    }

    @Override
    void writeProperties(Map properties) {
        properties.put(PROP_PLUGIN_ID, pluginId);
        properties.put(PROP_DEVICE_ID, deviceId);
        properties.put(PROP_NAME, name);
        properties.put(PROP_VALUE, value);
    }
}
