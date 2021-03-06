/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************
*/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.data.DataStreamManager;

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
    void addDataStreamManager(DataStreamManager manager);

    /**
     * Returns a viable network interface to be used for communication.
     *
     * @return a NetworkInterface object
     *
     * @throws IOException on failure
     */
    NetworkInfo getNetworkInfo() throws IOException;

    /**
     * Publish a web application to the local hub.
     *
     * @param app the hub application to publish
     */
    void publishWebApplication(HubWebApplication app);

    /**
     * Remove an appender for error logging.
     *
     * @param aAppender the appender to remove
     */
    void removeLogAppender(Object aAppender);

    /**
     * Sets connection information for a web socket server exposed by the hub.
     *
     * @param protocol the protocol of the web socket server (ws or wss)
     * @param port the port number of the server
     * @param path the URL path component
     */
    void setWebSocketInfo(String protocol, int port, String path);

    /**
     * Unpublish a previously published web application from the local hub.
     *
     * @param path the root URL path of the application
     */
    void unpublishWebApplication(String path);
}
