package com.whizzosoftware.hobson.api.variable;

public class DeviceVariable {
    DeviceVariableDescription desc;
    Long lastUpdate;
    Object value;

    public DeviceVariable(DeviceVariableDescription desc, Object value, Long lastUpdate) {
        this.desc = desc;
        this.lastUpdate = lastUpdate;
        this.value = value;
    }

    public DeviceVariableContext getContext() {
        return desc != null ? desc.getContext() : null;
    }

    public boolean hasDescription() {
        return (desc != null);
    }

    public DeviceVariableDescription getDescription() {
        return desc;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public Object getValue() {
        return value;
    }
}
