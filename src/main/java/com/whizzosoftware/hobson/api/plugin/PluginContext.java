/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.hub.HubContext;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Encapsulates contextual information for a plugin reference.
 *
 * @author Dan Noguerol
 */
public class PluginContext {
    private final HubContext hubContext;
    private final String pluginId;

    /**
     * Create a plugin context.
     *
     * @param ctx the hub context
     * @param pluginId the plugin ID
     *
     * @return a PluginContext instance
     */
    public static PluginContext create(HubContext ctx, String pluginId) {
        return new PluginContext(ctx, pluginId);
    }

    /**
     * Creates a local plugin context.
     *
     * @param pluginId the plugin ID
     *
     * @return a PluginContext instance
     */
    public static PluginContext createLocal(String pluginId) {
        return new PluginContext(HubContext.createLocal(), pluginId);
    }

    /**
     * Constructor.
     *
     * @param ctx the hub context
     * @param pluginId the plugin ID
     */
    private PluginContext(HubContext ctx, String pluginId) {
        this.hubContext = ctx;
        this.pluginId = pluginId;
    }

    public HubContext getHubContext() {
        return hubContext;
    }

    public String getUserId() {
        return hubContext.getUserId();
    }

    public String getHubId() {
        return hubContext.getHubId();
    }

    public String getPluginId() {
        return pluginId;
    }

    public boolean equals(Object o) {
        return (
            o instanceof PluginContext &&
            ((PluginContext)o).hubContext.equals(hubContext) &&
            (((PluginContext)o).pluginId == null && pluginId == null ||
                ((PluginContext)o).pluginId.equals(pluginId))
        );
    }

    public int hashCode() {
        return new HashCodeBuilder().append(hubContext).append(pluginId).toHashCode();
    }

    public String toString() {
        return hubContext + HubContext.DELIMITER + pluginId;
    }
}
