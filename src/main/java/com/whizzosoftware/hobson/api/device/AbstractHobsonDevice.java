/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.config.Configuration;
import com.whizzosoftware.hobson.api.config.ConfigurationPropertyMetaData;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for Hobson devices that provides various convenience functions. This provides a good starting
 * point for third-party HobsonDevice implementations.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHobsonDevice implements HobsonDevice, HobsonDeviceRuntime {
    private HobsonPlugin plugin;
    private DeviceContext ctx;
    private String name;
    private String defaultName;
    private DeviceError deviceError;
    private final List<ConfigurationPropertyMetaData> configMeta = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param plugin the HobsonPlugin that created this device
     * @param id the device ID
     */
    public AbstractHobsonDevice(HobsonPlugin plugin, String id) {
        this.plugin = plugin;
        this.ctx = DeviceContext.create(plugin.getContext(), id);

        // register this device's "name" configuration property
        configMeta.add(new ConfigurationPropertyMetaData("name", "Name", "A descriptive name for this device", ConfigurationPropertyMetaData.Type.STRING));

        // register any additional configuration metadata
        ConfigurationPropertyMetaData[] mds = createConfigurationPropertyMetaData();
        if (mds != null) {
            for (ConfigurationPropertyMetaData md : mds) {
                configMeta.add(md);
            }
        }
    }

    @Override
    public DeviceContext getContext() {
        return ctx;
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
    public boolean hasError() {
        return (deviceError != null);
    }

    @Override
    public DeviceError getError() {
        return deviceError;
    }

    @Override
    public String getPreferredVariableName() {
        return null;
    }

    @Override
    public Collection<ConfigurationPropertyMetaData> getConfigurationPropertyMetaData() {
        return configMeta;
    }

    @Override
    public boolean isTelemetryCapable() {
        return (getTelemetryVariableNames() != null);
    }

    @Override
    public HobsonDeviceRuntime getRuntime() {
        return this;
    }

    @Override
    public String[] getTelemetryVariableNames() {
        return null;
    }

    @Override
    public void setConfigurationProperty(String name, Object value, boolean overwrite) {
        getPlugin().getRuntime().setDeviceConfigurationProperty(getContext(), name, value, overwrite);
    }

    @Override
    public void onDeviceConfigurationUpdate(Configuration config) {
        if (config != null) {
            String s = (String)config.getPropertyValue("name");
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
            return ctx.getDeviceId();
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
     * Sets the error information for this device and puts the device into an error state.
     *
     * @param deviceError the device error information
     */
    protected void setError(DeviceError deviceError) {
        this.deviceError = deviceError;
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
        getPlugin().getRuntime().publishDeviceVariable(getContext(), name, value, mask);
    }

    protected void publishVariable(String name, Object value, HobsonVariable.Mask mask, String proxyType) {
        getPlugin().getRuntime().publishDeviceVariable(getContext(), name, value, mask, proxyType);
    }

    /**
     * Fires a notification that variables has been successfully updated.
     *
     * @param updates a List of VariableUpdate instances
     */
    protected void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        getPlugin().getRuntime().fireVariableUpdateNotifications(updates);
    }

    /**
     * Fires a notification that a variable has been successfully updated.
     *
     * @param name the variable name
     * @param value the variable value
     */
    protected void fireVariableUpdateNotification(String name, Object value) {
        getPlugin().getRuntime().fireVariableUpdateNotification(new VariableUpdate(ctx.getPluginId(), ctx.getDeviceId(), name, value));
    }

    /**
     * Retrieves a specific variable.
     *
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     */
    protected HobsonVariable getVariable(String variableName) {
        return getPlugin().getRuntime().getDeviceVariable(getContext(), variableName);
    }

    /**
     * Returns device-specific configuration property meda data.
     *
     * @return an Array of ConfigurationPropertyMetaData (or null if there is none)
     */
    abstract protected ConfigurationPropertyMetaData[] createConfigurationPropertyMetaData();

    public String toString() {
        return getName();
    }
}
