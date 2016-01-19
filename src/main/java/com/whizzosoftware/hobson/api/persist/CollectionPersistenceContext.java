/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

import java.util.Map;
import java.util.Set;

/**
 * A context used when persisting collections using the CollectionPersister class.
 *
 * @author Dan Noguerol
 */
public interface CollectionPersistenceContext {
    /**
     * Adds a value to a set (creating it if it doesn't already exist).
     *
     * @param key the key referencing the set
     * @param value the value to add
     */
    void addSetValue(String key, Object value);

    /**
     * Retrieves a named map.
     *
     * @param key the map key
     *
     * @return a Map instance
     */
    Map<String,Object> getMap(String key);

    /**
     * Retrieves a value from within a named map.
     *
     * @param key the map key
     * @param name the name of the property to retrieve
     *
     * @return an Object (or null if the property doesn't exist)
     */
    Object getMapValue(String key, String name);

    /**
     * Retrieves a named set.
     *
     * @param key the key referencing the set
     *
     * @return a Set instance
     */
    Set<Object> getSet(String key);

    /**
     * Indicates whether a given named map exists.
     *
     * @param key the key referencing the map
     *
     * @return a boolean
     */
    boolean hasMap(String key);

    /**
     * Indicates whether a given named set exists.
     *
     * @param key the key referencing the set
     *
     * @return a boolean
     */
    boolean hasSet(String key);

    /**
     * Sets a named map.
     *
     * @param key the key referencing the map
     * @param map the Map instance
     */
    void setMap(String key, Map<String,Object> map);

    /**
     * Sets a property within a named map.
     *
     * @param key the key referencing the map
     * @param name the name to set
     * @param value the value to set for the name
     */
    void setMapValue(String key, String name, Object value);

    void setSet(String key, Set<Object> set);

    /**
     * Removes a named map.
     *
     * @param key the key referencing the map
     */
    void remove(String key);

    /**
     * Removes a value from a named set.
     *
     * @param key the key referencing the set
     * @param value the value to remove
     */
    void removeFromSet(String key, Object value);

    /**
     * Commits changes to maps.
     */
    void commit();
}
