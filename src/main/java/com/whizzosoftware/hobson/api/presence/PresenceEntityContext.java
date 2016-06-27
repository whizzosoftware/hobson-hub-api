/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

import com.whizzosoftware.hobson.api.hub.HubContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A class that encapsulates the fully-qualified context of a presence entity.
 *
 * @author Dan Noguerol
 */
public class PresenceEntityContext {
    private HubContext hubContext;
    private String entityId;

    public static PresenceEntityContext create(HubContext ctx, String entityId) {
        return new PresenceEntityContext(ctx, entityId);
    }

    public static PresenceEntityContext create(String s) {
        String[] comps = StringUtils.split(s, HubContext.DELIMITER);
        return PresenceEntityContext.create(HubContext.create(comps[0]), comps[1]);
    }

    public static PresenceEntityContext createLocal(String entityId) {
        return new PresenceEntityContext(HubContext.createLocal(), entityId);
    }

    private PresenceEntityContext(HubContext hubContext, String entityId) {
        this.hubContext = hubContext;
        this.entityId = entityId;
    }

    public HubContext getHubContext() {
        return hubContext;
    }

    public String getHubId() {
        return hubContext.getHubId();
    }

    public String getEntityId() {
        return entityId;
    }

    public boolean equals(Object o) {
        return (
            o instanceof PresenceEntityContext &&
                ((PresenceEntityContext)o).hubContext.equals(hubContext) &&
                ((PresenceEntityContext)o).entityId.equals(entityId)
        );
    }

    public int hashCode() {
        return new HashCodeBuilder().append(hubContext).append(entityId).toHashCode();
    }

    public String toString() {
        return hubContext + HubContext.DELIMITER + entityId;
    }
}
