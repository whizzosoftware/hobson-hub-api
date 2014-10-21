/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.action.manager.ActionManager;
import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.device.manager.DeviceManager;
import com.whizzosoftware.hobson.api.disco.manager.DiscoManager;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.trigger.HobsonTrigger;
import com.whizzosoftware.hobson.api.trigger.TriggerProvider;
import com.whizzosoftware.hobson.api.trigger.manager.TriggerManager;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;
import com.whizzosoftware.hobson.bootstrap.api.plugin.BootstrapHobsonPlugin;
import com.whizzosoftware.hobson.bootstrap.api.plugin.PluginStatus;

import java.util.Dictionary;
import java.util.List;

/**
 * Interface for all Hobson Hub plugins.
 *
 * @author Dan Noguerol
 */
public interface HobsonPlugin extends BootstrapHobsonPlugin {
    /**
     * Initialize the plugin.
     */
    public void init(Dictionary config);

    /**
     * Stop the plugin.
     */
    public void stop();

    /**
     * Returns the plugin's current status.
     *
     * @return a PluginStatus instance
     */
    public PluginStatus getStatus();

    /**
     * Returns how often the refresh() method will be called.
     *
     * @return the refresh interval in seconds (a 0 value means never)
     */
    public long getRefreshInterval();

    /**
     * Sets the DeviceManager instance the plugin should use. This will be called before the init() method.
     *
     * @param deviceManager a DeviceManager
     */
    public void setDeviceManager(DeviceManager deviceManager);

    /**
     * Sets the VariableManager instance the plugin should use. This will be called before the init() method.
     *
     * @param variableManager a VariableManager
     */
    public void setVariableManager(VariableManager variableManager);

    /**
     * Sets the ConfigurationManager instance the plugin should use. This will be called before the init() method.
     *
     * @param configManager a ConfigManager
     */
    public void setConfigurationManager(ConfigurationManager configManager);

    /**
     * Sets the DiscoManager instance the plugin should use. This will be called before the init() method.
     *
     * @param discoManager a DiscoManager
     */
    public void setDiscoManager(DiscoManager discoManager);

    /**
     * Sets the TriggerManager instance the plugin should use. This will be called before the init() method.
     *
     * @param triggerManager a TriggerManager
     */
    public void setTriggerManager(TriggerManager triggerManager);

    /**
     * Sets the ActionManager instance the plugin should use. This will be called before the init() method.
     *
     * @param actionManager an ActionManager
     */
    public void setActionManager(ActionManager actionManager);

    /**
     * Sets a configuration property for a specific device.
     *
     * @param id the ID of the device
     * @param name the name of the variable
     * @param value the value of the variable
     * @param overwrite whether to overwrite the property if it already exists
     */
    public void setDeviceConfigurationProperty(String id, String name, Object value, boolean overwrite);

    /**
     * Retrieves a specific device variable.
     *
     * @param deviceId the ID Of the device that published the variable
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     * @throws com.whizzosoftware.hobson.api.variable.manager.VariableNotFoundException if the variable wasn't found
     */
    public HobsonVariable getDeviceVariable(String deviceId, String variableName);

    /**
     * Publish a global variable.
     *
     * @param variable the variable to publish
     */
    public void publishGlobalVariable(HobsonVariable variable);

    /**
     * Publish a device variable.
     *
     * @param deviceId the ID of the device publishing the variable
     * @param variable the variable to publish
     */
    public void publishDeviceVariable(String deviceId, HobsonVariable variable);

    /**
     * Publish a trigger provider.
     *
     * @param triggerProvider the trigger provider object
     */
    public void publishTriggerProvider(TriggerProvider triggerProvider);

    /**
     * Fires a notification that variables has been successfully updated.
     *
     * @param updates a List of VariableUpdate instances
     */
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates);

    /**
     * Fires a notification that a variable has been successfully updated.
     *
     * @param variableUpdate a VariableUpdate instance
     */
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate);

    /**
     * Callback that gives a plugin the opportunity to perform work. This will be called every
     * refresh interval.
     */
    public void onRefresh();

    /**
     * Called when the plugin's configuration has changed.
     *
     * @param config the new configuration
     */
    public void onPluginConfigurationUpdate(Dictionary config);

    /**
     * Called when the plugin device's configuration has changed.
     *
     * @param deviceId the device ID
     * @param config the new configuration
     */
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config);

    /**
     * Callback when a Hobson event is received.
     *
     * @param event the event
     */
    public void onHobsonEvent(HobsonEvent event);

    /**
     * Callback when a request to set a device variable has been received.
     *
     * @param deviceId the ID Of the device that published the variable
     * @param variableName the variable name
     * @param value the variable value
     *
     * @throws com.whizzosoftware.hobson.api.variable.manager.VariableNotFoundException
     */
    public void onSetDeviceVariable(String deviceId, String variableName, Object value);
}
