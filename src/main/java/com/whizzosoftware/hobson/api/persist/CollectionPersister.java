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
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.util.StringConversionUtil;

import java.util.*;

/**
 * A class that allows disk-based persistence of Hobson objects using Java Collection classes.
 *
 * @author Dan Noguerol
 */
public class CollectionPersister {

    public void saveActionSet(CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        String key = createActionSetKey(actionSet.getId());

        Map<String,String> map = pctx.getMap(key);
        map.put("id", actionSet.getId());
        if (actionSet.getName() != null) {
            map.put("name", actionSet.getName());
        }

        StringBuilder sb = new StringBuilder();
        List<? extends PropertyContainer> actions = actionSet.getProperties();
        for (int i=0; i < actions.size(); i++) {
            PropertyContainer action = actions.get(i);
            sb.append(action.getId());
            if (i < actions.size() - 1) {
                sb.append(",");
            }
            saveAction(pctx, action);
        }
        map.put("actions", sb.toString());

        pctx.commit();
    }

    public PropertyContainerSet restoreActionSet(CollectionPersistenceContext pctx, TaskManager manager, HubContext ctx, String actionSetId) {
        String key = createActionSetKey(actionSetId);

        Map<String,String> map = pctx.getMap(key);

        if (map != null && map.size() > 0) {
            PropertyContainerSet tas = new PropertyContainerSet();
            StringTokenizer tok = new StringTokenizer(map.get("actions"), ",");
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

    public void saveAction(CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = createActionKey(action.getId());

        Map<String,String> map = pctx.getMap(key);
        map.put("id", action.getId());

        saveActionProperties(pctx, action);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, TaskManager manager, HubContext ctx, String actionId) {
        String key = createActionKey(actionId);

        Map<String,String> map = pctx.getMap(key);

        if (manager != null) {
            return new PropertyContainer(
                actionId,
                PropertyContainerClassContext.create(PluginContext.create(ctx, map.get("pluginId")), "actionclass1"),
                restoreActionProperties(pctx, ctx, actionId)
            );
        } else {
            throw new HobsonRuntimeException("No task manager available to create action objects");
        }
    }

    public void saveActionProperties(CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = createActionPropertiesKey(action.getId());

        Map<String,String> map = pctx.getMap(key);

        for (String k : action.getPropertyValues().keySet()) {
            map.put(k, StringConversionUtil.createTypedValueString(action.getPropertyValues().get(k)));
        }
    }

    public Map<String,Object> restoreActionProperties(CollectionPersistenceContext pctx, HubContext ctx, String actionId) {
        String key = createActionPropertiesKey(actionId);

        Map<String,String> map = pctx.getMap(key);

        Map<String,Object> resultMap = new HashMap<>();
        for (String k : map.keySet()) {
            resultMap.put(k, StringConversionUtil.castTypedValueString(map.get(k)));
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

    protected String createActionSetKey(String actionSetId) {
        return actionSetId;
    }

    protected String createActionKey(String actionId) {
        return actionId;
    }

    protected String createActionPropertiesKey(String actionId) {
        return actionId + HubContext.DELIMITER + "properties";
    }
}
