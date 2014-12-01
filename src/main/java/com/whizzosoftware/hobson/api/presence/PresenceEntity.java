/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

import java.util.UUID;

/**
 * A class that represents the current presence status of an entity.
 *
 * @author Dan Noguerol
 */
public class PresenceEntity {
    private String id;
    protected String name;
    protected String location;
    protected Long lastUpdate;

    public PresenceEntity(String name, String location) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
}
