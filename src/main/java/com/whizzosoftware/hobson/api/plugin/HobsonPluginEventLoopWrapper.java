/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.action.manager.ActionManager;
import com.whizzosoftware.hobson.api.config.ConfigurationException;
import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.config.manager.PluginConfigurationListener;
import com.whizzosoftware.hobson.api.device.manager.DeviceManager;
import com.whizzosoftware.hobson.api.disco.manager.DiscoManager;
import com.whizzosoftware.hobson.api.event.EventTopics;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.event.PluginStartedEvent;
import com.whizzosoftware.hobson.api.event.PluginStoppedEvent;
import com.whizzosoftware.hobson.api.event.manager.EventManager;
import com.whizzosoftware.hobson.api.event.manager.EventManagerListener;
import com.whizzosoftware.hobson.api.trigger.TriggerProvider;
import com.whizzosoftware.hobson.api.trigger.manager.TriggerManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.bootstrap.api.config.ConfigurationMetaData;
import com.whizzosoftware.hobson.bootstrap.api.plugin.PluginStatus;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A class that encapsulates a Hobson plugin class in order to handle OSGi lifecycle events and provide
 * the plugin event loop. This implements HobsonPlugin so it can will appear to the OSGi runtime as
 * an actual plugin while it intercepts the OSGi lifecycle callbacks.
 *
 * @author Dan Noguerol
 */
public class HobsonPluginEventLoopWrapper implements HobsonPlugin, PluginConfigurationListener, EventManagerListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // these will be dependency injected by the OSGi runtime
    private volatile DeviceManager deviceManager;
    private volatile VariableManager variableManager;
    private volatile EventManager eventManager;
    private volatile ConfigurationManager configManager;
    private volatile DiscoManager discoManager;
    private volatile TriggerManager triggerManager;
    private volatile ActionManager actionManager;

    private HobsonPlugin plugin;
    private boolean starting = true;

    /**
     * Constructor.
     *
     * @param plugin the plugin to wrapper
     */
    public HobsonPluginEventLoopWrapper(HobsonPlugin plugin) {
        if (plugin != null) {
            this.plugin = plugin;
        } else {
            throw new HobsonRuntimeException("Passed a null plugin to HobsonPluginEventLoopWrapper");
        }
    }

    /**
     * Called when the OSGi service is started. This performs plugin dependency injection and gets the
     * plugin event loop started.
     */
    public void start() {
        // register plugin for configuration updates
        configManager.registerForPluginConfigurationUpdates(getId(), this);

        // register plugin for variable events
        eventManager.addListener(this, EventTopics.VARIABLES_TOPIC);

        // inject manager dependencies
        setDeviceManager(deviceManager);
        setVariableManager(variableManager);
        setConfigurationManager(configManager);
        setDiscoManager(discoManager);
        setTriggerManager(triggerManager);
        setActionManager(actionManager);

        // start the event loop
        Future f = plugin.submitInEventLoop(new Runnable() {
            @Override
            public void run() {
                // start the plugin
                onStartup(getPluginConfiguration(plugin.getId()));

                // post plugin started event
                eventManager.postEvent(new PluginStartedEvent(getId()));

                // schedule the refresh callback if the plugin's refresh interval > 0
                if (plugin.getRefreshInterval() > 0) {
                    plugin.scheduleAtFixedRateInEventLoop(new Runnable() {
                        @Override
                        public void run() {
                            plugin.onRefresh();
                        }
                    }, 0, plugin.getRefreshInterval(), TimeUnit.SECONDS);
                }
            }
        });

        // wait for the async task to complete so that the OSGi framework knows that we've really started
        try {
            f.get();
        } catch (Exception e) {
            logger.error("Error waiting for plugin to start", e);
        }

        starting = false;
    }

    /**
     * Called when the OSGi service is stopped. This will stop the plugin event loop.
     */
    public void stop() {
        // shutdown the plugin
        Future f = plugin.submitInEventLoop(new Runnable() {
            @Override
            public void run() {
                // stop listening for configuration changes
                configManager.unregisterForConfigurationUpdates(getId(), HobsonPluginEventLoopWrapper.this);

                // stop listening for variable events
                eventManager.removeListener(HobsonPluginEventLoopWrapper.this);

                // unpublish all variables published by this plugin's devices
                variableManager.unpublishAllPluginVariables(getId());

                // stop all devices
                deviceManager.unpublishAllDevices(plugin);

                // shut down the plugin
                onShutdown();

                // post plugin stopped event
                eventManager.postEvent(new PluginStoppedEvent(getId()));

                // drop reference
                plugin = null;
            }
        });

        // wait for the async task to complete so that the OSGi framework knows that we've really stopped
        try {
            f.get();
        } catch (Exception e) {
            logger.error("Error waiting for plugin to stop", e);
        }
    }

    /*
     * PluginConfigurationListener methods
     */

    @Override
    public void onPluginConfigurationUpdate(final Dictionary config) {
        if (!starting) { // ignore events when the plugin is still starting up
            plugin.executeInEventLoop(new Runnable() {
                @Override
                public void run() {
                    try {
                        plugin.onPluginConfigurationUpdate(config);
                    } catch (Exception e) {
                        logger.error("An error occurred updating plugin configuration", e);
                    }
                }
            });
        }
    }

    /*
     * DeviceConfigurationListener methods
     */

    @Override
    public void onDeviceConfigurationUpdate(final String deviceId, final Dictionary config) {
        if (!starting) { // ignore events when the plugin is still starting up
            plugin.executeInEventLoop(new Runnable() {
                @Override
                public void run() {
                    try {
                        plugin.onDeviceConfigurationUpdate(deviceId, config);
                    } catch (Exception e) {
                        logger.error("An error occurred updating device configuration", e);
                    }
                }
            });
        }
    }

    /*
     * EventManagerListener methods
     */

    @Override
    public void onHobsonEvent(final HobsonEvent event) {
        if (!starting) { // ignore events when the plugin is still starting up
            plugin.executeInEventLoop(new Runnable() {
                @Override
                public void run() {
                    try {
                        plugin.onHobsonEvent(event);
                    } catch (Exception e) {
                        logger.error("An error occurred processing an event", e);
                    }
                }
            });
        }
    }

    /*
     * Hobson plugin interface methods --  these all pass-through to the real plugin implementation
     */

    @Override
    public void onStartup(Dictionary config) {
        plugin.onStartup(config);
    }

    @Override
    public void onShutdown() {
        plugin.onShutdown();
    }

    @Override
    public void onRefresh() {
        plugin.onRefresh();
    }

    @Override
    public void setDeviceManager(DeviceManager deviceManager) {
        plugin.setDeviceManager(deviceManager);
    }

    @Override
    public void setVariableManager(VariableManager deviceManager) {
        plugin.setVariableManager(deviceManager);
    }

    @Override
    public void setConfigurationManager(ConfigurationManager configManager) {
        plugin.setConfigurationManager(configManager);
    }

    @Override
    public void setDiscoManager(DiscoManager discoManager) {
        plugin.setDiscoManager(discoManager);
    }

    @Override
    public void setTriggerManager(TriggerManager triggerManager) {
        plugin.setTriggerManager(triggerManager);
    }

    @Override
    public void setActionManager(ActionManager actionManager) {
        plugin.setActionManager(actionManager);
    }

    @Override
    public void executeInEventLoop(Runnable runnable) {
        plugin.executeInEventLoop(runnable);
    }

    @Override
    public Future submitInEventLoop(Runnable runnable) {
        return plugin.submitInEventLoop(runnable);
    }

    @Override
    public void scheduleAtFixedRateInEventLoop(Runnable runnable, long initialDelay, long time, TimeUnit unit) {
        plugin.scheduleAtFixedRateInEventLoop(runnable, initialDelay, time, unit);
    }

    @Override
    public void setDeviceConfigurationProperty(String id, String name, Object value, boolean overwrite) {
        plugin.setDeviceConfigurationProperty(id, name, value, overwrite);
    }

    @Override
    public void publishGlobalVariable(HobsonVariable variable) {
        plugin.publishGlobalVariable(variable);
    }

    @Override
    public void publishDeviceVariable(String deviceId, HobsonVariable variable) {
        plugin.publishDeviceVariable(deviceId, variable);
    }

    @Override
    public void publishTriggerProvider(TriggerProvider triggerProvider) {
        plugin.publishTriggerProvider(triggerProvider);
    }

    @Override
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        plugin.fireVariableUpdateNotifications(updates);
    }

    @Override
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate) {
        plugin.fireVariableUpdateNotification(variableUpdate);
    }

    @Override
    public HobsonVariable getDeviceVariable(String deviceId, String variableName) {
        return plugin.getDeviceVariable(deviceId, variableName);
    }

    @Override
    public void onSetDeviceVariable(String deviceId, String variableName, Object value) {
        plugin.onSetDeviceVariable(deviceId, variableName, value);
    }

    @Override
    public long getRefreshInterval() {
        return plugin.getRefreshInterval();
    }

    @Override
    public String getId() {
        return plugin.getId();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public String getVersion() {
        return plugin.getVersion();
    }

    @Override
    public PluginStatus getStatus() {
        return plugin.getStatus();
    }

    @Override
    public Collection<ConfigurationMetaData> getConfigurationMetaData() {
        return plugin.getConfigurationMetaData();
    }

    @Override
    public boolean isConfigurable() {
        return plugin.isConfigurable();
    }

    protected Dictionary getPluginConfiguration(String pluginId) {
        try {
            return configManager.getPluginConfiguration(pluginId);
        } catch (ConfigurationException ce) {
            return new Hashtable();
        }
    }
}
