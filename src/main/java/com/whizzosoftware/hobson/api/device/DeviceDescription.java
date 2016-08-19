package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariable;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableDescription;

import java.util.ArrayList;
import java.util.Collection;

public class DeviceDescription {
    private DeviceContext deviceContext;
    private String name;
    private DeviceType deviceType;
    private String manufacturerName;
    private String modelName;
    private String manufacturerVersion;
    private String preferredVariableName;
    private Collection<DeviceVariableDescription> deviceVariableDescriptions;

    public DeviceDescription(PluginContext pctx, String deviceId) {
        this(DeviceContext.create(pctx, deviceId));
    }

    public DeviceDescription(DeviceContext dctx) {
        this.deviceContext = dctx;
    }

    public DeviceContext getContext() {
        return deviceContext;
    }

    public String getName() {
        return name;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getManufacturerVersion() {
        return manufacturerVersion;
    }

    public boolean hasPreferredVariableName() {
        return (preferredVariableName != null);
    }

    public String getPreferredVariableName() {
        return preferredVariableName;
    }

    public boolean hasVariableDescriptions() {
        return (deviceVariableDescriptions != null);
    }

    public Collection<DeviceVariableDescription> getDeviceVariableDescriptions() {
        return deviceVariableDescriptions;
    }

    public DeviceVariableDescription getVariableDescription(DeviceVariableContext vctx) {
        if (deviceVariableDescriptions != null) {
            for (DeviceVariableDescription d : deviceVariableDescriptions) {
                if (d.getContext().equals(vctx)) {
                    return d;
                }
            }
        }
        return null;
    }

    static public class Builder {
        private DeviceDescription desc;

        public Builder(DeviceContext dctx) {
            desc = new DeviceDescription(dctx);
        }

        public Builder(DeviceDescription desc) {
            desc = new DeviceDescription(desc.getContext());
            name(desc.getName());
            type(desc.getDeviceType());
            manufacturerName(desc.getManufacturerName());
            modelName(desc.getModelName());
            manufacturerVersion(desc.getManufacturerVersion());
            preferredVariableName(desc.getPreferredVariableName());
            variableDescriptions(desc.getDeviceVariableDescriptions());
        }

        public DeviceDescription.Builder name(String name) {
            desc.name = name;
            return this;
        }

        public DeviceDescription.Builder type(DeviceType deviceType) {
            desc.deviceType = deviceType;
            return this;
        }

        public DeviceDescription.Builder manufacturerName(String manufacturerName) {
            desc.manufacturerName = manufacturerName;
            return this;
        }

        public DeviceDescription.Builder modelName(String modelName) {
            desc.modelName = modelName;
            return this;
        }

        public DeviceDescription.Builder manufacturerVersion(String manufacturerVersion) {
            desc.manufacturerVersion = manufacturerVersion;
            return this;
        }

        public DeviceDescription.Builder preferredVariableName(String preferredVariableName) {
            desc.preferredVariableName = preferredVariableName;
            return this;
        }

        public DeviceDescription.Builder variableDescriptions(Collection<DeviceVariableDescription> deviceVariableDescriptions) {
            desc.deviceVariableDescriptions = deviceVariableDescriptions;
            return this;
        }

        public DeviceDescription.Builder variableDescription(DeviceVariableDescription deviceVariableDescription) {
            if (desc.deviceVariableDescriptions == null) {
                desc.deviceVariableDescriptions = new ArrayList<>();
            }
            desc.deviceVariableDescriptions.add(deviceVariableDescription);
            return this;
        }

        public DeviceDescription build() {
            return desc;
        }
    }
}
