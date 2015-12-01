/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

/**
 * Credentials for a user's CloudLink account.
 *
 * @author Dan Noguerol
 */
public class CloudLinkCredentials {
    private String hubId;
    private String accessKey;

    public CloudLinkCredentials(String hubId, String accessKey) {
        this.hubId = hubId;
        this.accessKey = accessKey;
    }

    public String getHubId() {
        return hubId;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
