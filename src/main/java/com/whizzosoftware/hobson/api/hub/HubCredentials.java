/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * Class that encapsulates credentials (userId &amp; apiKey) for a Hub.
 *
 * @author Dan Noguerol
 */
public class HubCredentials {
    private String userId;
    private String apiKey;

    public HubCredentials(String userId, String apiKey) {
        this.userId = userId;
        this.apiKey = apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getApiKey() {
        return apiKey;
    }
}
