/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.user;

import com.whizzosoftware.hobson.api.HobsonAuthenticationException;

import java.util.Collection;

/**
 * An interface for a store of user information.
 *
 * @author Dan Noguerol
 */
public interface UserStore {
    /**
     * Adds a new user.
     *
     * @param username the new user's name
     * @param password the new user's password
     * @param givenName the new user's first name
     * @param familyName the new user's last name
     * @param roles a set of roles that the user has
     */
    void addUser(String username, String password, String givenName, String familyName, Collection<HobsonRole> roles);

    /**
     * Authenticates a user.
     *
     * @param username the username
     * @param password the password
     *
     * @return a UserAuthentication instance representing the authenticated user
     *
     * @throws HobsonAuthenticationException on failure
     */
    UserAuthentication authenticate(String username, String password) throws HobsonAuthenticationException;

    /**
     * Returns the default user for this store.
     *
     * @return a String (or null if there is no default user)
     */
    String getDefaultUser();

    /**
     * Returns the hubs associated with a given user.
     *
     * @param username the user's name
     *
     * @return a Collection of hub IDs
     */
    Collection<String> getHubsForUser(String username);

    /**
     * Returns a specific user.
     *
     * @param username the name of the user
     *
     * @return a HobsonUser instance
     */
    HobsonUser getUser(String username);

    /**
     * Returns a list of all defined users.
     *
     * @return a Collection of HobsonUser instances
     */
    Collection<HobsonUser> getUsers();

    /**
     * Indicates whether this store has a default user associated with it.
     *
     * @return a boolean
     */
    boolean hasDefaultUser();

    /**
     * Indicates whether this store supports management of users.
     *
     * @return a boolean
     */
    boolean supportsUserManagement();
}
