/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * A class encapsulating a NetworkInterface and associated InetAddress.
 *
 * @author Dan Noguerol
 */
public class NetworkInfo {
    private NetworkInterface networkInterface;
    private InetAddress inetAddress;

    public NetworkInfo(NetworkInterface networkInterface, InetAddress inetAddress) {
        this.networkInterface = networkInterface;
        this.inetAddress = inetAddress;
    }

    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
}
