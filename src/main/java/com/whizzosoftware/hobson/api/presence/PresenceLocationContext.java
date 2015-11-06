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
 * A class that encapsulates the fully-qualified context of a presence location.
 *
 * @author Dan Noguerol
 */
public class PresenceLocationContext {
    private HubContext hubContext;
    private String locationId;

    public static PresenceLocationContext create(HubContext ctx, String entityId) {
        return new PresenceLocationContext(ctx, entityId);
    }

    public static PresenceLocationContext create(String s) {
        String[] comps = StringUtils.split(s, HubContext.DELIMITER);
        return PresenceLocationContext.create(HubContext.create(comps[0], comps[1]), comps[2]);
    }

    public static PresenceLocationContext createLocal(String entityId) {
        return new PresenceLocationContext(HubContext.createLocal(), entityId);
    }

    private PresenceLocationContext(HubContext hubContext, String locationId) {
        this.hubContext = hubContext;
        this.locationId = locationId;
    }

    public HubContext getHubContext() {
        return hubContext;
    }

    public String getHubId() {
        return hubContext.getHubId();
    }

    public String getUserId() {
        return hubContext.getUserId();
    }

    public String getLocationId() {
        return locationId;
    }

    public boolean equals(Object o) {
        return (
            o instanceof PresenceLocationContext &&
                ((PresenceLocationContext)o).hubContext.equals(hubContext) &&
                ((PresenceLocationContext)o).locationId.equals(locationId)
        );
    }

    public int hashCode() {
        return new HashCodeBuilder().append(hubContext).append(locationId).toHashCode();
    }

    public String toString() {
        return hubContext + HubContext.DELIMITER + locationId;
    }
}
