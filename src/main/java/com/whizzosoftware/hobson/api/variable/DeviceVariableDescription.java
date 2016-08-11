package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

public class DeviceVariableDescription {
    private DeviceVariableContext context;
    private Mask mask;
    private String name;
    private VariableMediaType mediaType;

    public DeviceVariableDescription(PluginContext pctx, String deviceId, String name, Mask mask) {
        this(DeviceVariableContext.create(pctx, deviceId, name), mask, name);
    }

    public DeviceVariableDescription(DeviceContext dctx, String name, Mask mask) {
        this(DeviceVariableContext.create(dctx, name), mask);
    }

    public DeviceVariableDescription(DeviceVariableContext context, Mask mask) {
        this(context, mask, null, null);
    }

    public DeviceVariableDescription(DeviceVariableContext context, Mask mask, String name) {
        this(context, mask, name, null);
    }

    public DeviceVariableDescription(DeviceVariableContext context, Mask mask, String name, VariableMediaType mediaType) {
        this.context = context;
        this.mask = mask;
        this.name = name;
        this.mediaType = mediaType;
    }

    public DeviceVariableContext getContext() {
        return context;
    }

    public Mask getMask() {
        return mask;
    }

    public String getName() {
        return name;
    }

    public boolean hasMediaType() {
        return (mediaType != null);
    }

    public VariableMediaType getMediaType() {
        return mediaType;
    }

    public enum Mask {
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE
    }
}
