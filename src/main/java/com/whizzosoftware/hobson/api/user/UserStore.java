/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

import com.whizzosoftware.hobson.api.HobsonAuthenticationException;

/**
 * An interface for a store of user information.
 *
 * @author Dan Noguerol
 */
public interface UserStore {
    /**
     * Indicates whether this store has a default user associated with it.
     *
     * @return a boolean
     */
    boolean hasDefaultUser();

    /**
     * Returns the default user for this store.
     *
     * @return a String (or null if there is no default user)
     */
    String getDefaultUser();

    /**
     * Authenticates a user.
     *
     * @param username the username
     * @param password the password
     *
     * @return a HobsonUser instance representing the authenticated user
     *
     * @throws HobsonAuthenticationException on failure
     */
    HobsonUser authenticate(String username, String password) throws HobsonAuthenticationException;

    /**
     * Returns a user's information.
     *
     * @param username the username
     *
     * @return a HobsonUser instance
     */
    HobsonUser getUser(String username);
}
