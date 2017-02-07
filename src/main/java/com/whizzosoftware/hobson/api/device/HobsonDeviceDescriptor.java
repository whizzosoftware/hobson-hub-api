package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableDescriptor;

import java.util.*;

public class HobsonDeviceDescriptor {
    public static final int AVAILABILITY_TIMEOUT_INTERVAL = 600000; // default to 10 minutes

    private Collection<ActionClass> actionClasses;
    private Collection<DeviceContext> associations;
    private PropertyContainerClass configurationClass;
    private DeviceContext context;
    private String manufacturerName;
    private String manufacturerVersion;
    private String modelName;
    protected String name;
    private String preferredVariableName;
    private Set<String> tags;
    private DeviceType type;
    private Map<String,DeviceVariableDescriptor> variables;

    public HobsonDeviceDescriptor(DeviceContext context) {
        this.context = context;
    }

    public Collection<ActionClass> getActionClasses() {
        return actionClasses;
    }

    public Collection<DeviceContext> getAssociations() {
        return associations;
    }

    public PropertyContainerClass getConfigurationClass() {
        return configurationClass;
    }

    public DeviceContext getContext() {
        return context;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    protected void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerVersion() {
        return manufacturerVersion;
    }

    protected void setManufacturerVersion(String manufacturerVersion) {
        this.manufacturerVersion = manufacturerVersion;
    }

    public String getModelName() {
        return modelName;
    }

    protected void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferredVariableName() {
        return preferredVariableName;
    }

    protected void setPreferredVariableName(String preferredVariableName) {
        this.preferredVariableName = preferredVariableName;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public DeviceType getType() {
        return type;
    }

    protected void setType(DeviceType type) {
        this.type = type;
    }

    public boolean hasVariables() {
        return (variables != null && variables.size() > 0);
    }

    public Collection<DeviceVariableDescriptor> getVariables() {
        return variables != null ? variables.values() : null;
    }

    public boolean hasVariable(String name) {
        return (variables.containsKey(name));
    }

    public DeviceVariableDescriptor getVariable(String name) {
        return variables != null ? variables.get(name) : null;
    }

    public boolean hasPreferredVariableName() {
        return (preferredVariableName != null);
    }

    public boolean hasVariableDescriptions() {
        return (variables != null && variables.size() > 0);
    }

    public boolean hasActionClasses() {
        return (actionClasses != null && actionClasses.size() > 0);
    }

    public boolean hasTags() {
        return (tags != null && tags.size() > 0);
    }

    public void setVariableDescriptor(DeviceVariableDescriptor dvd) {
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put(dvd.getContext().getName(), dvd);
    }

    static public class Builder {
        private HobsonDeviceDescriptor desc;

        public Builder(DeviceContext dctx) {
            desc = new HobsonDeviceDescriptor(dctx);
        }

        public Builder(HobsonDeviceDescriptor desc) {
            desc = new HobsonDeviceDescriptor(desc.getContext());
            associations(desc.getAssociations());
            configurationClass(desc.getConfigurationClass());
            name(desc.getName());
            type(desc.getType());
            manufacturerName(desc.getManufacturerName());
            modelName(desc.getModelName());
            manufacturerVersion(desc.getManufacturerVersion());
            preferredVariableName(desc.getPreferredVariableName());
            tags(desc.getTags());
            variableDescriptions(desc.getVariables());
        }

        public HobsonDeviceDescriptor.Builder associations(Collection<DeviceContext> associations) {
            desc.associations = associations;
            return this;
        }

        public HobsonDeviceDescriptor.Builder configurationClass(PropertyContainerClass configurationClass) {
            desc.configurationClass = configurationClass;
            return this;
        }

        public HobsonDeviceDescriptor.Builder name(String name) {
            desc.name = name;
            return this;
        }

        public HobsonDeviceDescriptor.Builder tags(Set<String> tags) {
            desc.tags = tags;
            return this;
        }

        public HobsonDeviceDescriptor.Builder type(DeviceType type) {
            desc.type = type;
            return this;
        }

        public HobsonDeviceDescriptor.Builder manufacturerName(String manufacturerName) {
            desc.manufacturerName = manufacturerName;
            return this;
        }

        public HobsonDeviceDescriptor.Builder modelName(String modelName) {
            desc.modelName = modelName;
            return this;
        }

        public HobsonDeviceDescriptor.Builder manufacturerVersion(String manufacturerVersion) {
            desc.manufacturerVersion = manufacturerVersion;
            return this;
        }

        public HobsonDeviceDescriptor.Builder preferredVariableName(String preferredVariableName) {
            desc.preferredVariableName = preferredVariableName;
            return this;
        }

        public HobsonDeviceDescriptor.Builder variableDescriptions(Collection<DeviceVariableDescriptor> deviceVariableDescriptors) {
            desc.variables = new HashMap<>(deviceVariableDescriptors.size());
            for (DeviceVariableDescriptor dvd : deviceVariableDescriptors) {
                desc.variables.put(dvd.getContext().getName(), dvd);
            }
            return this;
        }

        public HobsonDeviceDescriptor.Builder actionClasses(Collection<ActionClass> actionClasses) {
            desc.actionClasses = actionClasses;
            return this;
        }

        public HobsonDeviceDescriptor.Builder variableDescription(DeviceVariableDescriptor deviceVariableDescriptor) {
            if (desc.variables == null) {
                desc.variables = new HashMap<>();
            }
            desc.variables.put(deviceVariableDescriptor.getContext().getName(), deviceVariableDescriptor);
            return this;
        }

        public HobsonDeviceDescriptor build() {
            return desc;
        }
    }
}
