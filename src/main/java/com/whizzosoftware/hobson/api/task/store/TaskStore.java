package com.whizzosoftware.hobson.api.task.store;
/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;
import com.whizzosoftware.hobson.api.task.HobsonTask;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.TaskManager;

import java.util.Collection;
import java.util.List;

/**
 * An interface for a task storage mechanism.
 *
 * @author Dan Noguerol
 */
public interface TaskStore {
    /**
     * Returns all tasks in the store.
     *
     * @return a Collection of HobsonTask instances (may be empty)
     */
    Collection<HobsonTask> getAllTasks();

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
     * Deletes a task from the store.
     *
     * @param context a task context
     */
    void deleteTask(TaskContext context);

    /**
     * Returns all action sets in the store.
     *
     * @param ctx a hub context
     *
     * @return a Collection of PropertyContainerSet instances representing the actions
     */
    Collection<PropertyContainerSet> getAllActionSets(HubContext ctx);

    /**
     * Returns a particular action set from the store.
     *
     * @param ctx a hub context
     * @param actionSetId an action set ID
     *
     * @return a PropertyContainerSet instance
     */
    PropertyContainerSet getActionSet(HubContext ctx, String actionSetId);

    /**
     * Adds an action set to the store.
     *
     * @param ctx a hub context
     * @param name the name of the action set
     * @param actions the actions that comprise the set
     *
     * @return a PropertyContainerSet instance representing the new action set
     */
    PropertyContainerSet addActionSet(HubContext ctx, String name, List<PropertyContainer> actions);

    /**
     * Deletes an action set from the store.
     *
     * @param actionSetId an action set ID
     */
    void deleteActionSet(String actionSetId);

    /**
     * Closes the store and performs cleanup.
     */
    void close();
}
