/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

/**
 * Encapsulates information about a remote user.
 *
 * @author Dan Noguerol
 */
public class UserRemoteInfo {
    public int hubCount;

    public UserRemoteInfo(int hubCount) {
        this.hubCount = hubCount;
    }

    /**
     * Returns the number of hubs associated with this user.
     *
     * @return an int
     */
    public int getHubCount() {
        return hubCount;
    }
}
