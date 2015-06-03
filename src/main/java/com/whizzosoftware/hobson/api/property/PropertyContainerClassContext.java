/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Provides a fully qualified context for PropertyContainerClass objects.
 *
 * @author Dan Noguerol
 */
public class PropertyContainerClassContext {
    private String userId;
    private String hubId;
    private String pluginId;
    private String containerClassId;

    static public PropertyContainerClassContext create(HubContext ctx, String containerClassId) {
        return create(ctx.getUserId(), ctx.getHubId(), null, containerClassId);
    }

    static public PropertyContainerClassContext create(PluginContext pctx, String containerClassId) {
        return create(pctx.getUserId(), pctx.getHubId(), pctx.getPluginId(), containerClassId);
    }

    static public PropertyContainerClassContext create(String userId, String hubId, String pluginId, String containerClassId) {
        PropertyContainerClassContext pccc = new PropertyContainerClassContext();
        pccc.userId = userId;
        pccc.hubId = hubId;
        pccc.pluginId = pluginId;
        pccc.containerClassId = containerClassId;
        return pccc;
    }

    public String getUserId() {
        return userId;
    }

    public String getHubId() {
        return hubId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getContainerClassId() {
        return containerClassId;
    }

    public boolean hasHubContext() {
        return (userId != null && hubId != null);
    }

    public HubContext getHubContext() {
        return HubContext.create(userId, hubId);
    }

    public boolean hasPluginContext() {
        return (hasHubContext() && pluginId != null);
    }

    public PluginContext getPluginContext() {
        return PluginContext.create(getHubContext(), pluginId);
    }

    public boolean equals(Object o) {
        return (
            o instanceof PropertyContainerClassContext &&
            ((PropertyContainerClassContext)o).userId.equals(userId) &&
            ((PropertyContainerClassContext)o).hubId.equals(hubId) &&
            ((PropertyContainerClassContext)o).pluginId.equals(pluginId) &&
            ((PropertyContainerClassContext)o).containerClassId.equals(containerClassId)
        );
    }

    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder().append(userId).append(hubId);
        if (pluginId != null) {
            hcb.append(pluginId);
        }
        return hcb.append(containerClassId).toHashCode();
    }

    public String toString() {
        return userId + HubContext.DELIMITER + hubId + HubContext.DELIMITER + pluginId + HubContext.DELIMITER + containerClassId;
    }
}
