/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * A class that encapsulates the fully-qualified context of a hub.
 *
 * @author Dan Noguerol
 */
public class HubContext {
    public static final String DEFAULT_USER = "local";
    public static final String DEFAULT_HUB = "local";

    public String userId;
    public String hubId;

    public static HubContext create(String userId, String hubId) {
        return new HubContext(userId, hubId);
    }

    public static HubContext createLocal() {
        return create(DEFAULT_USER, DEFAULT_HUB);
    }

    private HubContext(String userId, String hubId) {
        this.userId = userId;
        this.hubId = hubId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHubId() {
        return hubId;
    }
}
