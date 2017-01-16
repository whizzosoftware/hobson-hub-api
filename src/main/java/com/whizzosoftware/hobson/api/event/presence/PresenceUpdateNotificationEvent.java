/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.presence;

import com.whizzosoftware.hobson.api.presence.PresenceEntityContext;
import com.whizzosoftware.hobson.api.presence.PresenceLocationContext;

import java.util.Map;

/**
 * Event that occurs when an entity's presence has changed.
 *
 * @author Dan Noguerol
 */
public class PresenceUpdateNotificationEvent extends PresenceEvent {
    public static final String ID = "presenceUpdateNotify";

    private static final String PROP_ENTITY = "entity";
    private static final String PROP_OLD_LOCATION = "oldLocation";
    private static final String PROP_NEW_LOCATION = "newLocation";

    public PresenceUpdateNotificationEvent(long timestamp, PresenceEntityContext entity, PresenceLocationContext oldLocation, PresenceLocationContext newLocation) {
        super(timestamp, ID);

        setProperty(PROP_ENTITY, entity);
        setProperty(PROP_OLD_LOCATION, oldLocation);
        setProperty(PROP_NEW_LOCATION, newLocation);
    }

    public PresenceUpdateNotificationEvent(Map<String, Object> properties) {
        super(properties);
    }

    public PresenceEntityContext getEntityContext() {
        return (PresenceEntityContext)getProperty(PROP_ENTITY);
    }

    public PresenceLocationContext getOldLocation() {
        return (PresenceLocationContext)getProperty(PROP_OLD_LOCATION);
    }

    public PresenceLocationContext getNewLocation() {
        return (PresenceLocationContext)getProperty(PROP_NEW_LOCATION);
    }
}
