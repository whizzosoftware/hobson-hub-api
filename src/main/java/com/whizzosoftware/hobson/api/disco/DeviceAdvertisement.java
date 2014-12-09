/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A DeviceAdvertisement is a message that devices broadcast to indicate their availability (or lack thereof).
 * Generally, these objects will be consumed by plugins to determine if the advertised device is something they care
 * about.
 *
 * @author Dan Noguerol
 */
public class DeviceAdvertisement {
    private String id;
    private String protocolId;
    private String rawData;
    private Object object;

    public DeviceAdvertisement(String id, String protocolId, String rawData) {
        this.id = id;
        this.protocolId = protocolId;
        this.rawData = rawData;
    }

    public DeviceAdvertisement(String id, String protocolId, String rawData, Object object) {
        this(id, protocolId, rawData);
        this.object = object;
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
     * Returns a unique identifier for this device advertisement.
     *
     * @return an ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns an object representation of the advertisement. It will fall upon consumers to cast to the correct
     * class based on the protocol ID.
     *
     * @return an object
     */
    public Object getObject() {
        return object;
    }

    /**
     * Returns the raw data of the advertisement.
     *
     * @return the raw data as a String
     */
    public String getRawData() {
        return rawData;
    }
}
