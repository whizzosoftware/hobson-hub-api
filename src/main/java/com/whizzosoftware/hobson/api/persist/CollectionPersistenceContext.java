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
     * Retrieves a named map.
     *
     * @param key the map key
     *
     * @return a Map instance
     */
    Map<String,Object> getMap(String key);

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
     * Commits changes to maps.
     */
    void commit();
}
