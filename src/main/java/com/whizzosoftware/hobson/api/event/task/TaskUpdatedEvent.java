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
 * An event that occurs when a task is updated.
 *
 * @author Dan Noguerol
 */
public class TaskUpdatedEvent extends TaskEvent {
    public static final String ID = "taskUpdated";

    private static final String PROP_TASK = "task";

    public TaskUpdatedEvent(long timestamp, TaskContext ctx) {
        super(timestamp, ID);
        setProperty(PROP_TASK, ctx);
    }

    public TaskUpdatedEvent(Map<String,Object> properties) {
        super(properties);
    }

    public TaskContext getTask() {
        return (TaskContext)getProperty(PROP_TASK);
    }
}
