/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.executor;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A manager interface for both scheduled and immediate tasks. This will be backed by a thread pool and is intended
 * for short-running, asynchronous tasks. This is primarily used by the manager implementations to perform device
 * availability monitoring and housekeeping tasks and prevents the individual managers from creating their own
 * threads and consuming more resources than necessary.
 *
 * @author Dan Noguerol
 */
public interface ExecutorManager {
    /**
     * Schedule a task to run in the future at a recurring interval.
     *
     * @param r the Runnable task to execute
     * @param initialDelay the initial delay in time units
     * @param delay the delay between iterations in time units
     * @param unit the unit of time
     *
     * @return a Future that can be used to get the status of and cancel the task
     */
    Future schedule(Runnable r, long initialDelay, long delay, TimeUnit unit);

    /**
     * Submit a task for immediate execution.
     *
     * @param r the Runnable task to execute
     *
     * @return a Future that can be used to get the status of the task
     */
    Future submit(Runnable r);

    /**
     * Cancel a previously scheduled task.
     *
     * @param f a Future returned from a call to schedule()
     */
    void cancel(Future f);
}
