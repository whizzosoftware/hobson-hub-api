package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.variable.DeviceVariable;
import com.whizzosoftware.hobson.api.variable.DeviceVariableDescription;

public class DeviceProxyVariable extends DeviceVariable {
    public DeviceProxyVariable(DeviceVariableDescription desc) {
        this(desc, null, null);
    }

    public DeviceProxyVariable(DeviceVariableDescription desc, Object value, Long lastUpdate) {
        super(desc, value, lastUpdate);
    }

    public void setValue(Object value, long lastUpdate) {
        this.value = value;
        this.lastUpdate = lastUpdate;
    }
}
