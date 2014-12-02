/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

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
     * Publishes a new action.
     *
     * @param action the HobsonAction instance
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishAction(HobsonAction action);

    /**
     * Executes an action.
     *
     * @param pluginId the plugin ID associated with the action
     * @param actionId the action ID
     * @param properties the map of arguments to use for the execution
     *
     * @since hobson-hub-api 0.1.6
     */
    public void executeAction(String pluginId, String actionId, Map<String,Object> properties);

    /**
     * Retrieves all published actions.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     *
     * @return a Collection of HobsonAction instances
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<HobsonAction> getAllActions(String userId, String hubId);

    /**
     * Retrieve a published action.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID that published the action
     * @param actionId the action ID
     *
     * @return a HobsonAction instance (or null if not found)
     *
     * @since hobson-hub-api 0.1.6
     */
    public HobsonAction getAction(String userId, String hubId, String pluginId, String actionId);
}
