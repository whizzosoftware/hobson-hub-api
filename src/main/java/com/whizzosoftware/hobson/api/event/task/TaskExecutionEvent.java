/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event.task;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Map;

/**
 * Event that occurs when a task is executed.
 *
 * @author Dan Noguerol
 */
public class TaskExecutionEvent extends TaskEvent {
    public static final String ID = "taskExecute";

    private static final String PROP_CONTEXT = "context";
    private static final String PROP_ERROR = "error";

    public TaskExecutionEvent(long timestamp, TaskContext context, Throwable error) {
        super(timestamp, ID);
        setProperty(PROP_CONTEXT, context);
        if (error != null) {
            setProperty(PROP_ERROR, error);
        }
    }

    public TaskExecutionEvent(Map<String,Object> properties) {
        super(properties);
    }

    public TaskContext getContext() {
        return (TaskContext)getProperty(PROP_CONTEXT);
    }

    public boolean hasError() {
        return (getProperty(PROP_ERROR) != null);
    }

    public Throwable getError() {
        return (Throwable)getProperty(PROP_ERROR);
    }
}
