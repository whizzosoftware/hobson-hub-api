/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.util.UserUtil;

/**
 * Encapsulates contextual information for a plugin reference.
 *
 * @author Dan Noguerol
 */
public class PluginContext {
    private String userId;
    private String hubId;
    private String pluginId;

    /**
     * Creates a local plugin context.
     *
     * @param pluginId the plugin ID
     *
     * @return a PluginContext instance
     */
    public static PluginContext createLocal(String pluginId) {
        return new PluginContext(UserUtil.DEFAULT_USER, UserUtil.DEFAULT_HUB, pluginId);
    }

    /**
     * Constructor.
     *
     * @param userId the user ID associated with the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     */
    public PluginContext(String userId, String hubId, String pluginId) {
        this.userId = userId;
        this.hubId = hubId;
        this.pluginId = pluginId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHubId() {
        return hubId;
    }

    public String getPluginId() {
        return pluginId;
    }
}
