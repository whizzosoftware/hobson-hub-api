/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

import com.whizzosoftware.hobson.api.hub.HubContext;

/**
 * A class that encapsulates the fully-qualified context of a presence entity.
 *
 * @author Dan Noguerol
 */
public class PresenceEntityContext {
    private HubContext ctx;
    private String entityId;

    public static PresenceEntityContext create(HubContext ctx, String entityId) {
        return new PresenceEntityContext(ctx, entityId);
    }

    public static PresenceEntityContext createLocal(String entityId) {
        return new PresenceEntityContext(HubContext.createLocal(), entityId);
    }

    private PresenceEntityContext(HubContext ctx, String entityId) {
        this.ctx = ctx;
        this.entityId = entityId;
    }

    public HubContext getHubContext() {
        return ctx;
    }

    public String getHubId() {
        return ctx.getHubId();
    }

    public String getUserId() {
        return ctx.getUserId();
    }

    public String getEntityId() {
        return entityId;
    }
}
