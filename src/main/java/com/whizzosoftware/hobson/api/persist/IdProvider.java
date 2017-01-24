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

/**
 * An interface for classes that can provide IDs for common Hobson model entities.
 *
 * @author Dan Noguerol
 */
public interface IdProvider {
    TemplatedId createActionClassesId(HubContext ctx);
    TemplatedId createActionClassId(PropertyContainerClassContext ctx);
    TemplatedId createActionId(HubContext ctx, String actionId);
    TemplatedId createActionSetId(HubContext ctx, String actionSetId);
    TemplatedId createActionSetActionsId(HubContext ctx, String actionSetId);
    TemplatedId createActionSetsId(HubContext ctx);
    TemplatedId createActionPropertiesId(HubContext ctx, String actionId);
    TemplatedId createActivityLogId(HubContext ctx);
    TemplatedId createDataStreamsId(HubContext ctx);
    TemplatedId createDataStreamId(HubContext ctx, String dataStreamId);
    TemplatedId createDataStreamDataId(HubContext ctx, String dataStreamId);
    TemplatedId createDataStreamFieldId(HubContext ctx, String dataStreamId, String fieldId);
    TemplatedId createDataStreamFieldsId(HubContext ctx, String dataStreamId);
    TemplatedId createDataStreamTagsId(HubContext ctx, String dataStreamId);
    TemplatedId createDevicesId(HubContext ctx);
    DeviceContext createDeviceContext(String deviceId);
    DeviceContext createDeviceContextWithHub(HubContext ctx, String deviceId);
    TemplatedId createDeviceActionClassId(DeviceContext ctx, String actionClassId);
    TemplatedId createDeviceActionClassesId(DeviceContext ctx);
    TemplatedId createDeviceActionClassSupportedPropertiesId(DeviceContext dctx, String containerClassId);
    TemplatedId createDeviceId(DeviceContext ctx);
    TemplatedId createDeviceConfigurationId(DeviceContext ctx);
    TemplatedId createDeviceNameId(DeviceContext ctx);
    TemplatedId createDeviceVariableDescriptionId(DeviceVariableContext vctx);
    TemplatedId createDeviceVariableId(DeviceVariableContext vctx);
    TemplatedId createDeviceVariablesId(DeviceContext ctx);
    TemplatedId createDeviceConfigurationClassId(DeviceContext ctx);
    TemplatedId createDeviceTagsId(DeviceContext ctx);
    TemplatedId createPluginDeviceConfigurationClassesId(PluginContext ctx);
    TemplatedId createPluginDeviceConfigurationClassId(PluginContext ctx, String name);
    TemplatedId createGlobalVariableId(GlobalVariableContext gvctx);
    TemplatedId createGlobalVariablesId(HubContext ctx);
    TemplatedId createHubId(HubContext ctx);
    TemplatedId createHubConfigurationClassId(HubContext ctx);
    TemplatedId createHubConfigurationId(HubContext ctx);
    TemplatedId createHubLogId(HubContext ctx);
    TemplatedId createHubPasswordId(HubContext ctx);
    TemplatedId createHubSerialPortsId(HubContext ctx);
    TemplatedId createHubSerialPortId(HubContext ctx, String name);
    TemplatedId createJobId(HubContext ctx, String jobId);
    TemplatedId createLocalPluginActionClassesId(PluginContext ctx);
    TemplatedId createLocalPluginConfigurationId(PluginContext ctx);
    TemplatedId createLocalPluginConfigurationClassId(PluginContext ctx);
    TemplatedId createLocalPluginId(PluginContext ctx);
    TemplatedId createLocalPluginIconId(PluginContext ctx);
    TemplatedId createLocalPluginReloadId(PluginContext ctx);
    TemplatedId createLocalPluginsId(HubContext ctx);
    TemplatedId createPersonId(String userId);
    PluginContext createPluginContext(String pluginId);
    TemplatedId createPluginDevicesId(PluginContext pctx);
    TemplatedId createPresenceEntitiesId(HubContext ctx);
    PresenceEntityContext createPresenceEntityContext(String presenceEntityId);
    TemplatedId createPresenceEntityId(PresenceEntityContext ctx);
    TemplatedId createPresenceLocationsId(HubContext ctx);
    PresenceLocationContext createPresenceLocationContext(String presenceLocationId);
    TemplatedId createPresenceLocationId(PresenceLocationContext ctx);
    TemplatedId createPropertyContainerId(String id, PropertyContainerClass pcc);
    TemplatedId createPropertyContainerClassesId(PluginContext pctx);
    TemplatedId createPropertyContainerClassId(PropertyContainerClassContext pccc, PropertyContainerClassType type);
    TemplatedId createRemotePluginId(HubContext ctx, String pluginId, String version);
    TemplatedId createRemotePluginInstallId(HubContext ctx, String pluginId, String version);
    TemplatedId createRemotePluginsId(HubContext ctx);
    TemplatedId createRepositoriesId(HubContext ctx);
    TemplatedId createRepositoryId(HubContext ctx, String uri);
    TemplatedId createSendTestEmailId(HubContext ctx);
    TemplatedId createShutdownId(HubContext ctx);
    TemplatedId createTaskActionSetId(HubContext ctx, String id);
    TemplatedId createTaskActionSetsId(HubContext ctx);
    TemplatedId createTaskConditionClassesId(HubContext ctx);
    TemplatedId createTaskConditionClassId(PropertyContainerClassContext ctx);
    TemplatedId createTaskConditionId(TaskContext ctx, String propertyContainerId);
    TemplatedId createTaskConditionPropertiesId(TaskContext ctx, String propertyContainerId);
    TemplatedId createTaskConditionsId(TaskContext ctx);
    TemplatedId createTaskId(TaskContext ctx);
    TemplatedId createTaskPropertiesId(TaskContext ctx);
    TemplatedId createTasksId(HubContext ctx);
    TemplatedId createUserId(String userId);
    TemplatedId createUserHubsId(String userId);
    TemplatedId createUsersId();
    DeviceVariableContext createDeviceVariableContext(String variableId);
}
