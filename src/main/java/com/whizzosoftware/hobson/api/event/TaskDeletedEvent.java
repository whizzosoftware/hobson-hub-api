/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Map;

/**
 * An event that occurs when a task is updated.
 *
 * @author Dan Noguerol
 */
public class TaskDeletedEvent extends HobsonEvent {
    public static final String ID = "taskDeleted";

    private static final String PROP_CONTEXT = "context";

    public TaskDeletedEvent(long timestamp, TaskContext context) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
        setProperty(PROP_CONTEXT, context);
    }

    public TaskDeletedEvent(Map<String,Object> properties) {
        super(EventTopics.STATE_TOPIC, properties);
    }

    public TaskContext getContext() {
        return (TaskContext)getProperty(PROP_CONTEXT);
    }
}
