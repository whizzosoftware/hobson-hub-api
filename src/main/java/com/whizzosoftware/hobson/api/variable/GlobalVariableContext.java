/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    public int hashCode() {
        return new HashCodeBuilder().append(getPluginContext()).append(name).toHashCode();
    }

    public boolean equals(Object o) {
        return (
            o instanceof GlobalVariableContext &&
                ((GlobalVariableContext)o).getPluginContext().equals(getPluginContext()) &&
                ((GlobalVariableContext)o).name.equals(name)
        );
    }
}
