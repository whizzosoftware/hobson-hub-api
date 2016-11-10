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

import com.whizzosoftware.hobson.api.property.*;

/**
 * Represents the definition of an action that can be published by plugins/devices and invoked by clients.
 *
 * @author Dan Noguerol
 */
public class ActionClass extends PropertyContainerClass {
    private String description;
    private String name;
    private boolean taskAction;
    private long timeoutInterval;

    /**
     * Constructor.
     * @param context the container class context
     * @param name the name of the action
     * @param description a description of the action
     * @param taskAction should this action be used for tasks?
     * @param timeoutInterval the maximum interval (in ms) the action is allowed to run before it is timed out
     */
    public ActionClass(PropertyContainerClassContext context, String name, String description, boolean taskAction, long timeoutInterval) {
        super(context, PropertyContainerClassType.ACTION);
        this.name = name;
        this.description = description;
        this.taskAction = taskAction;
        this.timeoutInterval = timeoutInterval;
    }

    /**
     * Returns the action class description.
     *
     * @return a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the action class name.
     *
     * @return a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the timeout interval for the action.
     *
     * @return a long
     */
    public long getTimeoutInterval() {
        return timeoutInterval;
    }

    /**
     * Indicates whether this action class can be included in tasks.
     *
     * @return a boolean
     */
    public boolean isTaskAction() {
        return taskAction;
    }
}
