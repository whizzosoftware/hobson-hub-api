/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import java.util.Map;

/**
 * Event that occurs when an entity's presence has changed.
 *
 * @author Dan Noguerol
 */
public class PresenceUpdateEvent extends HobsonEvent {
    public static final String ID = "presenceUpdate";

    private static final String PROP_ENTITY_ID = "entityId";
    private static final String PROP_LOCATION = "location";

    public PresenceUpdateEvent(long timestamp, String entityId, String location) {
        super(timestamp, EventTopics.PRESENCE_TOPIC, ID);

        setProperty(PROP_ENTITY_ID, entityId);
        setProperty(PROP_LOCATION, location);
    }

    public PresenceUpdateEvent(Map<String,Object> properties) {
        super(EventTopics.PRESENCE_TOPIC, properties);
    }

    public String getEntityId() {
        return (String)getProperty(PROP_ENTITY_ID);
    }

    public String getLocation() {
        return (String)getProperty(PROP_LOCATION);
    }
}
