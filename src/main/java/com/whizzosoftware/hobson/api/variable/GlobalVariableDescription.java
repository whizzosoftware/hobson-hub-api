package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

public class GlobalVariableDescription {
    private GlobalVariableContext ctx;
    private String name;
    private VariableMediaType mediaType;

    public GlobalVariableContext getContext() {
        return ctx;
    }

    public HubContext getHubContext() {
        return ctx.getPluginContext() != null ? ctx.getPluginContext().getHubContext() : null;
    }

    public String getHubId() {
        return ctx.getPluginContext() != null ? ctx.getPluginContext().getHubId() : null;
    }

    public PluginContext getPluginContext() {
        return ctx.getPluginContext();
    }

    public String getPluginId() {
        return ctx.getPluginId();
    }

    public String getName() {
        return name;
    }

    public VariableMediaType getMediaType() {
        return mediaType;
    }
}
