/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.Collection;

/**
 * A manager interface for entity presence functions.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.7
 */
public interface PresenceManager {
    /**
     * Returns all presence entities for a hub.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of PresenceEntity objects
     */
    Collection<PresenceEntity> getAllEntities(HubContext ctx);

    /**
     * Returns details of a presence entity.
     *
     * @param ctx the context of the entity
     *
     * @return a PresenceEntity object
     */
    PresenceEntity getEntity(PresenceEntityContext ctx);

    /**
     * Adds a new presence entity.
     *
     * @param hctx the context of the hub that owns the entity
     * @param name the name of the entity
     *
     * @return the ID of the newly created entity
     */
    PresenceEntityContext addEntity(HubContext hctx, String name);

    PresenceLocation getEntityLocation(PresenceEntityContext ctx);

    /**
     * Sends an update event for a presence entity.
     *
     * @param ectx the context of the entity
     * @param lctx the context of the new location
     */
    void updateEntityLocation(PresenceEntityContext ectx, PresenceLocationContext lctx);

    /**
     * Retrieves a lit of all presence locations.
     *
     * @param ctx the context of the target hub
     *
     * @return a Collection of PresenceLocation objects
     */
    Collection<PresenceLocation> getAllLocations(HubContext ctx);

    /**
     * Returns details of a presence location.
     *
     * @param ctx the context of the presence location
     *
     * @return a PresenceLocation object
     */
    PresenceLocation getLocation(PresenceLocationContext ctx);

    /**
     * Adds a new presence location.
     *
     * @param hctx the context of the target hub
     * @param name the name of the location
     * @param latitude the location's latitude (for map-based locations)
     * @param longitude the location's longitude (for map-based locations)
     * @param radius the radius around the lat/long (for map-based locations)
     * @param beaconMajor the beacon major number (for beacon-based locations)
     * @param beaconMinor the beacon minor number (for beacon-based locations)
     *
     * @return the context of the newly created location
     */
    PresenceLocationContext addLocation(HubContext hctx, String name, Double latitude, Double longitude, Double radius, Integer beaconMajor, Integer beaconMinor);
}
