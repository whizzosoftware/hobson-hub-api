package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.event.HobsonEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Dictionary;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * A thread that provides an event loop. This insures that only one thread serially calls HobsonPlugin methods so
 * plugin developers don't need to worry about concurrent programming issues.
 *
 * @author Dan Noguerol
 */
public class PluginEventLoop extends Thread {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PluginEventLoopListener listener;
    private State state = State.AWAITING_INITIAL_PLUGIN_CONFIG;
    /**
     * How frequently the listener's refresh() method should be called.
     */
    private long refreshIntervalInSeconds;
    /**
     * The last time an iteration was run.
     */
    private long lastRunTime;
    /**
     * Deque used to hold state transitions. These are processed immediately.
     */
    private final BlockingDeque<State> stateDeque = new LinkedBlockingDeque<State>();
    /**
     * Queue to hold pending plugin configuration changes. These are processed when the PLUGIN_CONFIG_CHANGED state is executed.
     */
    private final Deque<Dictionary> pluginConfigQueue = new ArrayDeque<Dictionary>();
    /**
     * Queue to hold pending device configuration changes. There are processed when the DEVICE_CONFIG_CHANGED state is executed.
     */
    private final Deque<DeviceConfigurationUpdate> deviceConfigQueue = new ArrayDeque<DeviceConfigurationUpdate>();
    /**
     * Queue to hold pending events. These are processed when the EVENT_PENDING state is executed.
     */
    private final Deque<HobsonEvent> eventQueue = new ArrayDeque<HobsonEvent>();

    /**
     * Constructor.
     *
     * @param listener a listener for event loop events
     * @param refreshIntervalInSeconds the refresh interval in seconds (0 == wait indefinitely, -1 == don't wait at all)
     */
    public PluginEventLoop(PluginEventLoopListener listener, long refreshIntervalInSeconds) {
        super();
        this.listener = listener;
        this.refreshIntervalInSeconds = refreshIntervalInSeconds;
    }

    public void cancel() {
        pushNextState(State.STOPPING);
    }

    public void run() {
        logger.debug("Event loop thread is starting");

        try {
            while (!isInterrupted() && state != State.STOPPED) {
                long now = System.currentTimeMillis();

                // perform one event loop iteration
                runOneIteration(now);
            }
        } catch (InterruptedException e) {
            logger.warn("Event loop thread was interrupted");
        }

        logger.debug("Event loop thread is ending");
    }

    protected State getEventLoopState() {
        return state;
    }

    protected void setEventLoopState(State state) {
        this.state = state;
    }

    protected void runOneIteration(long now) throws InterruptedException {
        runOneIteration(now, true);
    }

    protected void runOneIteration(long now, boolean waitForNextState) throws InterruptedException {
        // execute the appropriate state
        try {
            switch (state) {
                case AWAITING_INITIAL_PLUGIN_CONFIG:
                    fireAwaitingInitialPluginConfig();
                    break;
                case INITIALIZING:
                    fireInitializing();
                    break;
                case RUNNING:
                    fireRefresh(now);
                    break;
                case PENDING_PLUGIN_CONFIG:
                    firePluginConfigChange();
                    break;
                case PENDING_DEVICE_CONFIG:
                    fireDeviceConfigChange();
                    break;
                case PENDING_EVENT:
                    fireEvent();
                    break;
                case STOPPING:
                    fireStopping();
                    break;
            }
        } catch (Exception e) {
            // if an error occurred during initialization, stop the plugin
            if (state == State.INITIALIZING) {
                logger.error("Error occurred during plugin initialization; stopping", e);
                fireStopping();
            } else {
                logger.error("Error executing current state", e);
            }
        }

        lastRunTime = now;

        if (waitForNextState) {
            // wait for next state change (or timeout to iterate on the current one)
            long timeout = (lastRunTime + refreshIntervalInSeconds * 1000) - now;
            waitForNextStateChange(timeout);
        }
    }

    protected void waitForNextStateChange(long timeout) throws InterruptedException {
        // wait for next state change (or timeout to iterate on the current one)
        logger.trace("Event loop waiting {} ms for next state change", timeout);
        State newState = popState(timeout);
        if (newState != null) {
            logger.trace("Changing state to {} " + newState);
            setEventLoopState(newState);
        }
    }

    public void fireAwaitingInitialPluginConfig() {
        if (hasPendingPluginConfigChange()) {
            pushNextState(State.INITIALIZING);
        }
    }

    public void fireInitializing() {
        listener.onEventLoopInitializing(pluginConfigQueue.pop());
        if (hasPendingPluginConfigChange()) {
            pushNextState(State.PENDING_PLUGIN_CONFIG);
        } else if (hasPendingDeviceConfigChange()) {
            pushNextState(State.PENDING_DEVICE_CONFIG);
        } else {
            pushNextState(State.RUNNING);
        }
    }

    public void fireRefresh(long now) {
        // call refresh if sufficient time has elapsed
        if (refreshIntervalInSeconds <= 0 || (refreshIntervalInSeconds > 0 && ((now - lastRunTime) >= (refreshIntervalInSeconds * 1000)))) {
            listener.onEventLoopRefresh();
        } else {
            logger.trace("Ignoring refresh since refresh interval hasn't elapsed");
        }

        // if there are pending items, process them
        if (hasPendingEvent()) {
            pushNextState(State.PENDING_EVENT);
        } else if (hasPendingPluginConfigChange()) {
            pushNextState(State.PENDING_PLUGIN_CONFIG);
        } else if (hasPendingDeviceConfigChange()) {
            pushNextState(State.PENDING_DEVICE_CONFIG);
        }
    }

    public void firePluginConfigChange() {
        while (hasPendingPluginConfigChange()) {
            listener.onEventLoopPluginConfigChange(pluginConfigQueue.pop());
        }
        pushNextState(State.RUNNING);
    }

    public void fireDeviceConfigChange() {
        while (hasPendingDeviceConfigChange()) {
            DeviceConfigurationUpdate dcu = deviceConfigQueue.pop();
            listener.onEventLoopDeviceConfigChange(dcu.getDeviceId(), dcu.getConfiguration());
        }
        pushNextState(State.RUNNING);
    }

    public void fireEvent() {
        // send all pending events to the listener
        HobsonEvent event = popEvent();
        while (event != null) {
            listener.onEventLoopEvent(event);
            event = popEvent();
        }

        pushNextState(State.RUNNING);
    }

    public void fireStopping() {
        listener.onEventLoopStop();
        listener = null;
        pushNextState(State.STOPPED);
    }

    /**
     * Called on OSGi configuration management thread when the plugin configuration changes.
     *
     * @param config the new configuration
     */
    public void onPluginConfigurationUpdate(Dictionary config) {
        if (state != State.STOPPING && state != State.STOPPED) {
            pushPluginConfigChange(config);
            if (state == State.PENDING_PLUGIN_CONFIG) {
                pushNextState(State.INITIALIZING);
            } else if (state == State.RUNNING) {
                pushNextState(State.PENDING_PLUGIN_CONFIG);
            }
        } else {
            logger.debug("Ignoring config change since listener is stopping or stopped");
        }
    }

    /**
     * Called on OSGi configuration management thread when the device configuration changes.
     *
     * @param deviceId the device ID
     * @param config the new configuration
     */
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config) {
        if (state != State.STOPPING && state != State.STOPPED) {
            pushDeviceConfigChange(deviceId, config);
            if (state == State.RUNNING) {
                pushNextState(State.PENDING_DEVICE_CONFIG);
            }
        } else {
            logger.debug("Ignoring config change since listener is stopping or stopped");
        }
    }

    /**
     * Called on OSGi event admin dispatcher thread when an event occurs.
     *
     * @param event the event
     */
    public void onHobsonEvent(HobsonEvent event) {
        if (state != State.STOPPING && state != State.STOPPED) {
            pushNextEvent(event);
            if (state == State.RUNNING) {
                pushNextState(State.PENDING_EVENT);
            }
        } else {
            logger.debug("Ignoring event since listener is stopping or stopped: " + event);
        }
    }

    protected State popState(long timeoutInMs) throws InterruptedException {
        // if we have a refresh interval (and hence should timeout) we use the poll() operation
        if (timeoutInMs > 0) {
            long timeoutInS = timeoutInMs / 1000;
            logger.trace("Waiting for state change for {} seconds", timeoutInS);
            return stateDeque.poll(timeoutInS, TimeUnit.SECONDS);
        } else if (timeoutInMs < 0) {
            return stateDeque.poll(0, TimeUnit.SECONDS);
            // if we don't have a refresh interval (and hence don't need to timeout) we use the take() operation which blocks
            // until an item is added to the queue
        } else {
            logger.trace("Waiting indefinitely for state change");
            return stateDeque.take();
        }
    }

    private void pushNextState(State state) {
        synchronized (stateDeque) {
            stateDeque.push(state);
        }
    }

    private void pushPluginConfigChange(Dictionary config) {
        synchronized (pluginConfigQueue) {
            pluginConfigQueue.push(config);
        }
    }

    private boolean hasPendingPluginConfigChange() {
        return (pluginConfigQueue.size() > 0);
    }

    private void pushDeviceConfigChange(String deviceId, Dictionary config) {
        synchronized (deviceConfigQueue) {
            deviceConfigQueue.push(new DeviceConfigurationUpdate(deviceId, config));
        }
    }

    private boolean hasPendingDeviceConfigChange() {
        return (deviceConfigQueue.size() > 0);
    }

    private void pushNextEvent(HobsonEvent event) {
        eventQueue.push(event);
    }

    protected boolean hasPendingEvent() {
        return (eventQueue.size() > 0);
    }

    private HobsonEvent popEvent() {
        return eventQueue.poll();
    }

    /**
     * The event loop states.
     */
    protected enum State {
        AWAITING_INITIAL_PLUGIN_CONFIG,
        INITIALIZING,
        RUNNING,
        PENDING_PLUGIN_CONFIG,
        PENDING_DEVICE_CONFIG,
        PENDING_EVENT,
        STOPPING,
        STOPPED
    }
}
