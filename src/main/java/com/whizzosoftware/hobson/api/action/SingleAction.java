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

import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an action that does a single operation.
 *
 * @author Dan Noguerol
 */
abstract public class SingleAction implements Action {
    private final static Logger logger = LoggerFactory.getLogger(SingleAction.class);

    private PluginContext pluginContext;
    private ActionExecutionContext executionContext;
    private EventLoopExecutor executor;

    /**
     * Constructor.
     *
     * @param executionContext an ActionExecutionContext implementation
     */
    public SingleAction(PluginContext pluginContext, ActionExecutionContext executionContext, EventLoopExecutor executor) {
        this.pluginContext = pluginContext;
        this.executionContext = executionContext;
        this.executor = executor;
    }

    @Override
    public boolean isAssociatedWithPlugin(PluginContext ctx) {
        return pluginContext.equals(ctx);
    }

    /**
     * Callback received when the action is started.
     */
    abstract public void onStart(ActionLifecycleContext ctx);

    /**
     * Callback received when the action is sent a message.
     *
     * @param msgName the name of the event
     * @param prop a property object (specific to the event)
     */
    abstract public void onMessage(ActionLifecycleContext ctx, String msgName, Object prop);

    /**
     * Callback received when the action is manually stopped.
     */
    abstract public void onStop(ActionLifecycleContext ctx);

    protected ActionExecutionContext getContext() {
        return executionContext;
    }

    @Override
    public Future sendMessage(final ActionLifecycleContext ctx, final String msgName, final Object prop) {
        return executor.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                logger.trace("Action {} received message: ", getClass().getName(), msgName);
                onMessage(ctx, msgName, prop);
            }
        });
    }

    @Override
    public Future start(final ActionLifecycleContext ctx) {
        return executor.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                logger.trace("Starting single action: {}", getClass().getName());
                onStart(ctx);
            }
        });
    }

    @Override
    public Future stop(final ActionLifecycleContext ctx) {
        return executor.executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                logger.trace("Stopping single action: {}", getClass().getName());
                onStop(ctx);
            }
        });
    }
}
