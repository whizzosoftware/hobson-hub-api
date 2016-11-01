/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.store;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;

import java.util.Collection;
import java.util.List;

/**
 * An interface for an action-related storage mechanism.
 *
 * @author Dan Noguerol
 */
public interface ActionStore {
    /**
     * Returns all action sets in the store.
     *
     * @param ctx a hub context
     *
     * @return a Collection of PropertyContainerSet instances representing the actions
     */
    Collection<PropertyContainerSet> getAllActionSets(HubContext ctx);

    /**
     * Returns a particular action set from the store.
     *
     * @param ctx a hub context
     * @param actionSetId an action set ID
     *
     * @return a PropertyContainerSet instance
     */
    PropertyContainerSet getActionSet(HubContext ctx, String actionSetId);

    /**
     * Adds an action set to the store.
     *
     * @param ctx a hub context
     * @param name the name of the action set
     * @param actions the actions that comprise the set
     *
     * @return a PropertyContainerSet instance representing the new action set
     */
    PropertyContainerSet saveActionSet(HubContext ctx, String name, List<PropertyContainer> actions);

    /**
     * Deletes an action set from the store.
     *
     * @param actionSetId an action set ID
     */
    void deleteActionSet(String actionSetId);

    void close();
}
