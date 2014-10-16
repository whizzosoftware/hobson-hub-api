/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.event.VariableUpdateRequestEvent;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Dictionary;
import java.util.Hashtable;

public class PluginEventLoopTest {
    @Test
    public void testAwaitingPluginConfigurationWithNoConfig() throws Exception {
        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, -1);
        assertEquals(PluginEventLoop.State.AWAITING_INITIAL_PLUGIN_CONFIG, loop.getEventLoopState());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(PluginEventLoop.State.AWAITING_INITIAL_PLUGIN_CONFIG, loop.getEventLoopState());
    }

    @Test
    public void testTransitionAwaitingPluginConfigurationToRunning() throws Exception {
        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, -1);
        assertEquals(PluginEventLoop.State.AWAITING_INITIAL_PLUGIN_CONFIG, loop.getEventLoopState());
        loop.onPluginConfigurationUpdate(new Hashtable());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(PluginEventLoop.State.INITIALIZING, loop.getEventLoopState());
        assertEquals(0, listener.getInitCalls());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(1, listener.getInitCalls());
        assertEquals(0, listener.getRefreshCalls());
        assertEquals(PluginEventLoop.State.RUNNING, loop.getEventLoopState());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(1, listener.getRefreshCalls());
    }

    @Test
    public void testTransitionRunningToEventPending() throws Exception {
        long baseTime = System.currentTimeMillis();

        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, -1);
        loop.setEventLoopState(PluginEventLoop.State.RUNNING);

        assertEquals(0, listener.getRefreshCalls());
        assertEquals(0, listener.getEventCalls());
        loop.runOneIteration(baseTime);
        assertEquals(1, listener.getRefreshCalls());
        assertEquals(0, listener.getEventCalls());

        listener.clearAllCalls();
        assertFalse(loop.hasPendingEvent());
        loop.onHobsonEvent(new VariableUpdateRequestEvent("p", "d", "n", "v"));
        assertTrue(loop.hasPendingEvent());
        loop.runOneIteration(baseTime + 5001);
        assertEquals(1, listener.getRefreshCalls());
        assertEquals(0, listener.getEventCalls());
        assertEquals(PluginEventLoop.State.PENDING_EVENT, loop.getEventLoopState());

        listener.clearAllCalls();
        assertEquals(0, listener.getRefreshCalls());
        assertEquals(0, listener.getEventCalls());
        loop.runOneIteration(baseTime + 5001);
        assertEquals(0, listener.getRefreshCalls());
        assertEquals(1, listener.getEventCalls());
        assertEquals(PluginEventLoop.State.RUNNING, loop.getEventLoopState());
    }

    @Test
    public void testTransitionRunningToConfigChange() throws Exception {
        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, -1);
        loop.setEventLoopState(PluginEventLoop.State.RUNNING);

        assertEquals(0, listener.getRefreshCalls());
        assertEquals(0, listener.getConfigChangeCalls());
        loop.onPluginConfigurationUpdate(new Hashtable());
        assertEquals(0, listener.getConfigChangeCalls());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(PluginEventLoop.State.PENDING_PLUGIN_CONFIG, loop.getEventLoopState());
        assertEquals(1, listener.getRefreshCalls());
        assertEquals(0, listener.getConfigChangeCalls());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(1, listener.getRefreshCalls());
        assertEquals(1, listener.getConfigChangeCalls());
        assertEquals(PluginEventLoop.State.RUNNING, loop.getEventLoopState());
    }

    @Test
    public void testRunOneIterationWithOneSecondTimeout() throws Exception {
        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, 1);
        loop.setEventLoopState(PluginEventLoop.State.RUNNING);

        long baseTime = System.currentTimeMillis();
        loop.runOneIteration(baseTime);
        long now = System.currentTimeMillis();
        assertTrue(now - baseTime >= 800);
    }

    @Test
    public void testRunBeforeRefreshIntervalHasElapsed() throws Exception {
        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, 5);
        loop.setEventLoopState(PluginEventLoop.State.RUNNING);

        assertEquals(0, listener.getRefreshCalls());
        long baseTime = System.currentTimeMillis();
        loop.runOneIteration(baseTime, false);
        assertEquals(1, listener.getRefreshCalls());
        loop.runOneIteration(baseTime + 1000, false);
        assertEquals(1, listener.getRefreshCalls());
    }

    @Test
    public void testEventLoopTermination() throws Exception {
        MockEventLoopListener listener = new MockEventLoopListener();
        PluginEventLoop loop = new PluginEventLoop(listener, -1);
        loop.setEventLoopState(PluginEventLoop.State.RUNNING);
        loop.cancel();
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(0, listener.getStopCalls());
        assertEquals(PluginEventLoop.State.STOPPING, loop.getEventLoopState());
        loop.runOneIteration(System.currentTimeMillis());
        assertEquals(1, listener.getStopCalls());
        assertEquals(PluginEventLoop.State.STOPPED, loop.getEventLoopState());
    }

    private class MockEventLoopListener implements PluginEventLoopListener {
        private int initCalls;
        private int refreshCalls;
        private int stopCalls;
        private int configChangeCalls;
        private int eventCalls;

        @Override
        public void onEventLoopInitializing(Dictionary config) {
            initCalls++;
        }

        @Override
        public void onEventLoopRefresh() {
            refreshCalls++;
        }

        @Override
        public void onEventLoopStop() {
            stopCalls++;
        }

        @Override
        public void onEventLoopPluginConfigChange(Dictionary config) {
            configChangeCalls++;
        }

        @Override
        public void onEventLoopDeviceConfigChange(String deviceId, Dictionary config) {}

        @Override
        public void onEventLoopEvent(HobsonEvent event) {
            eventCalls++;
        }

        public int getInitCalls() {
            return initCalls;
        }

        public int getRefreshCalls() {
            return refreshCalls;
        }

        public int getStopCalls() {
            return stopCalls;
        }

        public int getConfigChangeCalls() {
            return configChangeCalls;
        }

        public int getEventCalls() {
            return eventCalls;
        }

        public void clearAllCalls() {
            initCalls = 0;
            refreshCalls = 0;
            stopCalls = 0;
            configChangeCalls = 0;
            eventCalls = 0;
        }
    }
}
