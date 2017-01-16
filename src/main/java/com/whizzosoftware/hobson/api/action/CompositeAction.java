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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Represents an action comprised of other actions. The action is considered complete when all its
 * sub-actions are.
 *
 * @author Dan Noguerol
 */
public class CompositeAction implements Action {
    private final static Logger logger = LoggerFactory.getLogger(CompositeAction.class);

    private final Deque<Action> actionQueue = new ConcurrentLinkedDeque<>();
    private Action currentAction;
    private ActionLifecycleContext parentContext;

    /**
     * Constructor.
     *
     * @param actions a list of actions that comprise the composite action
     */
    public CompositeAction(List<Action> actions) {
        actionQueue.addAll(actions);
    }

    @Override
    public boolean isAssociatedWithPlugin(PluginContext ctx) {
        for (Action a : actionQueue) {
            if (a.isAssociatedWithPlugin(ctx)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Future sendMessage(ActionLifecycleContext ctx, String msgName, Object prop) {
        logger.trace("Dispatching received message to {}: {}", currentAction.getClass().getName(), msgName);
        return currentAction.sendMessage(new CompositeActionLifecycleContext(), msgName, prop);
    }

    @Override
    public Future start(final ActionLifecycleContext ctx) {
        parentContext = ctx;
        return startNextAction();
    }

    private Future startNextAction() {
        if (actionQueue.size() > 0) {
            currentAction = actionQueue.pop();
            logger.trace("Starting next action: {}", currentAction.getClass().getName());
            return currentAction.start(new CompositeActionLifecycleContext());
        } else {
            logger.trace("No more actions to execute");
            parentContext.complete();
            return null;
        }
    }

    @Override
    public Future stop(ActionLifecycleContext ctx) {
        return currentAction.stop(ctx);
    }

    private class CompositeActionLifecycleContext implements ActionLifecycleContext {
        @Override
        public void complete() {
            startNextAction();
        }

        @Override
        public void complete(String msg) {
            parentContext.update(msg);
            startNextAction();
        }

        @Override
        public void fail(String msg) {
            parentContext.fail(msg);
        }

        @Override
        public void update(String msg) {
            parentContext.update(msg);
        }
    }
}
