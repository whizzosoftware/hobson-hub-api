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

public interface ActionProvider {
    /**
     * Creates a new action.
     *
     * @param properties the property values for this action execution
     *
     * @return a new Action instance
     */
    Action createAction(Map<String,Object> properties);

    /**
     * Returns the action class this provider can instantiate actions for.
     *
     * @return an ActionClass instance
     */
    ActionClass getActionClass();
}
