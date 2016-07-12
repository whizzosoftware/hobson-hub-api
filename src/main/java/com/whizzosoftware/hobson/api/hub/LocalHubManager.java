/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.telemetry.TelemetryManager;

import java.io.IOException;

/**
 * A manager interface for functions that only apply to a local hub.
 *
 * @author Dan Noguerol
 */
public interface LocalHubManager {
    /**
     * Add a new appender for error logging.
     *
     * @param aAppender the appender to add
     */
    void addErrorLogAppender(Object aAppender);

    /**
     * Adds a manager to use for telemetry functions.
     *
     * @param manager the manager
     */
    void addTelemetryManager(TelemetryManager manager);

    /**
     * Authenticates the local admin user.
     *
     * @param ctx the hub context
     * @param password the password to check (plain text)
     *
     * @return true if the password is valid
     */
    boolean authenticateLocal(HubContext ctx, String password);

    /**
     * Returns a viable network interface to be used for communication.
     *
     * @return a NetworkInterface object
     */
    NetworkInfo getNetworkInfo() throws IOException;

    /**
     * Publish a web application to the local hub.
     *
     * @param app the hub application to publish
     */
    void publishWebApplication(HubWebApplication app);

    /**
     * Sets the password for the local admin user.
     *
     * @param ctx the context of the target hub
     * @param change a PasswordChange instance
     *
     * @throws com.whizzosoftware.hobson.api.HobsonInvalidRequestException if password does not meet complexity requirements
     */
    void setLocalPassword(HubContext ctx, PasswordChange change);

    /**
     * Remove an appender for error logging.
     *
     * @param aAppender the appender to remove
     */
    void removeLogAppender(Object aAppender);

    /**
     * Sets a URI for a web socket server exposed by the hub.
     *
     * @param uri the URI of the websocket server
     */
    void setWebSocketUri(String uri);

    /**
     * Unpublish a previously published web application from the local hub.
     *
     * @param path the root URL path of the application
     */
    void unpublishWebApplication(String path);
}
