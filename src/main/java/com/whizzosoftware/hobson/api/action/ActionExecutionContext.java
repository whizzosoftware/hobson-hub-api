/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.action;

import java.util.Map;

/**
 * An ActionExecutionContext implementation is passed to instantiated Action objects in order for them to perform
 * work. These are generally action-specific implementations.
 *
 * @author Dan Noguerol
 */
public interface ActionExecutionContext {
    /**
     * Returns the set of properties associated with the action execution.
     *
     * @return a map of properties
     */
    Map<String,Object> getProperties();
}
