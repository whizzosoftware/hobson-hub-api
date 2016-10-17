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

import com.whizzosoftware.hobson.api.job.JobContext;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;

/**
 * Represents an action instance created from an ActionClass. Actions run within the context of Job objects
 * managed by the JobManager.
 *
 * @param <T> an implementation of ActionExecutionContext that used by the action to perform its tasks
 * @author Dan Noguerol
 */
abstract public class Action<T extends ActionExecutionContext> {
    private T context;
    private EventLoopExecutor executor;

    /**
     * Constructor.
     *
     * @param ctx an ActionExecutionContext implementation
     * @param executor the EventLoopExecutor to be used for all action execution
     */
    public Action(T ctx, EventLoopExecutor executor) {
        this.context = ctx;
        this.executor = executor;
    }

    /**
     * Returns the EventLoopExecutor associated with this action and which should be used for all action execution.
     *
     * @return an EventLoopExecutor
     */
    public EventLoopExecutor getEventLoopExecutor() {
        return executor;
    }

    /**
     * Returns an action-specific context.
     *
     * @return the ActionExecutionContext implementation
     */
    protected T getContext() {
        return context;
    }

    /**
     * Callback received when the action is started.
     *
     * @param ctx the context of the job the task is running within
     */
    abstract public void onStart(JobContext ctx);

    /**
     * Callback received when the action is sent an event.
     *
     * @param ctx the context of the job the task is running within
     * @param name the name of the event
     * @param prop a property object (specific to the event)
     */
    abstract public void onEvent(JobContext ctx, String name, Object prop);

    /**
     * Callback received when the action is manually stopped.
     *
     * @param ctx the context of the job the task is running within
     */
    abstract public void onStop(JobContext ctx);
}
