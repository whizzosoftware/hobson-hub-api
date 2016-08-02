/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.hub.HubContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * A class that encapsulates the fully-qualified context of a task.
 *
 * @author Dan Noguerol
 */
public class TaskContext implements Serializable {
    private final HubContext hubContext;
    private final String taskId;

    public static TaskContext create(HubContext ctx, String taskId) {
        return new TaskContext(ctx, taskId);
    }

    public static TaskContext createLocal(String taskId) {
        return new TaskContext(HubContext.createLocal(), taskId);
    }

    public static TaskContext create(String s) {
        String[] comps = StringUtils.split(s, HubContext.DELIMITER);
        return TaskContext.create(HubContext.create(comps[0]), comps[1]);
    }

    private TaskContext(HubContext hubContext, String taskId) {
        this.hubContext = hubContext;
        this.taskId = taskId;
    }

    public HubContext getHubContext() {
        return hubContext;
    }

    public String getHubId() {
        return hubContext.getHubId();
    }

    public boolean hasTaskId() {
        return (taskId != null);
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean equals(Object o) {
        return (
            o instanceof TaskContext &&
                ((TaskContext)o).hubContext.equals(hubContext) &&
                ((TaskContext)o).taskId.equals(taskId)
        );
    }

    public int hashCode() {
        return new HashCodeBuilder().append(hubContext).append(taskId).toHashCode();
    }

    public String toString() {
        return hubContext.toString() + HubContext.DELIMITER + taskId;
    }
}
