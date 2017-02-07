/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.task.store;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.task.HobsonTask;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.TaskManager;

import java.util.Collection;

/**
 * An interface for a task storage mechanism.
 *
 * @author Dan Noguerol
 */
public interface TaskStore {
    /**
     * Closes the store and performs cleanup.
     */
    void close();

    /**
     * Deletes a task from the store.
     *
     * @param context a task context
     */
    void deleteTask(TaskContext context);

    /**
     * Returns all tasks in the store.
     *
     * @param hctx the hub context
     *
     * @return a Collection of HobsonTask instances (may be empty)
     */
    Collection<TaskContext> getAllTasks(HubContext hctx);

    /**
     * Returns all task in the store associated with a particular plugin.
     *
     * @param taskManager a TaskManager instance (used to identify trigger conditions)
     * @param pctx a plugin context
     *
     * @return a Collection of HobsonTask instances (may be empty)
     */
    Collection<HobsonTask> getAllTasks(TaskManager taskManager, PluginContext pctx);

    /**
     * Returns a particular task from the store.
     *
     * @param context a task context
     *
     * @return a HobsonTask instance (or null if not found)
     */
    HobsonTask getTask(TaskContext context);

    /**
     * Adds a task to the store.
     *
     * @param task the task to add
     *
     * @return the task state after it has been added to the store
     */
    HobsonTask saveTask(HobsonTask task);

    /**
     * Allows the task store to perform any implementation-specific housekeeping tasks.
     */
    void performHousekeeping();
}
