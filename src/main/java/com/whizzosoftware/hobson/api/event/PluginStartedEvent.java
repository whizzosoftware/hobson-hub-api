/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.Map;

/**
 * Event that occurs when a plugin is started.
 *
 * @author Dan Noguerol
 */
public class PluginStartedEvent extends HobsonEvent {
    public static final String ID = "pluginStarted";
    public static final String PROP_PLUGIN_ID = "pluginId";

    public PluginStartedEvent(PluginContext ctx) {
        super(EventTopics.PLUGINS_TOPIC, ID);
        setProperty(PROP_PLUGIN_ID, ctx.getPluginId());
    }

    public PluginStartedEvent(Map<String,Object> properties) {
        super(EventTopics.PLUGINS_TOPIC, properties);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }
}
