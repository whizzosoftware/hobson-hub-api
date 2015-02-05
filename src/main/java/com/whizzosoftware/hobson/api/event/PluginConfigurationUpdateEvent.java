/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.config.Configuration;

import java.util.Dictionary;
import java.util.Map;

/**
 * Event that occurs when a plugin's configuration is updated.
 *
 * @author Dan Noguerol
 */
public class PluginConfigurationUpdateEvent extends HobsonEvent {
    public static final String ID = "pluginConfigurationUpdate";

    private static final String PROP_PLUGIN_ID = "pluginId";
    private static final String PROP_CONFIGURATION = "configuration";

    public PluginConfigurationUpdateEvent(String pluginId, Configuration configuration) {
        super(EventTopics.CONFIG_TOPIC, ID);
        setProperty(PROP_PLUGIN_ID, pluginId);
        setProperty(PROP_CONFIGURATION, configuration);
    }

    public PluginConfigurationUpdateEvent(Map<String,Object> props) {
        super(EventTopics.CONFIG_TOPIC, props);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public Configuration getConfiguration() {
        return (Configuration)getProperty(PROP_CONFIGURATION);
    }
}
