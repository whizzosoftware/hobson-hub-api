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

/**
 * An interface for classes that can provide IDs for common Hobson model entities.
 *
 * @author Dan Noguerol
 */
public interface IdProvider {
    String createActionId(HubContext ctx, String actionId);
    String createActionSetId(HubContext ctx, String actionSetId);
    String createActionSetActionsId(HubContext ctx, String actionSetId);
    String createActionSetsId(HubContext ctx);
    String createActionPropertiesId(HubContext ctx, String actionId);
    String createActivityLogId(HubContext ctx);
    String createAllHubsId();
    String createDevicesId(HubContext ctx);
    DeviceContext createDeviceContext(String deviceId);
    DeviceContext createDeviceContextWithHub(HubContext ctx, String deviceId);
    String createDeviceId(DeviceContext ctx);
    String createDevicePassportId(HubContext ctx, String deviceId);
    String createDevicePassportsId(HubContext ctx);
    String createDeviceConfigurationId(DeviceContext ctx);
    String createDeviceConfigurationClassId(DeviceContext ctx);
    String createDeviceVariablesId(DeviceContext ctx);
    String createGlobalVariableId(HubContext ctx, String name);
    String createGlobalVariablesId(HubContext ctx);
    String createHubId(HubContext ctx);
    String createHubConfigurationClassId(HubContext ctx);
    String createHubConfigurationId(HubContext ctx);
    String createHubLogId(HubContext ctx);
    String createHubUploadCredentialsId(HubContext ctx);
    String createLocalPluginConfigurationId(PluginContext ctx);
    String createLocalPluginConfigurationClassId(PluginContext ctx);
    String createLocalPluginId(PluginContext ctx);
    String createLocalPluginIconId(PluginContext ctx);
    String createLocalPluginReloadId(PluginContext ctx);
    String createLocalPluginsId(HubContext ctx);
    String createPersonId(String userId);
    PluginContext createPluginContext(String pluginId);
    String createPluginDevicesId(PluginContext pctx);
    String createPresenceEntitiesId(HubContext ctx);
    PresenceEntityContext createPresenceEntityContext(String presenceEntityId);
    String createPresenceEntityId(PresenceEntityContext ctx);
    String createPresenceLocationsId(HubContext ctx);
    PresenceLocationContext createPresenceLocationContext(String presenceLocationId);
    String createPresenceLocationId(PresenceLocationContext ctx);
    String createPropertyContainerId(PropertyContainerClass pcc);
    String createPropertyContainerClassId(PropertyContainerClassContext pccc, PropertyContainerClassType type);
    String createRemotePluginId(PluginContext ctx, String version);
    String createRemotePluginInstallId(PluginContext ctx, String version);
    String createRemotePluginsId(HubContext ctx);
    String createRepositoriesId(HubContext ctx);
    String createRepositoryId(HubContext ctx, String uri);
    String createShutdownId(HubContext ctx);
    String createTaskActionClassesId(HubContext ctx);
    String createTaskActionClassId(PropertyContainerClassContext ctx);
    String createTaskActionSetId(HubContext ctx, String id);
    String createTaskActionSetsId(HubContext ctx);
    String createTaskConditionClassesId(HubContext ctx);
    String createTaskConditionClassId(PropertyContainerClassContext ctx);
    String createTaskConditionId(TaskContext ctx, String propertyContainerId);
    String createTaskConditionPropertiesId(TaskContext ctx, String propertyContainerId);
    String createTaskConditionsId(TaskContext ctx);
    String createTaskId(TaskContext ctx);
    String createTaskPropertiesId(TaskContext ctx);
    String createTasksId(HubContext ctx);
    String createTelemetryDatasetId(HubContext ctx, String dataSetId);
    String createUserId(String userId);
    String createUserHubsId(String userId);
    VariableContext createVariableContext(String variableId);
    String createVariableId(VariableContext ctx);
    String createVariablesId(HubContext ctx);
}
