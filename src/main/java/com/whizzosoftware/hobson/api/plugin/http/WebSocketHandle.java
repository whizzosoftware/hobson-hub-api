/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin.http;

/**
 * A handle to a WebSocket.
 *
 * @author Dan Noguerol
 */
public interface WebSocketHandle {
    /**
     * Sets the listener to use for callbacks.
     *
     * @param listener the listener
     *
     * @return the WebSocket handle
     */
    WebSocketHandle setListener(WebSocketListener listener);

    /**
     * Connect the WebSocket.
     */
    void connect();

    /**
     * Send a byte message.
     *
     * @param message a byte array
     *
     * @return the WebSocket handle
     */
    WebSocketHandle sendMessage(byte[] message);

    /**
     * Sends a text (String) message.
     *
     * @param message a String
     *
     * @return the WebSocket handle
     */
    WebSocketHandle sendTextMessage(String message);

    /**
     * Close the WebSocket.
     */
    void close();
}
