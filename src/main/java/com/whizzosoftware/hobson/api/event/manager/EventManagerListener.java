/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.manager;

import com.whizzosoftware.hobson.api.event.HobsonEvent;

/**
 * An interface for classes that want to receive event manager events.
 *
 * @author Dan Noguerol
 */
public interface EventManagerListener {
    /**
     * Callback when a HobsonEvent is received.
     *
     * @param event the received event
     */
    public void onHobsonEvent(HobsonEvent event);
}
