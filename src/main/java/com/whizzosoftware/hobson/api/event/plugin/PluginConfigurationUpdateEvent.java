/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.plugin;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;

import java.util.Map;

/**
 * Event that occurs when a plugin's configuration is updated.
 *
 * @author Dan Noguerol
 */
public class PluginConfigurationUpdateEvent extends PluginEvent {
    public static final String ID = "pluginConfigurationUpdate";

    private static final String PROP_PLUGIN_ID = "pluginId";
    private static final String PROP_CONFIGURATION = "configuration";

    public PluginConfigurationUpdateEvent(long timestamp, PluginContext ctx, PropertyContainer configuration) {
        super(timestamp, ID);
        setProperty(PROP_PLUGIN_ID, ctx.getPluginId());
        setProperty(PROP_CONFIGURATION, configuration);
    }

    public PluginConfigurationUpdateEvent(Map<String,Object> props) {
        super(props);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public PropertyContainer getConfiguration() {
        return (PropertyContainer)getProperty(PROP_CONFIGURATION);
    }
}
