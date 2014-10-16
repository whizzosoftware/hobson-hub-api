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
 * Event that occurs when a plugin is stopped.
 *
 * @author Dan Noguerol
 */
public class PluginStoppedEvent extends HobsonEvent {
    public static final String ID = "pluginStopped";

    private static final String PROP_PLUGIN_ID = "pluginId";

    private String pluginId;

    public PluginStoppedEvent(String pluginId) {
        super(EventTopics.PLUGINS_TOPIC, ID);
        this.pluginId = pluginId;
    }

    public PluginStoppedEvent(Event event) {
        super(event);
    }

    public String getPluginId() {
        return pluginId;
    }

    @Override
    void readProperties(Event event) {
        pluginId = (String)event.getProperty(PROP_PLUGIN_ID);
    }

    @Override
    void writeProperties(Map properties) {
        properties.put(PROP_PLUGIN_ID, getPluginId());
    }
}
