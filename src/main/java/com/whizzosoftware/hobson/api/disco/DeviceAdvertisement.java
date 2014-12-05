/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A DeviceAdvertisement is a message that devices broadcast to indicate their availability (or lack thereof). This
 * class treats the advertisement payload as a raw string. Generally, these objects will be consumed by
 * DeviceAdvertisementListener objects to determine if the advertised device is something they care about.
 *
 * @author Dan Noguerol
 */
public class DeviceAdvertisement {
    private String protocolId;
    private String data;

    public DeviceAdvertisement(String protocolId, String data) {
        this.protocolId = protocolId;
        this.data = data;
    }

    /**
     * Returns the protocol the advertisement used (e.g. SSDP).
     *
     * @return the protocol
     */
    public String getProtocolId() {
        return protocolId;
    }

    /**
     * Returns the raw data of the advertisement.
     *
     * @return the raw data as a String
     */
    public String getData() {
        return data;
    }
}
