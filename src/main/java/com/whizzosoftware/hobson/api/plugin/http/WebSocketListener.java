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
 * A listener for WebSocket events.
 *
 * @author Dan Noguerol
 */
public interface WebSocketListener {
    /**
     * Called when the WebSocket is successfully opened.
     *
     * @param handle the websocket handle
     */
    void onOpen(WebSocketHandle handle);

    /**
     * Called when the WebSocket is closed.
     *
     * @param handle the websocket handle
     */
    void onClose(WebSocketHandle handle);

    /**
     * Called when the WebSocket encounters an error condition.
     *
     * @param t the cause of the error
     */
    void onError(Throwable t);

    /**
     * Called when the WebSocket receives a message.
     *
     * @param b the message as a byte array
     */
    void onMessage(byte[] b);
}
