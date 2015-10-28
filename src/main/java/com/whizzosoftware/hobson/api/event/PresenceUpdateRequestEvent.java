/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.presence.PresenceEntityContext;
import com.whizzosoftware.hobson.api.presence.PresenceLocation;
import com.whizzosoftware.hobson.api.presence.PresenceLocationContext;

import java.util.Map;

/**
 * An event for making presence update requests.
 *
 * @author Dan Noguerol
 */
public class PresenceUpdateRequestEvent extends HobsonEvent {
    public static final String ID = "presenceUpdateReq";

    private static final String PROP_ENTITY = "entity";
    private static final String PROP_LOCATION = "location";

    public PresenceUpdateRequestEvent(long timestamp, PresenceEntityContext entity, PresenceLocationContext location) {
        super(timestamp, EventTopics.PRESENCE_TOPIC, ID);

        setProperty(PROP_ENTITY, entity);
        setProperty(PROP_LOCATION, location);
    }

    public PresenceUpdateRequestEvent(Map<String, Object> properties) {
        super(EventTopics.PRESENCE_TOPIC, properties);
    }

    public PresenceEntityContext getEntityContext() {
        return (PresenceEntityContext)getProperty(PROP_ENTITY);
    }

    public PresenceLocationContext getLocation() {
        return (PresenceLocationContext)getProperty(PROP_LOCATION);
    }
}
