/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.action.ActionProvider;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceError;
import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.device.HobsonDeviceDescriptor;
import com.whizzosoftware.hobson.api.event.device.DeviceAvailableEvent;
import com.whizzosoftware.hobson.api.event.device.DeviceVariablesUpdateEvent;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.variable.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

abstract public class AbstractHobsonDeviceProxy implements HobsonDeviceProxy {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHobsonDeviceProxy.class);

    private PropertyContainerClass configurationClass;
    private DeviceContext context;
    private String defaultName;
    private DeviceError deviceError;
    private Long lastCheckin;
    private String name;
    private HobsonPlugin plugin;
    private boolean started;
    private DeviceType type;
    private Map<String,DeviceProxyVariable> variables = Collections.synchronizedMap(new HashMap<String,DeviceProxyVariable>());
    private List<ActionClass> actionClasses = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param plugin the HobsonPlugin that created this device
     * @param id the device ID
     * @param defaultName the default name of the device (used when a user-defined name does not exist)
     * @param type the type of device
     */
    public AbstractHobsonDeviceProxy(HobsonPlugin plugin, String id, String defaultName, DeviceType type) {
        this.context = DeviceContext.create(plugin.getContext(), id);
        this.type = type;
        this.plugin = plugin;
        this.defaultName = defaultName;

        // create configuration class
        this.configurationClass = new PropertyContainerClass(PropertyContainerClassContext.create(DeviceContext.create(plugin.getContext(), id), "configurationClass"), PropertyContainerClassType.DEVICE_CONFIG);
        TypedProperty[] tps = getConfigurationPropertyTypes();
        if (tps != null) {
            for (TypedProperty tp : tps) {
                configurationClass.addSupportedProperty(tp);
            }
        }
    }

    //=================================================================================
    // HobsonDeviceProxy methods
    //=================================================================================

    @Override
    public PropertyContainerClass getConfigurationClass() {
        return configurationClass;
    }

    @Override
    public DeviceContext getContext() {
        return context;
    }

    @Override
    public String getDefaultName() {
        return this.defaultName;
    }

    @Override
    public HobsonDeviceDescriptor getDescriptor() {
        HobsonDeviceDescriptor.Builder b = new HobsonDeviceDescriptor.Builder(getContext());
        b.type(getType());
        b.name(getName());
        b.manufacturerName(getManufacturerName());
        b.manufacturerVersion(getManufacturerVersion());
        b.modelName(getModelName());
        b.preferredVariableName(getPreferredVariableName());
        b.configurationClass(configurationClass);
        b.actionClasses(actionClasses);
        for (DeviceProxyVariable dv : variables.values()) {
            b.variableDescription(dv.getDescriptor());
        }
        return b.build();
    }

    @Override
    public DeviceError getError() {
        return deviceError;
    }

    @Override
    public Long getLastCheckin() {
        return lastCheckin;
    }

    @Override
    public String getName() {
        return name != null ? name : defaultName;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public DeviceVariableState getVariableState(String name) {
        DeviceProxyVariable v = variables.get(name);
        if (v != null) {
            return new DeviceVariableState(v.getContext(), v.getValue(), v.getLastUpdate());
        } else {
            throw new HobsonNotFoundException("Unable to find variable with name: " + name);
        }
    }

    @Override
    public boolean hasError() {
        return (deviceError != null);
    }

    @Override
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public void setLastCheckin(Long lastCheckin) {
        if (this.lastCheckin == null || (lastCheckin != null && lastCheckin - this.lastCheckin > HobsonDeviceDescriptor.AVAILABILITY_TIMEOUT_INTERVAL)) {
            postEvent(new DeviceAvailableEvent(System.currentTimeMillis(), getContext()));
        }
        this.lastCheckin = lastCheckin;
    }

    public void start(final String name, final Map<String,Object> config) {
        logger.trace("Starting device: {}", getContext());
        this.name = name;
        onStartup(name, config);
        started = true;
        logger.trace("Device startup complete: {}", getContext());
    }

    //=================================================================================
    // Protected methods
    //=================================================================================

    /**
     * Returns an array of configuration properties the device supports. These will automatically
     * be registered with the device's configuration class.
     *
     * @return an array of TypedProperty objects (or null if there are none)
     */
    abstract protected TypedProperty[] getConfigurationPropertyTypes();

    protected Object getConfigurationProperty(String name) {
        return plugin.getDeviceConfigurationProperty(getContext().getDeviceId(), name);
    }

    /**
     * Returns this device's plugin.
     *
     * @return a HobsonPlugin instance
     */
    protected HobsonPlugin getPlugin() {
        return plugin;
    }

    protected void postEvent(HobsonEvent event) {
        getPlugin().postEvent(event);
    }

    protected void publishActionProvider(ActionProvider actionProvider) {
        actionClasses.add(actionProvider);
        plugin.publishActionProvider(actionProvider);
    }

    protected void publishVariables(DeviceProxyVariable... vars) {
        logger.trace("Publishing variables for device {}: {}", getContext(), vars);
        Collection<DeviceVariableUpdate> updates = new ArrayList<>();
        Collection<DeviceVariableDescriptor> newVars = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (DeviceProxyVariable v : vars) {
            logger.debug("Publishing variable: {}", v);
            variables.put(v.getContext().getName(), v);
            newVars.add(v.getDescriptor());
            updates.add(new DeviceVariableUpdate(DeviceVariableContext.create(getContext(), name), null, v.getValue(), now));
        }
        // only alert the plugin to the variable changes if the device has been started (this prevents unnecessary
        // events early on in the device startup cycle)
        if (started) {
            plugin.onDeviceVariablesUpdate(newVars);
        }
        postEvent(new DeviceVariablesUpdateEvent(System.currentTimeMillis(), updates));
    }

    protected void publishVariables(Collection<DeviceProxyVariable> vars) {
        logger.trace("Publishing variables for device {}: {}", getContext(), vars);
        Collection<DeviceVariableUpdate> updates = new ArrayList<>();
        Collection<DeviceVariableDescriptor> newVars = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (DeviceProxyVariable v : vars) {
            logger.debug("Publishing variable: {}", v);
            variables.put(v.getContext().getName(), v);
            newVars.add(v.getDescriptor());
            updates.add(new DeviceVariableUpdate(DeviceVariableContext.create(getContext(), name), null, v.getValue(), now));
        }
        // only alert the plugin to the variable changes if the device has been started (this prevents unnecessary
        // events early on in the device startup cycle)
        if (started) {
            plugin.onDeviceVariablesUpdate(newVars);
        }
        postEvent(new DeviceVariablesUpdateEvent(System.currentTimeMillis(), updates));
    }

    protected void setConfigurationProperty(String name, Object value) {
        plugin.setDeviceConfigurationProperty(getContext(), name, value);
    }

    /**
     * Sets the error information for this device and puts the device into an error state.
     *
     * @param deviceError the device error information
     */
    protected void setError(DeviceError deviceError) {
        this.deviceError = deviceError;
    }

    protected void setVariableValue(String name, Object value, Long updateTime) {
        if (name != null && value != null) {
            DeviceVariableContext dvctx = DeviceVariableContext.create(getContext(), name);
            DeviceProxyVariable var = variables.get(name);
            if (var != null) {
                Object oldVar = var.getValue();
                var.setValue(value, updateTime);
                postEvent(new DeviceVariablesUpdateEvent(System.currentTimeMillis(), new DeviceVariableUpdate(dvctx, oldVar != null ? oldVar : null, value, updateTime)));
            } else {
                throw new HobsonRuntimeException("Attempted to set value for unpublished variable: " + name);
            }
        } else {
            throw new HobsonRuntimeException("Unable to set variable value for " + name + "=" + value);
        }
    }

    protected void setVariableValues(Map<String,Object> values) {
        if (values != null && values.size() > 0) {
            long now = System.currentTimeMillis();
            List<DeviceVariableUpdate> updates = new ArrayList<>();
            for (String name : values.keySet()) {
                DeviceProxyVariable var = variables.get(name);
                Object value = values.get(name);
                Object oldVar = var.getValue();
                var.setValue(value, now);
                updates.add(new DeviceVariableUpdate(DeviceVariableContext.create(getContext(), name), oldVar, value, now));
            }
            postEvent(new DeviceVariablesUpdateEvent(System.currentTimeMillis(), updates));
        } else {
            throw new HobsonRuntimeException("Unable to process empty variables values");
        }
    }

    protected DeviceProxyVariable createDeviceVariable(String name, VariableMask mask) {
        return new DeviceProxyVariable(DeviceVariableContext.create(getContext(), name), mask);
    }

    protected DeviceProxyVariable createDeviceVariable(String name, VariableMask mask, VariableMediaType mediaType) {
        return new DeviceProxyVariable(DeviceVariableContext.create(getContext(), name), mask, mediaType);
    }

    protected DeviceProxyVariable createDeviceVariable(String name, VariableMask mask, VariableMediaType mediaType, Object value, Long lastUpdate) {
        return new DeviceProxyVariable(DeviceVariableContext.create(getContext(), name), mask, mediaType, value, lastUpdate);
    }

    protected DeviceProxyVariable createDeviceVariable(String name, VariableMask mask, Object value, Long lastUpdate) {
        return new DeviceProxyVariable(DeviceVariableContext.create(getContext(), name), mask, value, lastUpdate);
    }

    public String toString() {
        return DeviceContext.create(getPlugin().getContext(), getContext().getDeviceId()).toString();
    }
}
