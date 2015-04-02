/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import java.util.Dictionary;

/**
 * A class representing the location of a Hub.
 *
 * @author Dan Noguerol
 */
public class HubLocation {
    public static final String PROP_LOCATION_STRING = "location.string";
    public static final String PROP_LATITUDE = "latitude";
    public static final String PROP_LONGITUDE = "longitude";

    private String text;
    private Double latitude;
    private Double longitude;

    public String getText() {
        return text;
    }

    public boolean hasLatitude() {
        return (latitude != null);
    }

    public Double getLatitude() {
        return latitude;
    }

    public boolean hasLongitude() {
        return (longitude != null);
    }

    public Double getLongitude() {
        return longitude;
    }

    public static class Builder {
        private HubLocation hubLocation = new HubLocation();

        public Builder dictionary(Dictionary d) {
            hubLocation.text =(String)d.get(PROP_LOCATION_STRING);
            if (d.get(PROP_LATITUDE) != null) {
                hubLocation.latitude = (Double)d.get(PROP_LATITUDE);
            }
            if (d.get(PROP_LONGITUDE) != null) {
                hubLocation.longitude = (Double)d.get(PROP_LONGITUDE);
            }
            return this;
        }

        public Builder text(String text) {
            hubLocation.text = text;
            return this;
        }

        public Builder latitude(Double latitude) {
            hubLocation.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            hubLocation.longitude = longitude;
            return this;
        }

        public HubLocation build() {
            return hubLocation;
        }
    }
}
