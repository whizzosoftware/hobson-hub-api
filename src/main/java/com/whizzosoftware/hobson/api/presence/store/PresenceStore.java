/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence.store;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.presence.PresenceEntity;
import com.whizzosoftware.hobson.api.presence.PresenceEntityContext;
import com.whizzosoftware.hobson.api.presence.PresenceLocation;
import com.whizzosoftware.hobson.api.presence.PresenceLocationContext;

import java.util.Collection;

public interface PresenceStore {
    Collection<PresenceEntity> getAllPresenceEntities(HubContext ctx);
    PresenceEntity getPresenceEntity(PresenceEntityContext ctx);
    void savePresenceEntity(PresenceEntity pe);
    void deletePresenceEntity(PresenceEntityContext ctx);
    Collection<PresenceLocation> getAllPresenceLocations(HubContext ctx);
    PresenceLocation getPresenceLocation(PresenceLocationContext ctx);
    void savePresenceLocation(PresenceLocation pl);
    void deletePresenceLocation(PresenceLocationContext ctx);
    void close();
}