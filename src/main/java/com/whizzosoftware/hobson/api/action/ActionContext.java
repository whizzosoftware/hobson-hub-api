/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

/**
 * A class that encapsulates the fully-qualified context of an action.
 *
 * @author Dan Noguerol
 */
public class ActionContext {
    private PluginContext ctx;
    private String actionId;

    public static ActionContext create(PluginContext ctx, String actionId) {
        return new ActionContext(ctx, actionId);
    }

    public static ActionContext createLocal(String pluginId, String actionId) {
        return create(PluginContext.createLocal(pluginId), actionId);
    }

    /**
     * Constructor.
     *
     * @param ctx the plugin context associated with the action
     * @param actionId the action ID
     */
    private ActionContext(PluginContext ctx, String actionId) {
        this.ctx = ctx;
        this.actionId = actionId;
    }

    public HubContext getHubContext() {
        return ctx.getHubContext();
    }

    public PluginContext getPluginContext() {
        return ctx;
    }

    public String getHubId() {
        return getHubContext().getHubId();
    }

    public String getUserId() {
        return getHubContext().getUserId();
    }

    public String getPluginId() {
        return ctx.getPluginId();
    }

    public String getActionId() {
        return actionId;
    }
}
