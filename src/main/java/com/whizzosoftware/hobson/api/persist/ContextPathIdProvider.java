/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
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
import com.whizzosoftware.hobson.api.variable.VariableContext;
import org.apache.commons.lang3.StringUtils;

/**
 * An implementation of IdProvider that returns a context path style ID for model entities. This is primarily used
 * internally for persisting model entities in key-value stores.
 *
 * @author Dan Noguerol
 */
public class ContextPathIdProvider implements IdProvider {
    public String createActionId(HubContext ctx, String actionId) {
        return createHubId(ctx) + HubContext.DELIMITER + "actions" + HubContext.DELIMITER + actionId;
    }

    public String createActionSetId(HubContext ctx, String actionSetId) {
        return createActionSetsId(ctx) + HubContext.DELIMITER + actionSetId;
    }

    @Override
    public String createActionSetActionsId(HubContext ctx, String actionSetId) {
        return createActionSetId(ctx, actionSetId) + HubContext.DELIMITER + "actions";
    }

    @Override
    public String createActionSetsId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "actionSets";
    }

    public String createActionPropertiesId(HubContext ctx, String actionId) {
        return createActionId(ctx, actionId) + HubContext.DELIMITER + "properties";
    }

    @Override
    public String createActivityLogId(HubContext ctx) {
        return null;
    }

    @Override
    public String createDataStreamsId(String userId) {
        return userId + HubContext.DELIMITER + "dataStreams";
    }

    @Override
    public String createDataStreamId(String userId, String dataStreamId) {
        return createDataStreamsId(userId) + HubContext.DELIMITER + dataStreamId;
    }

    @Override
    public String createDataStreamDataId(String userId, String dataStreamId) {
        return createDataStreamId(userId, dataStreamId) + HubContext.DELIMITER + "data";
    }

    @Override
    public String createDataStreamVariablesId(String userId, String dataStreamId) {
        return createDataStreamId(userId, dataStreamId) + HubContext.DELIMITER + "variables";
    }

    @Override
    public DeviceContext createDeviceContext(String deviceId) {
        String[] s = StringUtils.split(deviceId, ":");
        if (s.length >= 7) {
            return DeviceContext.create(PluginContext.create(HubContext.create(s[1], s[3]), s[5]), s[6]);
        } else {
            return null;
        }
    }

    @Override
    public DeviceContext createDeviceContextWithHub(HubContext ctx, String deviceId) {
        String[] s = StringUtils.split(deviceId, ":");
        if (s.length >= 7) {
            return DeviceContext.create(PluginContext.create(ctx, s[5]), s[6]);
        } else {
            return null;
        }
    }

    @Override
    public String createDeviceId(DeviceContext ctx) {
        return createDevicesId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId();
    }

    @Override
    public String createDevicesId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "devices";
    }

    @Override
    public String createDevicePassportId(HubContext ctx, String passportId) {
        return createDevicePassportsId(ctx) + HubContext.DELIMITER + passportId;
    }

    @Override
    public String createDevicePassportsId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "passports";
    }

    @Override
    public String createDeviceConfigurationId(DeviceContext ctx) {
        return createHubId(ctx.getHubContext()) + HubContext.DELIMITER + "configuration" + HubContext.DELIMITER + "device" + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId();
    }

    @Override
    public String createDeviceConfigurationClassId(DeviceContext ctx) {
        return null;
    }

    public String createVariablesId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "variables";
    }

    @Override
    public String createDeviceVariablesId(DeviceContext ctx) {
        return createVariablesId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId();
    }

    @Override
    public String createGlobalVariableId(HubContext ctx, String variableName) {
        return createGlobalVariablesId(ctx) + HubContext.DELIMITER + variableName;
    }

    @Override
    public String createGlobalVariablesId(HubContext ctx) {
        return createVariablesId(ctx) + HubContext.DELIMITER + "global";
    }

    @Override
    public String createHubId(HubContext ctx) {
        return createUserHubsId(ctx.getUserId()) + HubContext.DELIMITER + ctx.getHubId();
    }

    @Override
    public String createUserHubsId(String userId) {
        return createUserId(userId) + HubContext.DELIMITER + "hubs";
    }

    @Override
    public String createUsersId() {
        return "users";
    }

    @Override
    public VariableContext createVariableContext(String variableId) {
        String[] s = StringUtils.split(variableId, ":");
        if (s.length >= 8) {
            return VariableContext.create(DeviceContext.create(PluginContext.create(HubContext.create(s[1], s[3]), s[5]), s[6]), s[7]);
        } else {
            return null;
        }
    }

    @Override
    public String createVariableId(VariableContext ctx) {
        return createHubId(ctx.getHubContext()) + HubContext.DELIMITER + "variables" + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId() + HubContext.DELIMITER + ctx.getName();
    }

    @Override
    public String createHubUploadCredentialsId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "uploadCredentials";
    }

    @Override
    public String createHubConfigurationId(HubContext ctx) {
        return null;
    }

    @Override
    public String createHubConfigurationClassId(HubContext ctx) {
        return null;
    }

    @Override
    public String createHubLogId(HubContext ctx) {
        return null;
    }

    @Override
    public String createHubPasswordId(HubContext ctx) {
        return null;
    }

    @Override
    public String createLocalPluginConfigurationId(PluginContext ctx) {
        return null;
    }

    @Override
    public String createLocalPluginConfigurationClassId(PluginContext ctx) {
        return null;
    }

    @Override
    public String createLocalPluginId(PluginContext ctx) {
        return null;
    }

    @Override
    public String createLocalPluginIconId(PluginContext ctx) {
        return null;
    }

    @Override
    public String createLocalPluginReloadId(PluginContext ctx) {
        return null;
    }

    @Override
    public String createLocalPluginsId(HubContext ctx) {
        return null;
    }

    @Override
    public String createPersonId(String userId) {
        return null;
    }

    @Override
    public PluginContext createPluginContext(String pluginId) {
        String[] s = StringUtils.split(pluginId, ":");
        if (s.length >= 6) {
            return PluginContext.create(HubContext.create(s[1], s[3]), s[5]);
        } else {
            return null;
        }
    }

    public String createPluginsId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "plugins";
    }

    public String createPluginId(PluginContext ctx) {
        return createPluginsId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getPluginId();
    }

    public String createPluginConfigurationId(PluginContext ctx) {
        return createPluginId(ctx) + HubContext.DELIMITER + "configuration";
    }

    public String createPluginDeviceId(HubContext ctx, String pluginAndDeviceId) {
        return createHubId(ctx) + HubContext.DELIMITER + "plugins" + HubContext.DELIMITER + pluginAndDeviceId;
    }

    @Override
    public String createPluginDevicesId(PluginContext ctx) {
        return createHubId(ctx.getHubContext()) + HubContext.DELIMITER + "devices" + HubContext.DELIMITER + ctx.getPluginId();
    }

    public String createPresenceEntitiesId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "presenceEntities";
    }

    @Override
    public PresenceEntityContext createPresenceEntityContext(String presenceEntityId) {
        return null;
    }

    @Override
    public String createPresenceEntityId(PresenceEntityContext ctx) {
        return createPresenceEntitiesId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getEntityId();
    }

    @Override
    public String createPresenceLocationsId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "presenceLocations";
    }

    @Override
    public PresenceLocationContext createPresenceLocationContext(String presenceLocationId) {
        return null;
    }

    @Override
    public String createPresenceLocationId(PresenceLocationContext ctx) {
        return createPresenceLocationsId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getLocationId();
    }

    @Override
    public String createPropertyContainerId(PropertyContainerClass pcc) {
        return null;
    }

    @Override
    public String createPropertyContainerClassesId(PluginContext pctx) {
        return createPluginId(pctx) + HubContext.DELIMITER + "containerClasses";
    }

    @Override
    public String createPropertyContainerClassId(PropertyContainerClassContext pccc, PropertyContainerClassType type) {
        return  createPropertyContainerClassesId(pccc.getPluginContext()) + HubContext.DELIMITER + pccc.getContainerClassId();
    }

    @Override
    public String createRemotePluginId(PluginContext ctx, String version) {
        return null;
    }

    @Override
    public String createRemotePluginInstallId(PluginContext ctx, String version) {
        return null;
    }

    @Override
    public String createRemotePluginsId(HubContext ctx) {
        return null;
    }

    @Override
    public String createRepositoriesId(HubContext ctx) {
        return null;
    }

    @Override
    public String createRepositoryId(HubContext ctx, String uri) {
        return null;
    }

    @Override
    public String createSendTestEmailId(HubContext ctx) {
        return null;
    }

    @Override
    public String createShutdownId(HubContext ctx) {
        return null;
    }

    @Override
    public String createTaskActionClassesId(HubContext ctx) {
        return null;
    }

    @Override
    public String createTaskActionClassId(PropertyContainerClassContext ctx) {
        return null;
    }

    @Override
    public String createTaskActionSetId(HubContext ctx, String id) {
        return createTaskActionSetsId(ctx) + HubContext.DELIMITER + id;
    }

    @Override
    public String createTaskActionSetsId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "actionSets";
    }

    @Override
    public String createTaskConditionClassesId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "conditionClasses";
    }

    @Override
    public String createTaskConditionClassId(PropertyContainerClassContext ctx) {
        return createPluginId(ctx.getPluginContext()) + HubContext.DELIMITER + ctx.getContainerClassId();
    }

    @Override
    public String createTaskId(TaskContext ctx) {
        return createHubId(ctx.getHubContext()) + HubContext.DELIMITER + "tasks" + HubContext.DELIMITER + ctx.getTaskId();
    }

    @Override
    public String createTasksId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "tasks";
    }

    @Override
    public String createUserId(String userId) {
        return createUsersId() + HubContext.DELIMITER + userId;
    }

    public String createTaskPropertiesId(TaskContext ctx) {
        return createTaskId(ctx) + HubContext.DELIMITER + "properties";
    }

    public  String createTaskConditionsId(TaskContext ctx) {
        return createTaskId(ctx) + HubContext.DELIMITER + "conditions";
    }

    public String createTaskConditionId(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionsId(ctx) + HubContext.DELIMITER + propertyContainerId;
    }

    @Override
    public String createTaskConditionPropertiesId(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionId(ctx, propertyContainerId) + HubContext.DELIMITER + "properties";
    }

    public String createTaskConditionValuesId(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionsId(ctx) + HubContext.DELIMITER + "conditionValues" + HubContext.DELIMITER + propertyContainerId;
    }

    public String createTaskConditionPropertyId(TaskContext ctx, String propertyContainerId, String name) {
        return createTaskConditionValuesId(ctx, propertyContainerId) + HubContext.DELIMITER + name;
    }
}
