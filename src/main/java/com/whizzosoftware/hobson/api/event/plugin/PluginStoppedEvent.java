/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.plugin;

import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.Map;

/**
 * Event that occurs when a plugin is stopped.
 *
 * @author Dan Noguerol
 */
public class PluginStoppedEvent extends PluginEvent {
    public static final String ID = "pluginStopped";
    public static final String PROP_PLUGIN_CONTEXT = "pluginCtx";

    public PluginStoppedEvent(long timestamp, PluginContext ctx) {
        super(timestamp, ID);
        setProperty(PROP_PLUGIN_CONTEXT, ctx);
    }

    public PluginStoppedEvent(Map<String,Object> properties) {
        super(properties);
    }

    public PluginContext getContext() {
        return (PluginContext)getProperty(PROP_PLUGIN_CONTEXT);
    }
}
