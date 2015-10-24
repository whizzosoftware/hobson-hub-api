/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

/**
 * Encapsulates information about a user's CloudLink account.
 *
 * @author Dan Noguerol
 */
public class UserAccount {
    public long expiration;
    public boolean hasAvailableHubs;

    public UserAccount(long expiration, boolean hasAvailableHubs) {
        this.expiration = expiration;
        this.hasAvailableHubs = hasAvailableHubs;
    }

    /**
     * Returns the account expiration date.
     *
     * @return a long
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Indicates whether the account has any available hub slots.
     *
     * @return a boolean
     */
    public boolean hasAvailableHubs() {
        return hasAvailableHubs;
    }
}
