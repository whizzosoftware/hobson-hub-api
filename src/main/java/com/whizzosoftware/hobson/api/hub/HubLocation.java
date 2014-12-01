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

    public HubLocation(Dictionary d) {
        text = (String)d.get(PROP_LOCATION_STRING);
        if (d.get(PROP_LATITUDE) != null) {
            latitude = (Double)d.get(PROP_LATITUDE);
        }
        if (d.get(PROP_LONGITUDE) != null) {
            longitude = (Double)d.get(PROP_LONGITUDE);
        }
    }

    public HubLocation(String text, Double latitude, Double longitude) {
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
}
