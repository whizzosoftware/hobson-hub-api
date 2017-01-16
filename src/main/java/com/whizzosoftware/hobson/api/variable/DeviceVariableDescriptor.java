package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

public class DeviceVariableDescriptor {
    private DeviceVariableContext context;
    private VariableMask mask;
    private VariableMediaType mediaType;

    public DeviceVariableDescriptor(PluginContext pctx, String deviceId, String name, VariableMask mask) {
        this(DeviceVariableContext.create(pctx, deviceId, name), mask);
    }

    public DeviceVariableDescriptor(DeviceContext dctx, String name, VariableMask mask) {
        this(DeviceVariableContext.create(dctx, name), mask);
    }

    public DeviceVariableDescriptor(DeviceVariableContext ctx, VariableMask mask) {
        this(ctx, mask, null);
    }

    public DeviceVariableDescriptor(DeviceVariableContext ctx, VariableMask mask, VariableMediaType mediaType) {
        this.context = ctx;
        this.mask = mask;
        this.mediaType = mediaType;
    }

    public DeviceVariableContext getContext() {
        return context;
    }

    public VariableMask getMask() {
        return mask;
    }

    public boolean hasMediaType() {
        return (mediaType != null);
    }

    public VariableMediaType getMediaType() {
        return mediaType;
    }
}
