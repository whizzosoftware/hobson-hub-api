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

import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.property.*;

/**
 * Represents the definition of an action that can be published by plugins/devices and invoked by clients.
 *
 * @param <T> a implementation of ActionExecutionContext that the instantiated action will use to perform its work
 * @author Dan Noguerol
 */
public abstract class ActionClass<T extends ActionExecutionContext> extends PropertyContainerClass {
    private String description;
    private String name;
    private boolean taskAction;
    private long timeoutInterval;

    /**
     * Constructor.
     *
     * @param context
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTaskAction() {
        return taskAction;
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
     * Creates a new instance of this action.
     *
     * @param ctx the ActionExecutionContext to use
     * @param properties the property values for this action execution
     * @param executor the EventLoopExecutor to use for action execution
     *
     * @return an Action instance
     */
    abstract public Action newInstance(T ctx, PropertyContainer properties, EventLoopExecutor executor);
}
