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
 * A manager interface for entity presence.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.7
 */
public interface PresenceManager {
    public Collection<PresenceEntity> getEntities();
    public void firePresenceUpdate(String entityId, String location);
}
