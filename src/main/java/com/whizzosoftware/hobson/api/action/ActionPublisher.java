/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

/**
 * An interface for classes that publish and unpublish Hobson actions.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.5.0
 */
public interface ActionPublisher {
    /**
     * Publishes a new action.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param action the action to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishAction(String userId, String hubId, HobsonAction action);

    /**
     * Unpublishes all actions previous published by a plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the ID of the plugin that published the actions
     */
    public void unpublishAllActions(String userId, String hubId, String pluginId);
}
