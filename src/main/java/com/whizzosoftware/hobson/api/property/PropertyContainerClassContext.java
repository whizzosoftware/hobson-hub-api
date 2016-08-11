/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Provides a fully qualified context for PropertyContainerClass objects.
 *
 * @author Dan Noguerol
 */
public class PropertyContainerClassContext implements Serializable { // TODO: remove
    private String hubId;
    private String pluginId;
    private String deviceId;
    private String containerClassId;

    static public PropertyContainerClassContext create(HubContext ctx, String containerClassId) {
        return create(ctx.getHubId(), null, null, containerClassId);
    }

    static public PropertyContainerClassContext create(PluginContext pctx, String containerClassId) {
        return create(pctx.getHubId(), pctx.getPluginId(), null, containerClassId);
    }

    static public PropertyContainerClassContext create(DeviceContext dctx, String containerClassId) {
        return create(dctx.getHubId(), dctx.getPluginId(), dctx.getDeviceId(), containerClassId);
    }

    static public PropertyContainerClassContext create(String s) {
        String[] comps = StringUtils.split(s, HubContext.DELIMITER);
        return PropertyContainerClassContext.create(
            DeviceContext.create(
                PluginContext.create(
                    HubContext.create(comps[0]),
                    comps[1]
                ),
                comps[2] != null && !"null".equalsIgnoreCase(comps[2]) ? comps[2] : null
            ),
            comps[3]
        );
    }

    static public PropertyContainerClassContext create(String hubId, String pluginId, String deviceId, String containerClassId) {
        PropertyContainerClassContext pccc = new PropertyContainerClassContext();
        pccc.hubId = hubId;
        pccc.pluginId = pluginId;
        pccc.deviceId = deviceId;
        pccc.containerClassId = containerClassId;
        return pccc;
    }

    public String getHubId() {
        return hubId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getContainerClassId() {
        return containerClassId;
    }

    public boolean hasHubContext() {
        return (hubId != null);
    }

    public HubContext getHubContext() {
        return HubContext.create(hubId);
    }

    public boolean hasPluginContext() {
        return (hasHubContext() && pluginId != null);
    }

    public boolean hasDeviceContext() {
        return (hasPluginContext() && deviceId != null);
    }

    public PluginContext getPluginContext() {
        return PluginContext.create(getHubContext(), pluginId);
    }

    public boolean equals(Object o) {
        return (
            o instanceof PropertyContainerClassContext &&
            ((PropertyContainerClassContext)o).hubId.equals(hubId) &&
            ((PropertyContainerClassContext)o).pluginId.equals(pluginId) &&
            (((PropertyContainerClassContext)o).deviceId == null || ((PropertyContainerClassContext)o).deviceId.equals(deviceId)) &&
            ((PropertyContainerClassContext)o).containerClassId.equals(containerClassId)
        );
    }

    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder().append(hubId);
        if (pluginId != null) {
            hcb.append(pluginId);
        }
        if (deviceId != null) {
            hcb.append(deviceId);
        }
        return hcb.append(containerClassId).toHashCode();
    }

    public String toString() {
        return hubId + HubContext.DELIMITER + pluginId + HubContext.DELIMITER + deviceId + HubContext.DELIMITER + containerClassId;
    }
}
