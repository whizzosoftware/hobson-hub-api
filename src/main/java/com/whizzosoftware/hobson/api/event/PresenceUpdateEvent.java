/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import org.osgi.service.event.Event;

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

    private String entityId;
    private String location;

    public PresenceUpdateEvent(String entityId, String location) {
        super(EventTopics.PRESENCE_TOPIC, ID);

        this.entityId = entityId;
        this.location = location;
    }

    public PresenceUpdateEvent(Event event) {
        super(event);
    }

    public String getEntityId() {
        return entityId;
    }

    public String getLocation() {
        return location;
    }

    @Override
    void readProperties(Event event) {
        this.entityId = (String)event.getProperty(PROP_ENTITY_ID);
        this.location = (String)event.getProperty(PROP_LOCATION);
    }

    @Override
    void writeProperties(Map properties) {
        properties.put(PROP_ENTITY_ID, entityId);
        properties.put(PROP_LOCATION, location);
    }
}
