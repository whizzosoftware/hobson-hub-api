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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompositeActionTest {
    @Test
    public void testStart() throws Exception {
        List<Action> actions = new ArrayList<>();
        final MockSignalAction action1 = new MockSignalAction();
        final MockSignalAction action2 = new MockSignalAction();
        actions.add(action1);
        actions.add(action2);

        final CompositeAction ca = new CompositeAction(actions);
        Future f = ca.start(new MockActionLifecycleContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ca.sendMessage(new MockActionLifecycleContext(), "foo", null).syncUninterruptibly();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                action1.finishStart();
            }
        }).start();
        assertFalse(action1.hasMessages());
        assertFalse(action2.hasMessages());
        f.syncUninterruptibly();
        assertTrue(action1.wasStartCalled());
        assertTrue(action1.hasMessages());
        assertFalse(action2.hasMessages());
        Thread.sleep(1000);
        assertTrue(action1.wasStartCompleted());
        assertTrue(action2.wasStartCalled());
        action2.finishStart();
        Thread.sleep(1000);
        assertTrue(action2.wasStartCompleted());
    }

    private class MockSignalAction implements Action {
        private EventLoopGroup eventLoop = new LocalEventLoopGroup(2);
        private final Object mutex = new Object();
        private final List<String> messages = new ArrayList<>();
        private boolean startCalled;
        private boolean startCompleted;

        @Override
        public boolean isAssociatedWithPlugin(PluginContext ctx) {
            return false;
        }

        @Override
        public Future sendMessage(ActionLifecycleContext ctx, final String msgName, Object prop) {
            return eventLoop.submit(new Runnable() {
                @Override
                public void run() {
                    messages.add(msgName);
                }
            });
        }

        public boolean hasMessages() {
            return messages.size() > 0;
        }

        @Override
        public Future start(final ActionLifecycleContext ctx) {
            startCalled = true;
            return eventLoop.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (mutex) {
                            mutex.wait();
                        }
                        ctx.complete();
                        startCompleted = true;
                    } catch (InterruptedException ignored) {}
                }
            });
        }

        @Override
        public Future stop(ActionLifecycleContext ctx) {
            return null;
        }

        public boolean wasStartCalled() {
            return startCalled;
        }

        public boolean wasStartCompleted() {
            return startCompleted;
        }

        public void finishStart() {
            synchronized (mutex) {
                mutex.notify();
            }
        }
    }
}
