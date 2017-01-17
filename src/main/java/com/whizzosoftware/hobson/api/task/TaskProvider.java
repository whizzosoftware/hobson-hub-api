/*
 *******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.task;

import java.util.Collection;

/**
 * Interface for plugins that can contribute tasks to the system.
 *
 * @author Dan Noguerol
 */
public interface TaskProvider {
    /**
     * Callback when tasks are registered during plugin startup or added later.
     *
     * @param tasks the tasks that have been created
     */
    void onRegisterTasks(Collection<TaskContext> tasks);

    /**
     * Callback when a task is updated.
     *
     * @param ctx an existing task to update
     */
    void onUpdateTask(TaskContext ctx);

    /**
     * Callback when a task is deleted.
     *
     * @param ctx the context of the task to remove
     */
    void onDeleteTask(TaskContext ctx);
}
