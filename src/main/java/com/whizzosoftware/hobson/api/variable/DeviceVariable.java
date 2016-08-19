package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;

public class DeviceVariable {
    protected DeviceVariableDescription desc;
    protected Long lastUpdate;
    protected Object value;

    public DeviceVariable(DeviceVariableDescription desc, Object value, Long lastUpdate) {
        if (desc == null) {
            throw new HobsonRuntimeException("Device variable description cannot be null");
        }
        this.desc = desc;
        this.lastUpdate = lastUpdate;
        this.value = value;
    }

    public DeviceVariableContext getContext() {
        return desc.getContext();
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
