/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.plugin.http.ning;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ws.WebSocket;
import com.ning.http.client.ws.WebSocketByteListener;
import com.ning.http.client.ws.WebSocketUpgradeHandler;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.plugin.http.WebSocketHandle;
import com.whizzosoftware.hobson.api.plugin.http.WebSocketListener;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * WebSocketHandle implementation using Ning AsyncHttpClient.
 *
 * @author Dan Noguerol
 */
public class NingWebSocketHandle implements WebSocketHandle {
    private AsyncHttpClient client;
    private URI uri;
    private Map<String,Collection<String>> headers;
    private WebSocket webSocket;
    private WebSocketListener listener;

    public NingWebSocketHandle(AsyncHttpClient client, URI uri, Map<String,Collection<String>> headers) {
        this.client = client;
        this.uri = uri;
        this.headers = headers;
    }

    @Override
    public WebSocketHandle setListener(WebSocketListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void connect() throws HobsonRuntimeException {
        try {
            this.webSocket = client.prepareGet(uri.toASCIIString()).
                setHeaders(headers).
                execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(
                    new WebSocketByteListener() {
                        @Override
                        public void onMessage(byte[] bytes) {
                            if (listener != null) {
                                listener.onMessage(bytes);
                            }
                        }

                        @Override
                        public void onOpen(WebSocket webSocket) {
                            if (listener != null) {
                                listener.onOpen(NingWebSocketHandle.this);
                            }
                        }

                        @Override
                        public void onClose(WebSocket webSocket) {
                            if (listener != null) {
                                listener.onClose(NingWebSocketHandle.this);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (listener != null) {
                                listener.onError(throwable);
                            }
                        }
                    }
                ).build()).get();
        } catch (Exception e) {
            throw new HobsonRuntimeException("Error connecting WebSocket", e);
        }
    }

    @Override
    public WebSocketHandle sendMessage(byte[] message) {
        webSocket.sendMessage(message);
        return this;
    }

    @Override
    public WebSocketHandle sendTextMessage(String message) {
        webSocket.sendMessage(message);
        return this;
    }

    @Override
    public void close() {
        webSocket.close();
    }
}
