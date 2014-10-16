/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.HobsonVariableImpl;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.bootstrap.api.config.ConfigurationMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

/**
 * Abstract base class for Hobson devices that provides various convenience functions. This provides a good starting
 * point for third-party HobsonDevice implementations.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHobsonDevice implements HobsonDevice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HobsonPlugin plugin;
    private String id;
    private String name;
    private String defaultName;
    private final List<ConfigurationMetaData> configMeta = new ArrayList<ConfigurationMetaData>();

    /**
     * Constructor.
     *
     * @param plugin the HobsonPlugin that created this device
     * @param id the device ID
     */
    public AbstractHobsonDevice(HobsonPlugin plugin, String id) {
        this.plugin = plugin;
        this.id = id;

        addConfigurationMetaData(new ConfigurationMetaData("name", "Name", "A descriptive name for this device", ConfigurationMetaData.Type.STRING));
    }

    @Override
    public String getPluginId() {
        return getPlugin().getId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        } else {
            return getDefaultName();
        }
    }

    @Override
    public String getPreferredVariableName() {
        return null;
    }

    @Override
    public void start() {
        logger.debug("Device \"" + getId() + "\" starting");

        // allow the device sub-class to initialize
        startup();

        // set the device's name to default if it's not already set
        setConfigurationProperty("name", getDefaultName(), false);
    }

    @Override
    public void stop() {
        logger.debug("Device \"" + getId() + "\" stopping");

        // allow the device subclass to shutdown
        shutdown();
    }

    @Override
    public Collection<ConfigurationMetaData> getConfigurationMetaData() {
        return configMeta;
    }

    protected void addConfigurationMetaData(ConfigurationMetaData metaData) {
        configMeta.add(metaData);
    }

    @Override
    public void setConfigurationProperty(String name, Object value, boolean overwrite) {
        getPlugin().setDeviceConfigurationProperty(getId(), name, value, overwrite);
    }

    @Override
    public void onDeviceConfigurationUpdate(Dictionary config) {
        if (config != null) {
            String s = (String)config.get("name");
            if (s != null) {
                this.name = s;
            }
        }
    }

    /**
     * Returns the default name for the device.
     *
     * @return the default name that was set (or the device ID if not set)
     */
    protected String getDefaultName() {
        if (defaultName != null) {
            return defaultName;
        } else {
            return getId();
        }
    }

    /**
     * Sets the default name for the device.
     *
     * @param defaultName the name to set as the default
     */
    protected void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    /**
     * Returns this device's plugin.
     *
     * @return a HobsonPlugin instance
     */
    protected HobsonPlugin getPlugin() {
        return plugin;
    }

    /**
     * Publishes a device variable.
     *
     * @param name the variable name
     * @param value the variable value
     * @param mask the variable mask
     */
    protected void publishVariable(String name, Object value, HobsonVariable.Mask mask) {
        getPlugin().publishDeviceVariable(getId(), new HobsonVariableImpl(name, value, mask));
    }

    /**
     * Fires a notification that variables has been successfully updated.
     *
     * @param updates a List of VariableUpdate instances
     */
    protected void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        getPlugin().fireVariableUpdateNotifications(updates);
    }

    /**
     * Fires a notification that a variable has been successfully updated.
     *
     * @param name the variable name
     * @param value the variable value
     */
    protected void fireVariableUpdateNotification(String name, Object value) {
        getPlugin().fireVariableUpdateNotification(new VariableUpdate(getPluginId(), getId(), name, value));
    }

    /**
     * Retrieves a specific variable.
     *
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     */
    protected HobsonVariable getVariable(String variableName) {
        return getPlugin().getDeviceVariable(getId(), variableName);
    }

    public String toString() {
        return getName();
    }

    /**
     * Method that allows subclasses to perform startup functions.
     */
    abstract protected void startup();

    /**
     * Method that allows subclasses to perform shutdown functions.
     */
    abstract protected void shutdown();
}
