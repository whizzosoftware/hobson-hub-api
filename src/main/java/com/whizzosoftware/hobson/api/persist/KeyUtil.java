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
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.task.TaskContext;

public class KeyUtil {
    public static String createHubsKey(String userId) {
        return userId + HubContext.DELIMITER + "hubs";
    }

    public static String createHubKey(HubContext ctx) {
        return createHubsKey(ctx.getUserId()) + HubContext.DELIMITER + ctx.getHubId();
    }

    public static String createPluginsKey(HubContext ctx) {
        return createHubKey(ctx) + HubContext.DELIMITER + "plugins";
    }

    public static String createPluginKey(PluginContext ctx) {
        return createPluginsKey(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getPluginId();
    }

    public static String createPluginConfigurationKey(PluginContext ctx) {
        return createPluginKey(ctx) + HubContext.DELIMITER + "configuration";
    }

    public static String createDevicesKey(HubContext ctx) {
        return createHubKey(ctx) + HubContext.DELIMITER + "devices";
    }

    public static String createPluginDevicesKey(PluginContext ctx) {
        return createPluginKey(ctx) + HubContext.DELIMITER + "devices";
    }

    public static String createPluginDeviceKey(HubContext ctx, String pluginAndDeviceId) {
        return createHubKey(ctx) + HubContext.DELIMITER + "plugins" + HubContext.DELIMITER + pluginAndDeviceId;
    }

    public static String createGlobalVariablesKey(HubContext ctx) {
        return createHubKey(ctx) + HubContext.DELIMITER + "globalVariables";
    }

    public static String createGlobalVariableKey(HubContext ctx, String variableName) {
        return createGlobalVariablesKey(ctx) + HubContext.DELIMITER + variableName;
    }

    public static String createDeviceKey(DeviceContext ctx) {
        return createPluginDevicesKey(ctx.getPluginContext()) + HubContext.DELIMITER + ctx.getDeviceId();
    }

    public static String createDeviceConfigurationKey(DeviceContext ctx) {
        return createDeviceKey(ctx) + HubContext.DELIMITER + "configuration";
    }

    public static String createDeviceVariableKey(DeviceContext ctx, String variableName) {
        return createDeviceKey(ctx) + HubContext.DELIMITER + "variables" + HubContext.DELIMITER + variableName;
    }

    public static String createActionKey(HubContext ctx, String actionId) {
        return createHubKey(ctx) + HubContext.DELIMITER + "actions" + HubContext.DELIMITER + actionId;
    }

    public static String createActionSetKey(HubContext ctx, String actionSetId) {
        return createHubKey(ctx) + HubContext.DELIMITER + "actionSets" + HubContext.DELIMITER + actionSetId;
    }

    public static String createActionPropertiesKey(HubContext ctx, String actionId) {
        return createActionKey(ctx, actionId) + HubContext.DELIMITER + "properties";
    }

    public static String createTasksKey(HubContext ctx) {
        return createHubKey(ctx) + HubContext.DELIMITER + "tasks";
    }

    public static String createTaskMetaRootKey(HubContext ctx) {
        return createTasksKey(ctx) + HubContext.DELIMITER + "taskMeta";
    }

    public static String createTaskMetaKey(TaskContext ctx) {
        return createTaskMetaRootKey(ctx.getHubContext()) + HubContext.DELIMITER + ctx.getTaskId();
    }

    public static String createTaskPropertiesKey(TaskContext ctx) {
        return createTasksKey(ctx.getHubContext()) + HubContext.DELIMITER + "properties" + HubContext.DELIMITER + ctx.getTaskId();
    }

    public static String createTaskPropertyKey(TaskContext ctx, String name) {
        return createTaskPropertiesKey(ctx) + HubContext.DELIMITER + name;
    }

    public static String createTaskConditionsKey(TaskContext ctx) {
        return createTasksKey(ctx.getHubContext()) + HubContext.DELIMITER + "conditions" + HubContext.DELIMITER + ctx.getTaskId();
    }

    public static String createTaskConditionMetasKey(TaskContext ctx) {
        return createTaskConditionsKey(ctx) + HubContext.DELIMITER + "conditionMeta";
    }

    public static String createTaskConditionMetaKey(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionMetasKey(ctx) + HubContext.DELIMITER + propertyContainerId;
    }

    public static String createTaskConditionValuesKey(TaskContext ctx, String propertyContainerId) {
        return createTaskConditionsKey(ctx) + HubContext.DELIMITER + "conditionValues" + HubContext.DELIMITER + propertyContainerId;
    }

    public static String createTaskConditionValueKey(TaskContext ctx, String propertyContainerId, String name) {
        return createTaskConditionValuesKey(ctx, propertyContainerId) + HubContext.DELIMITER + name;
    }
}
