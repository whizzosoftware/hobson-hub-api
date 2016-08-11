package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceError;
import com.whizzosoftware.hobson.api.event.DeviceVariableUpdateEvent;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.variable.*;

import java.util.*;

abstract public class AbstractDeviceProxy implements DeviceProxy {
    private HobsonPlugin plugin;
    private String id;
    private PropertyContainerClass configClass;
    private String defaultName;
    private boolean started;
    private DeviceError deviceError;
    private Map<DeviceVariableContext,DeviceVariableDescription> variableDescriptions = new HashMap<>();
    private Map<String,DeviceProxyVariable> variableMap = Collections.synchronizedMap(new HashMap<String,DeviceProxyVariable>());

    /**
     * Constructor.
     *
     * @param plugin the HobsonPlugin that created this device
     */
    public AbstractDeviceProxy(HobsonPlugin plugin, String id, String defaultName) {
        this.plugin = plugin;
        this.id = id;
        this.defaultName = defaultName;
        this.configClass = new PropertyContainerClass(PropertyContainerClassContext.create(DeviceContext.create(plugin.getContext(), id), "configurationClass"), PropertyContainerClassType.DEVICE_CONFIG);

        TypedProperty[] tps = createConfigurationPropertyTypes();
        if (tps != null) {
            for (TypedProperty tp : tps) {
                configClass.addSupportedProperty(tp);
            }
        }

        DeviceVariableDescription[] vars = createVariableDescriptions();
        if (vars != null) {
            for (DeviceVariableDescription dvd : vars) {
                variableDescriptions.put(dvd.getContext(), dvd);
            }
        }
    }

    public PropertyContainerClass getConfigurationClass() {
        return configClass;
    }


    protected DeviceVariableDescription createDeviceVariableDescription(String name, DeviceVariableDescription.Mask mask) {
        return new DeviceVariableDescription(DeviceVariableContext.create(plugin.getContext(), getDeviceId(), name), mask, name, null);
    }

    protected DeviceVariableDescription createDeviceVariableDescription(String name, DeviceVariableDescription.Mask mask, VariableMediaType mediaType) {
        return new DeviceVariableDescription(DeviceVariableContext.create(plugin.getContext(), getDeviceId(), name), mask, name, mediaType);
    }

    protected PluginContext getPluginContext() {
        return getPlugin().getContext();
    }

    @Override
    public String getDeviceId() {
        return this.id;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void onStartup(PropertyContainer config) {
        started = true;
    }

    @Override
    public boolean hasError() {
        return (deviceError != null);
    }

    @Override
    public DeviceError getError() {
        return deviceError;
    }

    public boolean hasPreferredVariableName() {
        return (getPreferredVariableName() != null);
    }

    /**
     * Returns an array of configuration properties the device supports. These will automatically
     * be registered with the device's configuration class.
     *
     * @return an array of TypedProperty objects (or null if there are none)
     */
    abstract protected TypedProperty[] createConfigurationPropertyTypes();

    /**
     * Returns the default name for the device.
     *
     * @return the default name that was set (or the device ID if not set)
     */
    public String getDefaultName() {
        return this.defaultName;
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
    private HobsonPlugin getPlugin() {
        return plugin;
    }

    public DeviceVariableDescription getVariableDescription(DeviceVariableContext vctx) {
        return variableDescriptions.get(vctx);
    }

    public Collection<DeviceVariableDescription> getVariableDescriptions() {
        return variableDescriptions.values();
    }

    abstract protected DeviceVariableDescription[] createVariableDescriptions();

    /**
     * Retrieves a specific variable.
     *
     * @param variableName the variable name
     *
     * @return a HobsonVariable instance
     */
    public DeviceProxyVariable getVariableValue(String variableName) {
        return variableMap.get(variableName);
    }

    public Collection<DeviceProxyVariable> getVariableValues() {
        return variableMap.values();
    }

    public boolean hasVariableDescriptions() {
        return variableDescriptions.size() > 0;
    }

    public boolean hasVariableValue(String name) {
        return variableMap.containsKey(name);
    }

    protected void setVariableValue(String name, Object value, Long updateTime) {
        DeviceVariableContext dvctx = DeviceVariableContext.create(getPluginContext(), getDeviceId(), name);
        DeviceProxyVariable oldVar = getVariableValue(name);
        variableMap.put(name, new DeviceProxyVariable(dvctx, value, updateTime));
        postEvent(new DeviceVariableUpdateEvent(System.currentTimeMillis(), new DeviceVariableUpdate(dvctx, oldVar != null ? oldVar.getValue() : null, value)));
    }

    protected void setVariableValues(Map<String,Object> values) {
        long now = System.currentTimeMillis();
        for (String name : values.keySet()) {
            setVariableValue(name, values.get(name), now);
        }
    }

    protected void setDeviceAvailability(boolean available, Long checkInTime) {
        getPlugin().getRuntime().setDeviceAvailability(getDeviceId(), available, checkInTime);
    }

    protected void postEvent(HobsonEvent event) {
        getPlugin().getRuntime().postEvent(event);
    }

    public String toString() {
        return DeviceContext.create(getPlugin().getContext(), getDeviceId()).toString();
    }
}
