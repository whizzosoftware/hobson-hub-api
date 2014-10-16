/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

import java.util.HashMap;
import java.util.Map;

/**
 * A reference to a HobsonAction.
 *
 * @author Dan Noguerol
 */
public class HobsonActionRef {
    private final String name;
    private final String pluginId;
    private final String actionId;
    private final Map<String,Object> properties = new HashMap<String,Object>();

    public HobsonActionRef(String pluginId, String actionId, String name) {
        this.pluginId = pluginId;
        this.actionId = actionId;
        this.name = name;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getActionId() {
        return actionId;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
