/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * An class that represents an external bridge. These are entities that are external to the Hub but that Hub plugins
 * must use to communicate with devices (e.g. serial ports).
 *
 * @author Dan Noguerol
 */
public class ExternalBridge {
    private String pluginId;
    private String type;
    private String name;
    private String value;

    /**
     * Constructor.
     *
     * @param pluginId the ID of the plugin that created the bridge
     * @param type the type of bridge
     * @param name the name of the bridge
     * @param value the "value" of the bridge -- this is what would be used when setting configuration properties
     */
    public ExternalBridge(String pluginId, String type, String name, String value) {
        this.pluginId = pluginId;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
