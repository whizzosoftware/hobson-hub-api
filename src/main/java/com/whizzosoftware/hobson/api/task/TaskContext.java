/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

/**
 * A class that encapsulates the fully-qualified context of a task.
 *
 * @author Dan Noguerol
 */
public class TaskContext {
    private PluginContext ctx;
    private String taskId;

    public static TaskContext create(HubContext ctx, String pluginId, String taskId) {
        return create(PluginContext.create(ctx, pluginId), taskId);
    }

    public static TaskContext create(PluginContext ctx, String taskId) {
        return new TaskContext(ctx, taskId);
    }

    public static TaskContext createLocal(String pluginId, String taskId) {
        return new TaskContext(PluginContext.createLocal(pluginId), taskId);
    }

    private TaskContext(PluginContext ctx, String taskId) {
        this.ctx = ctx;
        this.taskId = taskId;
    }

    public PluginContext getPluginContext() {
        return ctx;
    }

    public String getHubId() {
        return ctx.getHubId();
    }

    public String getUserId() {
        return ctx.getUserId();
    }

    public String getPluginId() {
        return ctx.getPluginId();
    }

    public String getTaskId() {
        return taskId;
    }

    public String toString() {
        return ctx.toString() + "." + taskId;
    }
}
