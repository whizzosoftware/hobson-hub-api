/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

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

    public VariableUpdateRequestEvent(String pluginId, String name, Object value) {
        this(pluginId, null, name, value);
    }

    public VariableUpdateRequestEvent(String pluginId, String deviceId, String name, Object value) {
        super(EventTopics.VARIABLES_TOPIC, ID);

        setProperty(PROP_PLUGIN_ID, pluginId);
        setProperty(PROP_DEVICE_ID, deviceId);
        setProperty(PROP_NAME, name);
        setProperty(PROP_VALUE, value);
    }

    public VariableUpdateRequestEvent(Map<String,Object> properties) {
        super(EventTopics.VARIABLES_TOPIC, properties);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public String getDeviceId() {
        return (String)getProperty(PROP_DEVICE_ID);
    }

    public String getName() {
        return (String)getProperty(PROP_NAME);
    }

    public Object getValue() {
        return getProperty(PROP_VALUE);
    }

    public boolean isGlobal() {
        return (getDeviceId() == null);
    }
}
