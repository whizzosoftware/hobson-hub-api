package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

public class GlobalVariableContext {
    private PluginContext pctx;
    private String name;

    public static GlobalVariableContext create(PluginContext pctx, String name) {
        return new GlobalVariableContext(pctx, name);
    }

    protected GlobalVariableContext(PluginContext pctx, String name) {
        this.pctx = pctx;
        this.name = name;
    }

    public HubContext getHubContext() {
        return pctx != null ? pctx.getHubContext() : null;
    }

    public String getHubId() {
        return pctx != null ? pctx.getHubId() : null;
    }

    public PluginContext getPluginContext() {
        return pctx;
    }

    public String getPluginId() {
        return pctx != null ? pctx.getPluginId() : null;
    }

    public String getName() {
        return name;
    }
}
