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

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import io.netty.util.concurrent.Future;

/**
 * Represents an action created by an ActionProvider. Actions run within the context of Job objects
 * managed by the ActionManager.
 *
 * @author Dan Noguerol
 */
public interface Action {
    /**
     * Indicates whether this action is associated with a particular plugin.
     *
     * @param ctx the plugin context
     *
     * @return a boolean
     */
    boolean isAssociatedWithPlugin(PluginContext ctx);

    /**
     * Sends a message to the action.
     *
     * @param ctx the lifecycle context
     * @param msgName the name of the message
     * @param prop a property object
     *
     * @return a Future
     */
    Future sendMessage(final ActionLifecycleContext ctx, final String msgName, final Object prop);

    /**
     * Starts the action.
     *
     * @param ctx the lifecycle context
     *
     * @return a Future
     */
    Future start(final ActionLifecycleContext ctx);

    /**
     * Stops the action.
     *
     * @param ctx the lifecycle context
     *
     * @return a Future
     */
    Future stop(final ActionLifecycleContext ctx);
}
