/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableContext;
import com.whizzosoftware.hobson.api.variable.VariableMediaType;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;

import java.util.Collections;
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
    private boolean started;
    private DeviceError deviceError;
    private final PropertyContainerClass configClass;

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
        configClass = new PropertyContainerClass(PropertyContainerClassContext.create(ctx, "configurationClass"), PropertyContainerClassType.DEVICE_CONFIG);
        configClass.addSupportedProperty(new TypedProperty.Builder("name", "Name", "A descriptive name for this device", TypedProperty.Type.STRING).build());

        // register any supported properties the subclass needs
        TypedProperty[] props = createSupportedProperties();
        if (props != null) {
            for (TypedProperty p : props) {
                configClass.addSupportedProperty(p);
            }
        }

        // assume if this device is being created, it has checked-in
        setDeviceAvailability(true, System.currentTimeMillis());
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void onStartup(PropertyContainer config) {
        started = true;

        if (config != null && config.hasPropertyValue("name")) {
            this.name = config.getPropertyValue("name").toString();
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
    public String getManufacturerName() {
        return null;
    }

    @Override
    public String getModelName() {
        return null;
    }

    @Override
    public String getManufacturerVersion() {
        return null;
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
    public void setDeviceAvailability(boolean available, Long checkInTime) {
        getPlugin().getRuntime().setDeviceAvailability(getContext(), available, checkInTime);
    }

    @Override
    public boolean hasPreferredVariableName() {
        return (getPreferredVariableName() != null);
    }

    @Override
    public String getPreferredVariableName() {
        return null;
    }

    @Override
    public PropertyContainerClass getConfigurationClass() {
        return configClass;
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
    public void onDeviceConfigurationUpdate(PropertyContainer config) {
        if (config != null) {
            String s = (String)config.getPropertyValue("name");
            if (s != null) {
                this.name = s;
            }
        }
    }

    /**
     * Returns an array of configuration properties the device supports. These will automatically
     * be registered with the device's configuration class.
     *
     * @return an array of TypedProperty objects (or null if there are none)
     */
    abstract protected TypedProperty[] createSupportedProperties();

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
    protected void publishVariable(String name, Object value, HobsonVariable.Mask mask, Long lastUpdate) {
        getPlugin().getRuntime().publishVariable(VariableContext.create(getContext(), name), value, mask, lastUpdate);
    }

    /**
     * Publishes a device variable.
     *
     * @param name the variable name
     * @param value the variable value
     * @param mask the variable mask
     * @param mediaType the variable media type
     */
    protected void publishVariable(String name, Object value, HobsonVariable.Mask mask, Long lastUpdate, VariableMediaType mediaType) {
        getPlugin().getRuntime().publishVariable(VariableContext.create(getContext(), name), value, mask, lastUpdate, mediaType);
    }

    /**
     * Fires a notification that a variable has been successfully updated.
     *
     * @param name the variable name
     * @param value the variable value
     */
    protected void fireVariableUpdateNotification(String name, Object value) {
        fireVariableUpdateNotifications(Collections.singletonList(new VariableUpdate(VariableContext.create(getContext(), name), value)));
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
     * Retrieves a specific variable.
     *
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     */
    protected HobsonVariable getVariable(String variableName) {
        return getPlugin().getRuntime().getDeviceVariable(getContext(), variableName);
    }

    public String toString() {
        return getName();
    }
}
