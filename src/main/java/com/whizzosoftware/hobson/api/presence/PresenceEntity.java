/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.presence;

/**
 * An interface that represents the current presence status of an entity.
 *
 * @author Dan Noguerol
 */
public interface PresenceEntity {
    public String getId();
    public String getName();
    public String getLocation();
    public Long getLastUpdate();
}
