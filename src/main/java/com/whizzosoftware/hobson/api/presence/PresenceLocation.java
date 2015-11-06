/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

/**
 * A class that encapsulates information about a presence location (i.e. a location that a presence entity can
 * enter/exit).
 *
 * @author Dan Noguerol
 */
public class PresenceLocation {
    private PresenceLocationContext context;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private Integer beaconMajor;
    private Integer beaconMinor;

    public PresenceLocation(PresenceLocationContext context, String name) {
        this.context = context;
        this.name = name;
    }

    public PresenceLocation(PresenceLocationContext context, String name, double latitude, double longitude, double radius) {
        this(context, name, latitude, longitude, radius, null, null);
    }

    public PresenceLocation(PresenceLocationContext context, String name, int beaconMajor, int beaconMinor) {
        this(context, name, null, null, null, beaconMajor, beaconMinor);
    }

    public PresenceLocation(PresenceLocationContext context, String name, Double latitude, Double longitude, Double radius, Integer beaconMajor, Integer beaconMinor) {
        this(context, name);
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.beaconMajor = beaconMajor;
        this.beaconMinor = beaconMinor;
    }

    public PresenceLocationContext getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public Integer getBeaconMajor() {
        return beaconMajor;
    }

    public Integer getBeaconMinor() {
        return beaconMinor;
    }
}
