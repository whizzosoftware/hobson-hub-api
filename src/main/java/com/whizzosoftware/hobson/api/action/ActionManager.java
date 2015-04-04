/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.Collection;
import java.util.Map;

/**
 * An interface for classes that manage Hobson actions.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface ActionManager {
    /**
     * Executes an action.
     *
     * @param ctx the context of the action
     * @param properties the map of arguments to use for the execution
     *
     * @since hobson-hub-api 0.1.6
     */
    public void executeAction(ActionContext ctx, Map<String,Object> properties);

    /**
     * Retrieves all published actions.
     *
     * @param ctx the context of the hub that published the actions
     *
     * @return a Collection of HobsonAction instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonAction> getAllActions(HubContext ctx);

    /**
     * Retrieve a published action.
     *
     * @param ctx the context of the action
     *
     * @return a HobsonAction instance (or null if not found)
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonAction getAction(ActionContext ctx);

    /**
     * Publishes a new action.
     *
     * @param action the action to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishAction(HobsonAction action);

    /**
     * Unpublishes all actions previous published by a plugin.
     *
     * @param ctx the context of the plugin that published the actions
     */
    public void unpublishAllActions(PluginContext ctx);
}
