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
    /**
     * Returns a collection of all presence entities.
     *
     * @param ctx the hub context
     *
     * @return a collection of PresenceEntity instances
     */
    Collection<PresenceEntity> getAllPresenceEntities(HubContext ctx);

    /**
     * Returns a presence entity.
     *
     * @param ctx the context of the presence entity
     *
     * @return a PresenceEntity instance
     */
    PresenceEntity getPresenceEntity(PresenceEntityContext ctx);

    /**
     * Saves a presence entity.
     *
     * @param pe the presence entity to save
     */
    void savePresenceEntity(PresenceEntity pe);

    /**
     * Deletes an existing presence entity.
     *
     * @param ctx the context of the presence entity
     */
    void deletePresenceEntity(PresenceEntityContext ctx);

    /**
     * Returns a collection of all presence locations.
     *
     * @param ctx the hub context
     *
     * @return a collection of PresenceLocation instances
     */
    Collection<PresenceLocation> getAllPresenceLocations(HubContext ctx);

    /**
     * Returns a presence location.
     *
     * @param ctx the presence location context
     *
     * @return a PresenceLocation instance
     */
    PresenceLocation getPresenceLocation(PresenceLocationContext ctx);

    /**
     * Saves a presence location.
     *
     * @param pl the presence location to save
     */
    void savePresenceLocation(PresenceLocation pl);

    /**
     * Deletes a presence location.
     *
     * @param ctx the presence location context
     */
    void deletePresenceLocation(PresenceLocationContext ctx);

    /**
     * Cleans up and closes the presence store.
     */
    void close();
}