/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

import java.util.Collection;

/**
 * A manager interface for entity presence functions.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.7
 */
public interface PresenceManager {
    /**
     * Returns a summary of all presence entities.
     *
     * @return a Collection of PresenceEntity objects
     */
    public Collection<PresenceEntity> getAllEntities(String userId, String hubId);

    /**
     * Returns details of a presence entity.
     *
     * @param entityId the ID of the entity
     *
     * @return a PresenceEntity object
     */
    public PresenceEntity getEntity(String userId, String hubId, String entityId);

    /**
     * Adds a new presence entity.
     *
     * @param entity the entity to add
     *
     * @return the ID of the newly created entity
     */
    public String addEntity(String userId, String hubId, PresenceEntity entity);

    /**
     * Sends an update event for a presence entity.
     *
     * @param entityId the ID of the entity
     * @param name the name of the entity (or null if unchanged)
     * @param location the location of the entity (or null if unknown)
     */
    public void updateEntity(String userId, String hubId, String entityId, String name, String location);
}
