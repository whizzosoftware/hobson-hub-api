/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.event.plugin;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.plugin.PluginStatus;

import java.util.Map;

/**
 * Event that occurs when a plugin's status changes.
 *
 * @author Dan Noguerol
 */
public class PluginStatusChangeEvent extends PluginEvent {
    public static final String ID = "pluginStatusChange";
    private static final String PROP_PLUGIN_CONTEXT = "pluginCtx";
    private static final String PROP_STATUS = "status";

    public PluginStatusChangeEvent(long timestamp, PluginContext ctx, PluginStatus status) {
        super(timestamp, ID);
        setProperty(PROP_PLUGIN_CONTEXT, ctx);
        setProperty(PROP_STATUS, status);
    }

    public PluginStatusChangeEvent(Map<String,Object> properties) {
        super(properties);
    }

    public PluginContext getContext() {
        return (PluginContext)getProperty(PROP_PLUGIN_CONTEXT);
    }

    public PluginStatus getStatus() {
        return (PluginStatus)getProperty(PROP_STATUS);
    }
}
