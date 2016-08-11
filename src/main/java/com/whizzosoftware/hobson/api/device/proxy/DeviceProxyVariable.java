package com.whizzosoftware.hobson.api.device.proxy;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;

public class DeviceProxyVariable {
    private DeviceVariableContext ctx;
    private Object value;
    private Long lastUpdate;

    public DeviceProxyVariable(DeviceContext dctx, String name, Object value, Long lastUpdate) {
        this(DeviceVariableContext.create(dctx, name), value, lastUpdate);
    }

    public DeviceProxyVariable(DeviceVariableContext ctx, Object value, Long lastUpdate) {
        this.ctx = ctx;
        this.value = value;
        this.lastUpdate = lastUpdate;
    }

    public DeviceVariableContext getContext() {
        return ctx;
    }

    public Object getValue() {
        return value;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
}
