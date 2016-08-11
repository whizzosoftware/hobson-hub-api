/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import io.netty.util.concurrent.Future;

/**
 * An interface that allows the invocation of code from within an event loop thread.
 *
 * @author Dan Noguerol
 */
public interface EventLoopExecutor {
    /**
     * Execute a task using an event loop. Note that this will tie up the event loop while the task is being
     * executed so this should not be used for long running tasks.
     *
     * @param runnable a task to execute
     */
    Future executeInEventLoop(Runnable runnable);
}
