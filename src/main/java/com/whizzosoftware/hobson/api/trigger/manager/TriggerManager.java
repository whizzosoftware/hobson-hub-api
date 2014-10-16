/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.trigger.manager;

import com.whizzosoftware.hobson.api.trigger.HobsonTrigger;

import java.util.Collection;

/**
 * An interface for managing Hobson triggers.
 *
 * @author Dan Noguerol
 */
public interface TriggerManager {
    public Collection<HobsonTrigger> getAllTriggers();
    public HobsonTrigger getTrigger(String providerId, String triggerId);
    public void addTrigger(String providerId, Object trigger);
    public void deleteTrigger(String providerId, String triggerId);
}
