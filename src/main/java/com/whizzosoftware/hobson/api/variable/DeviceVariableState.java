package com.whizzosoftware.hobson.api.variable;

public class DeviceVariableState {
    private DeviceVariableContext context;
    private Object value;
    private Long lastUpdate;

    public DeviceVariableState(DeviceVariableContext context, Object value, Long lastUpdate) {
        this.context = context;
        this.value = value;
        this.lastUpdate = lastUpdate;
    }

    public DeviceVariableContext getContext() {
        return context;
    }

    public Object getValue() {
        return value;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
}
