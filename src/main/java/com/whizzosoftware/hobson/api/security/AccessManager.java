/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.security;

import com.whizzosoftware.hobson.api.HobsonAuthenticationException;
import com.whizzosoftware.hobson.api.HobsonAuthorizationException;
import com.whizzosoftware.hobson.api.hub.OIDCConfig;
import com.whizzosoftware.hobson.api.hub.PasswordChange;

import java.util.Collection;
import java.util.Set;

/**
 * Interface for managing authentication and authorization for the Hub.
 *
 * @author Dan Noguerol
 */
public interface AccessManager {
    /**
     * Add a new user.
     *
     * @param userId the user ID
     * @param password the password
     * @param givenName the user's first name
     * @param familyName the user's last name
     * @param roles the roles assigned to the user
     */
    void addUser(String userId, String password, String givenName, String familyName, Collection<String> roles);

    /**
     * Authenticates a user.
     *
     * @param userId the user ID
     * @param password the password
     *
     * @return a HobsonUser instance representing the authenticated user
     * @throws HobsonAuthenticationException if the user ID/password combination are invalid
     */
    HobsonUser authenticate(String userId, String password) throws HobsonAuthenticationException;

    /**
     * Authenticates a user.
     *
     * @param token a token
     *
     * @return a HobsonUser instance representing the authenticated user
     * @throws HobsonAuthenticationException if the token is invalid
     */
    HobsonUser authenticate(String token) throws HobsonAuthenticationException;

    /**
     * Authorizes a user's access to an action/resource combination.
     *
     * @param user the user being checked
     * @param action the action that is being performed
     * @param resource the resource the action is being attempted against
     *
     * @throws HobsonAuthorizationException if the user is not authorized
     */
    void authorize(HobsonUser user, String action, String resource) throws HobsonAuthorizationException;

    /**
     * Changes the password for a user.
     *
     * @param userId the user ID
     * @param password the new password
     */
    void changeUserPassword(String userId, PasswordChange password);

    /**
     * Creates a token for a user.
     *
     * @param user the user
     *
     * @return the token
     */
    String createToken(HobsonUser user);

    /**
     * Returns the default user if one is available. If a default user exists, it is used when only a
     * password is provided at authentication time.
     *
     * @return a user ID
     */
    String getDefaultUser();

    /**
     * Returns the hubs associated with a user.
     *
     * @param userId the user ID
     *
     * @return a Collection of hub IDs (or null if the user has no hubs)
     */
    Collection<String> getHubsForUser(String userId);

    /**
     * Returns the OpenID Connect configuration.
     *
     * @return an OIDCConfig instance
     */
    OIDCConfig getOIDCConfig();

    /**
     * Returns all the valid roles that can be assigned to a user.
     *
     * @return a Set of Strings
     */
    Set<String> getRoles();

    /**
     * Returns details about a user.
     *
     * @param userId the user ID
     *
     * @return a HobsonUser instance
     */
    HobsonUser getUser(String userId);

    /**
     * Returns all users in the user store.
     *
     * @return a Collection of HobsonUser instances
     */
    Collection<HobsonUser> getUsers();

    /**
     * Indicates whether this access manager has a default user (e.g. getDefaultUser() would not return null).
     *
     * @return a boolean
     */
    boolean hasDefaultUser();

    /**
     * Indicates whether this access manager is fronting a federated identity provider.
     *
     * @return a boolean
     */
    boolean isFederated();
}
