/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.presence.PresenceEntityContext;
import com.whizzosoftware.hobson.api.presence.PresenceLocationContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassType;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.GlobalVariableContext;
import org.apache.commons.lang3.StringUtils;

/**
 * An implementation of IdProvider that returns a context path style ID for model entities. This is primarily used
 * internally for persisting model entities in key-value stores.
 *
 * @author Dan Noguerol
 */
public class ContextPathIdProvider implements IdProvider {
    public TemplatedId createActionId(HubContext ctx, String actionId) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "actions" + HubContext.DELIMITER + actionId,
            ""
        );
    }

    public TemplatedId createActionSetId(HubContext ctx, String actionSetId) {
        return new TemplatedId(
            createActionSetsId(ctx).getId() + HubContext.DELIMITER + actionSetId,
            ""
        );
    }

    @Override
    public TemplatedId createActionSetActionsId(HubContext ctx, String actionSetId) {
        return new TemplatedId(
            createActionSetId(ctx, actionSetId).getId() + HubContext.DELIMITER + "actions",
            ""
        );
    }

    @Override
    public TemplatedId createActionSetsId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "actionSets",
            ""
        );
    }

    @Override
    public TemplatedId createActionPropertiesId(HubContext ctx, String actionId) {
        return new TemplatedId(
            createActionId(ctx, actionId).getId() + HubContext.DELIMITER + "properties",
            ""
        );
    }

    @Override
    public TemplatedId createActivityLogId(HubContext ctx) {
        return new TemplatedId(
            "",
            ""
        );
    }

    @Override
    public TemplatedId createDataStreamsId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "dataStreams",
            "hubs:{hubId}:dataStreams"
        );
    }

    @Override
    public TemplatedId createDataStreamId(HubContext ctx, String dataStreamId) {
        return new TemplatedId(
            createDataStreamsId(ctx).getId() + HubContext.DELIMITER + dataStreamId,
            ""
        );
    }

    @Override
    public TemplatedId createDataStreamDataId(HubContext ctx, String dataStreamId) {
        return new TemplatedId(
            createDataStreamId(ctx, dataStreamId).getId() + HubContext.DELIMITER + "data",
            ""
        );
    }

    @Override
    public TemplatedId createDataStreamFieldsId(HubContext ctx, String dataStreamId) {
        return new TemplatedId(
            createDataStreamId(ctx, dataStreamId).getId() + HubContext.DELIMITER + "fields",
            ""
        );
    }

    @Override
    public TemplatedId createDataStreamFieldId(HubContext ctx, String dataStreamId, String fieldId) {
        return new TemplatedId(
            createDataStreamFieldsId(ctx, dataStreamId).getId() + HubContext.DELIMITER + fieldId,
            ""
        );
    }

    @Override
    public TemplatedId createDataStreamTagsId(HubContext ctx, String dataStreamId) {
        return new TemplatedId(
            createDataStreamId(ctx, dataStreamId).getId() + HubContext.DELIMITER + "tags",
            ""
        );
    }

    @Override
    public DeviceContext createDeviceContext(String deviceId) {
        String[] s = StringUtils.split(deviceId, ":");
        if (s.length >= 5) {
            return DeviceContext.create(PluginContext.create(HubContext.create(s[1]), s[3]), s[4]);
        } else {
            return null;
        }
    }

    @Override
    public DeviceContext createDeviceContextWithHub(HubContext ctx, String deviceId) {
        String[] s = StringUtils.split(deviceId, ":");
        if (s.length >= 5) {
            return DeviceContext.create(PluginContext.create(ctx, s[3]), s[4]);
        } else {
            return null;
        }
    }

    @Override
    public TemplatedId createDeviceActionClassId(DeviceContext ctx, String actionClassId) {
        return new TemplatedId(
            createDeviceActionClassesId(ctx).getId() + HubContext.DELIMITER + actionClassId,
            ""
        );
    }

    @Override
    public TemplatedId createDeviceActionClassesId(DeviceContext ctx) {
        return new TemplatedId(
            createDeviceId(ctx).getId() + HubContext.DELIMITER + "actionClasses",
            ""
        );
    }

    @Override
    public TemplatedId createDeviceActionClassSupportedPropertiesId(DeviceContext dctx, String containerClassId) {
        return new TemplatedId(
            createDeviceActionClassId(dctx, containerClassId).getId() + HubContext.DELIMITER + "supportedProperties",
            ""
        );
    }

    @Override
    public TemplatedId createDeviceId(DeviceContext ctx) {
        return new TemplatedId(
            createDevicesId(ctx.getHubContext()).getId() + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId(),
            "hubs:{hubId}:{pluginId}:{deviceId}"
        );
    }

    @Override
    public TemplatedId createDevicesId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "devices",
            "hubs:{hubId}:devices"
        );
    }

    @Override
    public TemplatedId createDeviceConfigurationId(DeviceContext ctx) {
        return new TemplatedId(
            createHubId(ctx.getHubContext()).getId() + HubContext.DELIMITER + "configuration" + HubContext.DELIMITER + "device" + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId(),
            ""
        );
    }

    @Override
    public TemplatedId createDeviceNameId(DeviceContext ctx) {
        return new TemplatedId(
            createDeviceId(ctx).getId() + HubContext.DELIMITER + "name",
            ""
        );
    }

    @Override
    public TemplatedId createDeviceVariableDescriptionId(DeviceVariableContext vctx) {
        return new TemplatedId(
            createDeviceVariableId(vctx).getId() + HubContext.DELIMITER + "description",
            ""
        );
    }

    @Override
    public TemplatedId createDeviceVariableId(DeviceVariableContext vctx) {
        return new TemplatedId(
            createHubId(vctx.getHubContext()).getId() + HubContext.DELIMITER + "variables" + HubContext.DELIMITER + vctx.getPluginId() + HubContext.DELIMITER + vctx.getDeviceId() + HubContext.DELIMITER + vctx.getName(),
            "hubs:{hubId}:variables:{pluginId}:{deviceId}:{name}"
        );
    }

    @Override
    public TemplatedId createDeviceConfigurationClassId(DeviceContext ctx) {
        return new TemplatedId(
            "",
            ""
        );
    }

    @Override
    public TemplatedId createDeviceTagsId(DeviceContext ctx) {
        return new TemplatedId(
            createDeviceId(ctx).getId() + HubContext.DELIMITER + "tags",
            ""
        );
    }

    @Override
    public TemplatedId createLocalPluginActionClassesId(PluginContext ctx) {
        return new TemplatedId(
            createPluginId(ctx) + HubContext.DELIMITER + "actionClasses",
            ""
        );
    }

    @Override
    public TemplatedId createPluginDeviceConfigurationClassesId(PluginContext ctx) {
        return new TemplatedId(
            createPluginId(ctx) + HubContext.DELIMITER + "deviceConfigurationClasses",
            ""
        );
    }

    @Override
    public TemplatedId createPluginDeviceConfigurationClassId(PluginContext ctx, String name) {
        return new TemplatedId(
            createPluginDeviceConfigurationClassesId(ctx).getId() + HubContext.DELIMITER + name,
            ""
        );
    }

    private String createVariablesId(HubContext ctx) {
        return createHubId(ctx).getId() + HubContext.DELIMITER + "variables";
    }

    @Override
    public TemplatedId createDeviceVariablesId(DeviceContext ctx) {
        return new TemplatedId(
            createVariablesId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId(),
            "hubs:{hubId}:variables:{pluginId}:{deviceId}"
        );
    }

    @Override
    public TemplatedId createGlobalVariableId(GlobalVariableContext gvctx) {
        return new TemplatedId(
            createVariablesId(gvctx.getPluginContext().getHubContext()) + HubContext.DELIMITER + gvctx.getPluginId() + HubContext.DELIMITER + gvctx.getName(),
            ""
        );
    }

    @Override
    public TemplatedId createGlobalVariablesId(HubContext ctx) {
        return new TemplatedId(
            createVariablesId(ctx) + HubContext.DELIMITER + "global",
            ""
        );
    }

    @Override
    public TemplatedId createHubId(HubContext ctx) {
        return new TemplatedId(
            "hubs" + HubContext.DELIMITER + ctx.getHubId(),
            "hubs:{hubId}"
        );
    }

    @Override
    public TemplatedId createUserHubsId(String userId) {
        return new TemplatedId(createUserId(userId).getId() + HubContext.DELIMITER + "hubs", null);
    }

    @Override
    public TemplatedId createUsersId() {
        return new TemplatedId("users", null);
    }

    @Override
    public DeviceVariableContext createDeviceVariableContext(String variableId) {
        String[] s = StringUtils.split(variableId, ":");
        if (s.length >= 6) {
            return DeviceVariableContext.create(DeviceContext.create(PluginContext.create(HubContext.create(s[1]), s[3]), s[4]), s[5]);
        } else {
            return null;
        }
    }

    @Override
    public TemplatedId createJobId(HubContext ctx, String jobId) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "jobs" + HubContext.DELIMITER + jobId,
            ""
        );
    }

    @Override
    public TemplatedId createHubConfigurationId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "configuration",
            ""
        );
    }

    @Override
    public TemplatedId createHubConfigurationClassId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "configurationClass",
            "hubs:{hubId}:configurationClass"
        );
    }

    @Override
    public TemplatedId createHubLogId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "log",
            ""
        );
    }

    @Override
    public TemplatedId createHubPasswordId(HubContext ctx) {
        return new TemplatedId(
            "",
            ""
        );
    }

    @Override
    public TemplatedId createHubSerialPortsId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "serialPorts",
            "hubs:{hubId}:serialPorts"
        );
    }

    @Override
    public TemplatedId createHubSerialPortId(HubContext ctx, String name) {
        return new TemplatedId(
            createHubSerialPortsId(ctx).getId() + HubContext.DELIMITER + name,
            ""
        );
    }

    @Override
    public TemplatedId createLocalPluginConfigurationId(PluginContext ctx) {
        return new TemplatedId(
            createLocalPluginId(ctx).getId() + HubContext.DELIMITER + "configuration",
            ""
        );
    }

    @Override
    public TemplatedId createLocalPluginConfigurationClassId(PluginContext ctx) {
        return null;
    }

    @Override
    public TemplatedId createLocalPluginId(PluginContext ctx) {
        return new TemplatedId(
            createLocalPluginsId(ctx.getHubContext()).getId() + HubContext.DELIMITER + ctx.getPluginId(),
            ""
        );
    }

    @Override
    public TemplatedId createLocalPluginIconId(PluginContext ctx) {
        return new TemplatedId("", "");
    }

    @Override
    public TemplatedId createLocalPluginReloadId(PluginContext ctx) {
        return null;
    }

    @Override
    public TemplatedId createLocalPluginsId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "localPlugins",
            ""
        );
    }

    @Override
    public TemplatedId createPersonId(String userId) {
        return new TemplatedId(
            null,
            null
        );
    }

    @Override
    public PluginContext createPluginContext(String pluginId) {
        String[] s = StringUtils.split(pluginId, ":");
        if (s.length >= 4) {
            return PluginContext.create(HubContext.create(s[1]), s[3]);
        } else {
            return null;
        }
    }

    public String createPluginsId(HubContext ctx) {
        return createHubId(ctx).getId() + HubContext.DELIMITER + "plugins";
    }

    public String createPluginId(PluginContext ctx) {
        return createPluginsId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getPluginId();
    }

    public String createPluginConfigurationId(PluginContext ctx) {
        return createPluginId(ctx) + HubContext.DELIMITER + "configuration";
    }

    public String createPluginDeviceId(HubContext ctx, String pluginAndDeviceId) {
        return createHubId(ctx).getId() + HubContext.DELIMITER + "plugins" + HubContext.DELIMITER + pluginAndDeviceId;
    }

    @Override
    public TemplatedId createPluginDevicesId(PluginContext ctx) {
        return new TemplatedId(
            createHubId(ctx.getHubContext()).getId() + HubContext.DELIMITER + "devices" + HubContext.DELIMITER + ctx.getPluginId(),
            ""
        );
    }

    public TemplatedId createPresenceEntitiesId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "presenceEntities",
            "hubs:{hubId}:presenceEntities"
        );
    }

    @Override
    public PresenceEntityContext createPresenceEntityContext(String presenceEntityId) {
        return null;
    }

    @Override
    public TemplatedId createPresenceEntityId(PresenceEntityContext ctx) {
        return new TemplatedId(
            createPresenceEntitiesId(ctx.getHubContext()).getId() + HubContext.DELIMITER + ctx.getEntityId(),
            ""
        );
    }

    @Override
    public TemplatedId createPresenceLocationsId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "presenceLocations",
            "hubs:{hubId}:presenceLocations"
        );
    }

    @Override
    public PresenceLocationContext createPresenceLocationContext(String presenceLocationId) {
        return null;
    }

    @Override
    public TemplatedId createPresenceLocationId(PresenceLocationContext ctx) {
        return new TemplatedId(
            createPresenceLocationsId(ctx.getHubContext()).getId() + HubContext.DELIMITER + ctx.getLocationId(),
            ""
        );
    }

    @Override
    public TemplatedId createPropertyContainerId(String id, PropertyContainerClass pcc) {
        if (pcc != null) {
            switch (pcc.getType()) {
                case CONDITION:
                case ACTION: {
                    return new TemplatedId(id, "");
                }
                case HUB_CONFIG: {
                    return new TemplatedId(createHubConfigurationId(pcc.getContext().getHubContext()).getId(), "");
                }
                case PLUGIN_CONFIG: {
                    return new TemplatedId(createLocalPluginConfigurationId(pcc.getContext().getPluginContext()).getId(), "");
                }
                case DEVICE_CONFIG: {
                    return new TemplatedId(createDeviceConfigurationId(DeviceContext.create(pcc.getContext().getHubContext(), pcc.getContext().getPluginId(), pcc.getContext().getDeviceId())).getId(), "");
                }
                default: {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    @Override
    public TemplatedId createPropertyContainerClassesId(PluginContext pctx) {
        return new TemplatedId(
            createPluginId(pctx) + HubContext.DELIMITER + "containerClasses",
            ""
        );
    }

    @Override
    public TemplatedId createPropertyContainerClassId(PropertyContainerClassContext pccc, PropertyContainerClassType type) {
        return new TemplatedId(
            createPropertyContainerClassesId(pccc.getPluginContext()).getId() + HubContext.DELIMITER + pccc.getContainerClassId(),
            ""
        );
    }

    @Override
    public TemplatedId createRemotePluginId(HubContext ctx, String pluginId, String version) {
        return new TemplatedId(null, null);
    }

    @Override
    public TemplatedId createRemotePluginInstallId(HubContext ctx, String pluginId, String version) {
        return new TemplatedId(null, null);
    }

    @Override
    public TemplatedId createRemotePluginsId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "remotePlugins",
            "hubs:{hubId}:remotePlugins"
        );
    }

    @Override
    public TemplatedId createRepositoriesId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "repositories",
            "hubs:{hubId}:repositories"
        );
    }

    @Override
    public TemplatedId createRepositoryId(HubContext ctx, String uri) {
        return new TemplatedId(null, null);
    }

    @Override
    public TemplatedId createSendTestEmailId(HubContext ctx) {
        return new TemplatedId(null, null);
    }

    @Override
    public TemplatedId createShutdownId(HubContext ctx) {
        return new TemplatedId(null, null);
    }

    @Override
    public TemplatedId createActionClassesId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "actionClasses",
            "hubs:{hubId}:actionClasses"
        );
    }

    @Override
    public TemplatedId createActionClassId(PropertyContainerClassContext ctx) {
        return null;
    }

    @Override
    public TemplatedId createTaskActionSetId(HubContext ctx, String id) {
        return new TemplatedId(createTaskActionSetsId(ctx).getId() + HubContext.DELIMITER + id, null);
    }

    @Override
    public TemplatedId createTaskActionSetsId(HubContext ctx) {
        return new TemplatedId(createHubId(ctx).getId() + HubContext.DELIMITER + "actionSets", null);
    }

    @Override
    public TemplatedId createTaskConditionClassesId(HubContext ctx) {
        return new TemplatedId(createHubId(ctx).getId() + HubContext.DELIMITER + "conditionClasses", null);
    }

    @Override
    public TemplatedId createTaskConditionClassId(PropertyContainerClassContext ctx) {
        return new TemplatedId(
            createPluginId(ctx.getPluginContext()) + HubContext.DELIMITER + ctx.getContainerClassId(),
            ""
        );
    }

    @Override
    public TemplatedId createTaskId(TaskContext ctx) {
        return new TemplatedId(
            createHubId(ctx.getHubContext()).getId() + HubContext.DELIMITER + "tasks" + HubContext.DELIMITER + ctx.getTaskId(),
            null
        );
    }

    @Override
    public TemplatedId createTasksId(HubContext ctx) {
        return new TemplatedId(
            createHubId(ctx).getId() + HubContext.DELIMITER + "tasks",
            "hubs:{hubId}:local"
        );
    }

    @Override
    public TemplatedId createUserId(String userId) {
        return new TemplatedId(createUsersId().getId() + HubContext.DELIMITER + userId, null);
    }

    public TemplatedId createTaskPropertiesId(TaskContext ctx) {
        return new TemplatedId(createTaskId(ctx).getId() + HubContext.DELIMITER + "properties", null);
    }

    public TemplatedId createTaskConditionsId(TaskContext ctx) {
        return new TemplatedId(createTaskId(ctx).getId() + HubContext.DELIMITER + "conditions", null);
    }

    public TemplatedId createTaskConditionId(TaskContext ctx, String propertyContainerId) {
        return new TemplatedId(createTaskConditionsId(ctx).getId() + HubContext.DELIMITER + propertyContainerId, null);
    }

    @Override
    public TemplatedId createTaskConditionPropertiesId(TaskContext ctx, String propertyContainerId) {
        return new TemplatedId(createTaskConditionId(ctx, propertyContainerId).getId() + HubContext.DELIMITER + "properties", null);
    }

    public String createTaskConditionValuesId(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionsId(ctx).getId() + HubContext.DELIMITER + "conditionValues" + HubContext.DELIMITER + propertyContainerId;
    }

    public String createTaskConditionPropertyId(TaskContext ctx, String propertyContainerId, String name) {
        return createTaskConditionValuesId(ctx, propertyContainerId) + HubContext.DELIMITER + name;
    }
}
