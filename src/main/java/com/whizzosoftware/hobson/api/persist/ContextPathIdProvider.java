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
        return createHubId(ctx) + HubContext.DELIMITER + "actionSets" + HubContext.DELIMITER + actionSetId;
    }

    public String createActionPropertiesId(HubContext ctx, String actionId) {
        return createActionId(ctx, actionId) + HubContext.DELIMITER + "properties";
    }

    @Override
    public String createActivityLogId(HubContext ctx) {
        return null;
    }

    @Override
    public DeviceContext createDeviceContext(String deviceId) {
        String[] s = StringUtils.split(deviceId, ":");
        if (s.length >= 6) {
            return DeviceContext.create(PluginContext.create(HubContext.create(s[0], s[2]), s[4]), s[5]);
        } else {
            return null;
        }
    }

    @Override
    public DeviceContext createDeviceContextWithHub(HubContext ctx, String deviceId) {
        String[] s = StringUtils.split(deviceId, ":");
        if (s.length >= 6) {
            return DeviceContext.create(PluginContext.create(ctx, s[4]), s[5]);
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
    public String createDeviceBootstrapId(HubContext ctx, String deviceId) {
        return null;
    }

    @Override
    public String createDeviceBootstrapsId(HubContext ctx) {
        return null;
    }

    @Override
    public String createDeviceConfigurationId(DeviceContext ctx) {
        return createHubId(ctx.getHubContext()) + HubContext.DELIMITER + "configuration" + HubContext.DELIMITER + "device" + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId();
    }

    @Override
    public String createDeviceConfigurationClassId(DeviceContext ctx) {
        return null;
    }

    @Override
    public String createDeviceTelemetryId(DeviceContext ctx) {
        return null;
    }

    @Override
    public String createDeviceTelemetryDatasetId(DeviceContext ctx, String dataSetId) {
        return null;
    }

    @Override
    public String createDeviceTelemetryDatasetsId(DeviceContext ctx) {
        return null;
    }

    @Override
    public DeviceContext createDeviceVariableContext(String variableId) {
        String[] s = StringUtils.split(variableId, ":");
        if (s.length >= 8) {
            return DeviceContext.create(HubContext.create(s[0], s[2]), s[5], s[6]);
        } else {
            return null;
        }
    }

    public String createVariablesId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "variables";
    }

    @Override
    public String createDeviceVariableId(DeviceContext ctx, String variableName) {
        return createDeviceVariablesId(ctx) + HubContext.DELIMITER + variableName;
    }

    @Override
    public String createDeviceVariableName(String variableId) {
        String[] s = StringUtils.split(variableId, ":");
        if (s.length >= 8) {
            return s[7];
        } else {
            return null;
        }
    }

    @Override
    public String createDeviceVariablesId(DeviceContext ctx) {
        return createVariablesId(ctx.getHubContext()) + HubContext.DELIMITER + "device" + HubContext.DELIMITER + ctx.getPluginId() + HubContext.DELIMITER + ctx.getDeviceId();
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
        return createHubsId(ctx.getUserId()) + HubContext.DELIMITER + ctx.getHubId();
    }

    @Override
    public String createHubsId(String userId) {
        return userId + HubContext.DELIMITER + "hubs";
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
        if (s.length >= 5) {
            return PluginContext.create(HubContext.create(s[0], s[2]), s[4]);
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
    public String createPropertyContainerClassId(PropertyContainerClassContext pccc, PropertyContainerClassType type) {
        return null;
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
        return null;
    }

    @Override
    public String createTaskActionSetsId(HubContext ctx) {
        return null;
    }

    @Override
    public String createTaskConditionClassesId(HubContext ctx) {
        return null;
    }

    @Override
    public String createTaskConditionClassId(PropertyContainerClassContext ctx) {
        return null;
    }

    @Override
    public String createTaskId(TaskContext ctx) {
        return null;
    }

    @Override
    public String createTasksId(HubContext ctx) {
        return createHubId(ctx) + HubContext.DELIMITER + "tasks";
    }

    public String createTaskMetaRootId(HubContext ctx) {
        return createTasksId(ctx) + HubContext.DELIMITER + "taskMeta";
    }

    public String createTaskMetaId(TaskContext ctx) {
        return createTaskMetaRootId(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getTaskId();
    }

    public String createTaskPropertiesId(TaskContext ctx) {
        return createTasksId(ctx.getHubContext()) + HubContext.DELIMITER + "properties" + HubContext.DELIMITER + ctx.getTaskId();
    }

    public String createTaskPropertyId(TaskContext ctx, String name) {
        return createTaskPropertiesId(ctx) + HubContext.DELIMITER + name;
    }

    public  String createTaskConditionsId(TaskContext ctx) {
        return createTasksId(ctx.getHubContext()) + HubContext.DELIMITER + "conditions" + HubContext.DELIMITER + ctx.getTaskId();
    }

    public String createTaskConditionMetasId(TaskContext ctx) {
        return createTaskConditionsId(ctx) + HubContext.DELIMITER + "conditionMeta";
    }

    public String createTaskConditionMetaId(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionMetasId(ctx) + HubContext.DELIMITER + propertyContainerId;
    }

    public String createTaskConditionValuesId(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionsId(ctx) + HubContext.DELIMITER + "conditionValues" + HubContext.DELIMITER + propertyContainerId;
    }

    public String createTaskConditionValueId(TaskContext ctx, String propertyContainerId, String name) {
        return createTaskConditionValuesId(ctx, propertyContainerId) + HubContext.DELIMITER + name;
    }
}
