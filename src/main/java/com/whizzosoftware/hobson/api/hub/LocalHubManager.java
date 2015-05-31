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
     * Authenticates the local admin user.
     *
     * @param ctx the hub context
     * @param password the password to check
     *
     * @return true if the password is valid
     */
    public boolean authenticateLocal(HubContext ctx, String password);

    /**
     * Sets the password for the local admin user.
     *
     * @param ctx the context of the target hub
     * @param change a PasswordChange instance
     *
     * @throws com.whizzosoftware.hobson.api.HobsonInvalidRequestException if password does not meet complexity requirements
     */
    public void setLocalPassword(HubContext ctx, PasswordChange change);

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
