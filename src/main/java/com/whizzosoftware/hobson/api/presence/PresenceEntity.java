/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

/**
 * A class that represents an entity that can have presence.
 *
 * @author Dan Noguerol
 */
public class PresenceEntity {
    private PresenceEntityContext ctx;
    protected String name;
    protected Long lastUpdate;

    public PresenceEntity(PresenceEntityContext ctx, String name) {
        this.ctx = ctx;
        this.name = name;
    }

    public PresenceEntityContext getContext() {
        return ctx;
    }

    public String getName() {
        return name;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
}
