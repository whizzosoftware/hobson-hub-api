/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * A manager interface for local Hub-related functions.
 *
 * @author Dan Noguerol
 */
public interface LocalHubManager {
    /**
     * Authenticates the admin password.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param password the password to check
     *
     * @return true if the password is valid
     */
    public boolean authenticateAdmin(String userId, String hubId, String password);

    /**
     * Add a new appender for error logging.
     *
     * @param aAppender the appender to add
     */
    public void addErrorLogAppender(Object aAppender);

    /**
     * Remove an appender for error logging.
     *
     * @param aAppender the appender to remove
     */
    public void removeLogAppender(Object aAppender);
}
