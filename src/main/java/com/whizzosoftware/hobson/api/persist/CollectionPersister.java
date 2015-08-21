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

    public void saveTask(CollectionPersistenceContext pctx, HobsonTask task) {
        Map<String,Object> map = new HashMap<>();

        map.put("name", task.getName());
        map.put("description", task.getDescription());
        if (task.hasProperties()) {
            map.put("properties", new HashMap<>(task.getProperties()));
        }
        if (task.hasConditions()) {
            List<Map<String,Object>> conditionList = new ArrayList<>();
            map.put("conditions", conditionList);
            for (PropertyContainer pc : task.getConditions()) {
                conditionList.add(createPropertyContainerMap(pc));
            }
        }
        map.put("actionSetId", task.getActionSet().getId());

        pctx.setMap(task.getContext().toString(), map);
        pctx.commit();
    }

    public HobsonTask restoreTask(CollectionPersistenceContext pctx, TaskContext taskContext) {
        Map<String,Object> map = pctx.getMap(taskContext.toString());

        return new HobsonTask(
            taskContext,
            (String)map.get("name"),
            (String)map.get("description"),
            (Map<String,Object>)map.get("properties"),
            createPropertyContainerList((List<Map<String,Object>>)map.get("conditions")),
            new PropertyContainerSet((String)map.get("actionSetId"))
        );
    }

    public void saveActionSet(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        String key = createActionSetKey(ctx, actionSet.getId());

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
        String key = createActionSetKey(ctx, actionSetId);

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
        String key = createActionKey(ctx, action.getId());

        Map<String,Object> map = pctx.getMap(key);
        map.put("id", action.getId());
        map.put("pluginId", action.getContainerClassContext().getPluginId());
        map.put("containerClassId", action.getContainerClassContext().getContainerClassId());

        saveActionProperties(ctx, pctx, action);

        pctx.setMap(key, map);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, TaskManager manager, HubContext ctx, String actionId) {
        String key = createActionKey(ctx, actionId);

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
        String key = createActionPropertiesKey(ctx, action.getId());

        Map<String,Object> map = pctx.getMap(key);

        for (String k : action.getPropertyValues().keySet()) {
            map.put(k, StringConversionUtil.createTypedValueString(action.getPropertyValues().get(k)));
        }

        pctx.setMap(key, map);
    }

    public Map<String,Object> restoreActionProperties(HubContext ctx, CollectionPersistenceContext pctx, String actionId) {
        String key = createActionPropertiesKey(ctx, actionId);

        Map<String,Object> map = pctx.getMap(key);

        Map<String,Object> resultMap = new HashMap<>();
        for (String k : map.keySet()) {
            resultMap.put(k, StringConversionUtil.castTypedValueString((String) map.get(k)));
        }

        return resultMap;
    }

    public String getActionSetIdFromKey(HubContext ctx, String key) {
        if (key.startsWith(createActionSetKeyPrefix(ctx))) {
            return key.substring(key.lastIndexOf(":") + 1);
        }
        return null;
    }

    protected String createActionSetKeyPrefix(HubContext ctx) {
        return ctx + HubContext.DELIMITER + "actionSets";
    }

    protected String createActionSetKey(HubContext ctx, String actionSetId) {
        return createActionSetKeyPrefix(ctx) + HubContext.DELIMITER + actionSetId;
    }

    protected String createActionKeyPrefix(HubContext ctx) {
        return ctx + HubContext.DELIMITER + "actions";
    }

    protected String createActionKey(HubContext ctx, String actionId) {
        return createActionKeyPrefix(ctx) + HubContext.DELIMITER + actionId;
    }

    protected String createActionPropertiesKey(HubContext ctx, String actionId) {
        return createActionKey(ctx, actionId) + HubContext.DELIMITER + "properties";
    }

    protected Map<String,Object> createPropertyContainerMap(PropertyContainer pc) {
        Map<String,Object> map = new HashMap<>();
        map.put("ctx", pc.getContainerClassContext().toString());
        map.put("name", pc.getName());
        map.put("values", new HashMap<>(pc.getPropertyValues()));
        return map;
    }

    protected List<PropertyContainer> createPropertyContainerList(List<Map<String,Object>> l) {
        List<PropertyContainer> results = new ArrayList<>();
        if (l != null) {
            for (Map<String, Object> m : l) {
                results.add(createPropertyContainer(m));
            }
        }
        return results;
    }

    protected PropertyContainer createPropertyContainer(Map<String,Object> map) {
        return new PropertyContainer(
            PropertyContainerClassContext.create((String)map.get("ctx")),
            (Map<String,Object>)map.get("values")
        );
    }
}
