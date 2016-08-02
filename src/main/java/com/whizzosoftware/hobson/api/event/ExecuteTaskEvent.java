/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Map;

/**
 * Event that requests a task be manually executed.
 *
 * @author Dan Noguerol
 */
public class ExecuteTaskEvent extends HobsonEvent {
    public static final String ID = "executeTask";

    private static final String PROP_TASK = "task";

    public ExecuteTaskEvent(long timestamp, TaskContext context) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
        setProperty(PROP_TASK, context);
    }

    public ExecuteTaskEvent(Map<String,Object> props) {
        super(EventTopics.STATE_TOPIC, props);
    }

    public TaskContext getTask() {
        return (TaskContext)getProperty(PROP_TASK);
    }
}
