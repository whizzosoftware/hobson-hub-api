/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.DeviceBootstrap;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;
import com.whizzosoftware.hobson.api.task.HobsonTask;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.util.StringConversionUtil;

import java.util.*;

/**
 * A class that allows persistence of Hobson objects using Java Collection classes. This can be utilized
 * by file-persistence libraries like MapDB as well as key-value stores like Redis.
 *
 * @author Dan Noguerol
 */
public class CollectionPersister {

    public void saveDeviceBootstrap(CollectionPersistenceContext pctx, DeviceBootstrap db) {
        Map<String,Object> map = new HashMap<>();
        map.put("id", db.getId());
        map.put("deviceId", db.getDeviceId());
        map.put("secret", db.getSecret());
        map.put("creationTime", db.getCreationTime());
        if (db.hasBootstrapTime()) {
            map.put("bootstrapTime", db.getBootstrapTime());
        }

        pctx.setMap(db.getId(), map);
        pctx.commit();
    }

    public void saveTask(CollectionPersistenceContext pctx, HobsonTask task) {
        Map<String,Object> map = new HashMap<>();

        map.put("name", task.getName());
        map.put("description", task.getDescription());
        map.put("context", task.getContext().toString());

        // save task properties
        if (task.hasProperties()) {
            for (String name : task.getProperties().keySet()) {
                Map<String,Object> pmap = new HashMap<>();
                pmap.put("name", name);
                pmap.put("value", task.getProperties().get(name));
                pctx.setMap(KeyUtil.createTaskPropertyKey(task.getContext(), name), pmap);
            }
        }

        // save task conditions
        if (task.hasConditions()) {
            for (PropertyContainer pc : task.getConditions()) {
                Map<String,Object> cmap = new HashMap<>();
                cmap.put("id", pc.getId());
                cmap.put("name", pc.getName());
                cmap.put("context", pc.getContainerClassContext().toString());
                pctx.setMap(KeyUtil.createTaskConditionMetaKey(task.getContext(), pc.getId()), cmap);
                for (String pvalName : pc.getPropertyValues().keySet()) {
                    Map<String,Object> pvalmap = new HashMap<>();
                    pvalmap.put("name", pvalName);
                    pvalmap.put("value", StringConversionUtil.createTypedValueString(pc.getPropertyValues().get(pvalName)));
                    pctx.setMap(KeyUtil.createTaskConditionValueKey(task.getContext(), pc.getId(), pvalName), pvalmap);
                }
            }
        }

        // save task action set ID
        map.put("actionSetId", task.getActionSet().getId());

        pctx.setMap(KeyUtil.createTaskMetaKey(task.getContext()), map);
        pctx.commit();
    }

    public void deleteDeviceBootstrap(CollectionPersistenceContext pctx, String id) {
        pctx.removeMap(id);
        pctx.commit();
    }

    public void deleteTask(CollectionPersistenceContext pctx, TaskContext context) {
        pctx.removeMap(KeyUtil.createTaskMetaKey(context));
        pctx.commit();
    }

    public DeviceBootstrap restoreDeviceBootstrap(CollectionPersistenceContext pctx, String id) {
        Map<String,Object> map = pctx.getMap(id);
        if (map != null) {
            String deviceId = (String) map.get("deviceId");
            if (deviceId != null) {
                DeviceBootstrap db = new DeviceBootstrap(id, deviceId, (Long)map.get("creationTime"), (Long)map.get("bootstrapTime"));
                db.setSecret((String)map.get("secret"));
                return db;
            }
        }
        return null;
    }

    public HobsonTask restoreTask(CollectionPersistenceContext pctx, TaskContext taskContext) {
        Map<String,Object> taskMap = pctx.getMap(KeyUtil.createTaskMetaKey(taskContext));

        HobsonTask task = new HobsonTask(taskContext, (String)taskMap.get("name"), (String)taskMap.get("description"), null, null, null);

        // restore properties
        List<Map<String,Object>> mapList = pctx.getMapsWithPrefix(KeyUtil.createTaskPropertiesKey(taskContext));
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                task.setProperty((String) map.get("name"), map.get("value"));
            }
        }

        // restore conditions
        List<PropertyContainer> conditions = new ArrayList<>();
        mapList = pctx.getMapsWithPrefix(KeyUtil.createTaskConditionMetasKey(taskContext));
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                // restore condition values
                Map<String, Object> values = new HashMap<>();
                String conditionId = (String) map.get("id");
                List<Map<String, Object>> valueList = pctx.getMapsWithPrefix(KeyUtil.createTaskConditionValuesKey(taskContext, conditionId));
                for (Map<String, Object> vmap : valueList) {
                    values.put((String) vmap.get("name"), StringConversionUtil.castTypedValueString((String) vmap.get("value")));
                }
                conditions.add(new PropertyContainer(conditionId, (String) map.get("name"), PropertyContainerClassContext.create((String) map.get("context")), values));
            }
        }
        task.setConditions(conditions);

        // restore action set
        task.setActionSet(new PropertyContainerSet((String)taskMap.get("actionSetId")));

        return task;
    }

    public void saveActionSet(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        String key = KeyUtil.createActionSetKey(ctx, actionSet.getId());

        Map<String,Object> map = new HashMap<>();
        map.put("id", actionSet.getId());
        if (actionSet.getName() != null) {
            map.put("name", actionSet.getName());
        }

        StringBuilder sb = new StringBuilder();
        List<? extends PropertyContainer> actions = actionSet.getProperties();
        for (int i=0; i < actions.size(); i++) {
            PropertyContainer action = actions.get(i);
            if (!action.hasId()) {
                action.setId(UUID.randomUUID().toString());
            }
            sb.append(action.getId());
            if (i < actions.size() - 1) {
                sb.append(",");
            }
            saveAction(ctx, pctx, action);
        }
        map.put("actions", sb.toString());

        pctx.setMap(key, map);
        pctx.commit();
    }

    public PropertyContainerSet restoreActionSet(HubContext ctx, CollectionPersistenceContext pctx, TaskManager manager, String actionSetId) {
        String key = KeyUtil.createActionSetKey(ctx, actionSetId);

        Map<String,Object> map = pctx.getMap(key);

        if (map != null && map.size() > 0) {
            PropertyContainerSet tas = new PropertyContainerSet(actionSetId);
            if (map.containsKey("name")) {
                tas.setName((String)map.get("name"));
            }
            StringTokenizer tok = new StringTokenizer((String)map.get("actions"), ",");
            List<PropertyContainer> actions = new ArrayList<>();
            while (tok.hasMoreTokens()) {
                actions.add(restoreAction(pctx, manager, ctx, tok.nextToken()));
            }
            tas.setProperties(actions);
            return tas;
        } else {
            throw new HobsonNotFoundException("Unable to find action set: " + actionSetId);
        }
    }

    public void saveAction(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = KeyUtil.createActionKey(ctx, action.getId());

        Map<String,Object> map = pctx.getMap(key);
        map.put("id", action.getId());
        map.put("pluginId", action.getContainerClassContext().getPluginId());
        map.put("containerClassId", action.getContainerClassContext().getContainerClassId());

        saveActionProperties(ctx, pctx, action);

        pctx.setMap(key, map);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, TaskManager manager, HubContext ctx, String actionId) {
        String key = KeyUtil.createActionKey(ctx, actionId);

        Map<String,Object> map = pctx.getMap(key);

        if (manager != null) {
            return new PropertyContainer(
                actionId,
                PropertyContainerClassContext.create(PluginContext.create(ctx, (String) map.get("pluginId")), (String)map.get("containerClassId")),
                restoreActionProperties(ctx, pctx, actionId)
            );
        } else {
            throw new HobsonRuntimeException("No task manager available to create action objects");
        }
    }

    public void saveActionProperties(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = KeyUtil.createActionPropertiesKey(ctx, action.getId());

        Map<String,Object> map = pctx.getMap(key);

        for (String k : action.getPropertyValues().keySet()) {
            map.put(k, StringConversionUtil.createTypedValueString(action.getPropertyValues().get(k)));
        }

        pctx.setMap(key, map);
    }

    public Map<String,Object> restoreActionProperties(HubContext ctx, CollectionPersistenceContext pctx, String actionId) {
        String key = KeyUtil.createActionPropertiesKey(ctx, actionId);

        Map<String,Object> map = pctx.getMap(key);

        Map<String,Object> resultMap = new HashMap<>();
        for (String k : map.keySet()) {
            resultMap.put(k, StringConversionUtil.castTypedValueString((String) map.get(k)));
        }

        return resultMap;
    }

    public String getActionSetIdFromKey(HubContext ctx, String key) {
        return key.substring(key.lastIndexOf(HubContext.DELIMITER) + 1);
    }
}
