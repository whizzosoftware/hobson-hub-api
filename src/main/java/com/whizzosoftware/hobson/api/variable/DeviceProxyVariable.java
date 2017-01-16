package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

public class DeviceProxyVariable {
    private DeviceVariableContext context;
    private VariableMask mask;
    private VariableMediaType mediaType;
    protected Object value;
    protected Long lastUpdate;

    public DeviceProxyVariable(PluginContext pctx, String deviceId, String name, VariableMask mask) {
        this(DeviceVariableContext.create(pctx, deviceId, name), mask);
    }

    public DeviceProxyVariable(DeviceContext dctx, String name, VariableMask mask) {
        this(DeviceVariableContext.create(dctx, name), mask);
    }

    public DeviceProxyVariable(DeviceVariableContext ctx, VariableMask mask) {
        this(ctx, mask, null, null);
    }

    public DeviceProxyVariable(DeviceVariableContext ctx, VariableMask mask, VariableMediaType mediaType) {
        this(ctx, mask, mediaType, null, null);
    }

    public DeviceProxyVariable(DeviceVariableContext ctx, VariableMask mask, Object value, Long lastUpdate) {
        this(ctx, mask, null, value, lastUpdate);
    }

    public DeviceProxyVariable(DeviceVariableContext ctx, VariableMask mask, VariableMediaType mediaType, Object value, Long lastUpdate) {
        this.context = ctx;
        this.mask = mask;
        this.mediaType = mediaType;
        this.value = value;
        this.lastUpdate = lastUpdate;
    }

    public DeviceVariableContext getContext() {
        return context;
    }

    public DeviceVariableDescriptor getDescriptor() {
        return new DeviceVariableDescriptor(context, mask, mediaType);
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public VariableMask getMask() {
        return mask;
    }

    public VariableMediaType getMediaType() {
        return mediaType;
    }

    public DeviceVariableState getState() {
        return new DeviceVariableState(context, value, lastUpdate);
    }

    public Object getValue() {
        return value;
    }

    public boolean hasMediaType() {
        return (mediaType != null);
    }

    public void setValue(Object value, long timestamp) {
        this.value = value;
        this.lastUpdate = timestamp;
    }

    public String toString() {
        return getContext().toString();
    }
}
