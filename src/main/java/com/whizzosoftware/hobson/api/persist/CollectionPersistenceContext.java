/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

import java.util.Map;

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
    public Map<String,String> getMap(String key);

    /**
     * Commits changes to maps.
     */
    public void commit();
}
