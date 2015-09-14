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
    private String uri;
    private String rawData;
    private Object object;

    private DeviceAdvertisement(String id, String protocolId) {
        this.id = id;
        this.protocolId = protocolId;
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
     * Returns the protocol the advertisement used (e.g. SSDP).
     *
     * @return the protocol
     */
    public String getProtocolId() {
        return protocolId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    static public class Builder {
        private DeviceAdvertisement da;

        public Builder(String id, String protocolId) {
            da = new DeviceAdvertisement(id, protocolId);
        }

        public Builder uri(String uri) {
            da.uri = uri;
            return this;
        }

        public Builder rawData(String rawData) {
            da.rawData = rawData;
            return this;
        }

        public Builder object(Object object) {
            da.object = object;
            return this;
        }

        public DeviceAdvertisement build() {
            return da;
        }
    }
}
