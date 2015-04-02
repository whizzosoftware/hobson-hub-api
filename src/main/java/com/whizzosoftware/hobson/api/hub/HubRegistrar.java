/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * A management interface that provides the ability to associate/disassociate hubs with a user.
 *
 * @author Dan Noguerol
 */
public interface HubRegistrar {
    /**
     * Associates a new hub with a user.
     *
     * @param userId the user ID that will own the hub
     * @param name the name of the new hub
     *
     * @return a HubInfo object
     */
    public HobsonHub addHub(String userId, String name);

    /**
     * Removes a previously added hub.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the ID of the hub to remove
     */
    public void removeHub(String userId, String hubId);
}
