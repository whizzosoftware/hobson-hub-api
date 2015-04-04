/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.UUID;

/**
 * A class that represents the current presence status of an entity.
 *
 * @author Dan Noguerol
 */
public class PresenceEntity {
    private PresenceEntityContext ctx;
    protected String name;
    protected String location;
    protected Long lastUpdate;

    public PresenceEntity(HubContext ctx, String name, String location) {
        this.ctx = PresenceEntityContext.create(ctx, UUID.randomUUID().toString());
        this.name = name;
        this.location = location;
    }

    public PresenceEntityContext getContext() {
        return ctx;
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
