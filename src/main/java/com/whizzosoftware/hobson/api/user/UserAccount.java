/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

import java.util.Arrays;
import java.util.Collection;

/**
 * Encapsulates information about a user's CloudLink account.
 *
 * @author Dan Noguerol
 */
public class UserAccount {
    public Long expiration;
    public Collection<String> hubs;

    public UserAccount(String expiration, String hubIds) {
        this(expiration != null ? Long.parseLong(expiration) : null, hubIds != null ? Arrays.asList(hubIds.split(",")) : null);
    }

    public UserAccount(Long expiration, Collection<String> hubs) {
        this.expiration = expiration;
        this.hubs = hubs;
    }

    /**
     * Returns the account expiration date.
     *
     * @return a Long (or null if no expiration)
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * Returns the hubs associated with the user account.
     *
     * @return a Collection of hub ID strings (or null if there are no hub associations)
     */
    public Collection<String> getHubs() {
        return hubs;
    }

    /**
     * Indicates whether the account has any available hub slots.
     *
     * @return a boolean
     */
    public boolean hasAvailableHubs() {
        return hubs != null && hubs.size() > 0;
    }
}
