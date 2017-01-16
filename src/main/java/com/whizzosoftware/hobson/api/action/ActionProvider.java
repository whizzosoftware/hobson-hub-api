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

import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;

import java.util.Map;

abstract public class ActionProvider extends ActionClass {
    /**
     * Constructor.
     *
     * @param context the container class context
     * @param name the name of the action
     * @param description a description of the action
     * @param taskAction should this action be used for tasks?
     * @param timeoutInterval the maximum interval (in ms) the action is allowed to run before it is timed out
     */
    public ActionProvider(PropertyContainerClassContext context, String name, String description, boolean taskAction, long timeoutInterval) {
        super(context, name, description, taskAction, timeoutInterval);
    }

    /**
     * Creates a new action.
     *
     * @param properties the property values for this action execution
     *
     * @return a new Action instance
     */
    abstract public Action createAction(Map<String,Object> properties);
}
