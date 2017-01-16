/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************
*/
package com.whizzosoftware.hobson.api.hub;

/**
 * Connection information for a web socket.
 *
 * @author Dan Noguerol
 */
public class WebSocketInfo {
    private String protocol;
    private int port;
    private String path;

    public WebSocketInfo(String protocol, int port, String path) {
        this.protocol = protocol;
        this.port = port;
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
