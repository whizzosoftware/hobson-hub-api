/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A context used when persisting collections using the CollectionPersister class.
 *
 * @author Dan Noguerol
 */
public interface CollectionPersistenceContext {
    /**
     * Retrieves a named map.
     *
     * @param key the map key
     *
     * @return a Map instance
     */
    Map<String,Object> getMap(String key);

    /**
     * Retrieves a list of maps that start with a key prefix.
     *
     * @param keyPrefix the key prefix
     *
     * @return a List of Map instances
     */
    List<Map<String,Object>> getMapsWithPrefix(String keyPrefix);

    /**
     * Sets a named map.
     *
     * @param key the map key
     * @param map the Map instance
     */
    void setMap(String key, Map<String,Object> map);

    /**
     * Returns the key set for all named maps.
     *
     * @return a set of String keys
     */
    Set<String> getKeySet();

    /**
     * Returns the PropertyContainerClass for a specific context.
     *
     * @param ctx the context
     *
     * @return a PropertyContainerClass instance (or null if not found)
     */
    PropertyContainerClass getPropertyContainerClass(PropertyContainerClassContext ctx);

    /**
     * Commits changes to maps.
     */
    void commit();
}
