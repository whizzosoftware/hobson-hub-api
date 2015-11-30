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
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.presence.PresenceEntity;
import com.whizzosoftware.hobson.api.presence.PresenceEntityContext;
import com.whizzosoftware.hobson.api.presence.PresenceLocation;
import com.whizzosoftware.hobson.api.presence.PresenceLocationContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;
import com.whizzosoftware.hobson.api.task.HobsonTask;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.util.StringConversionUtil;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.HobsonVariableStub;

import java.util.*;

/**
 * A class that allows persistence of Hobson objects using Java Collection classes. This can be utilized
 * by file-persistence libraries like MapDB as well as key-value stores like Redis.
 *
 * @author Dan Noguerol
 */
public class CollectionPersister {
    private ContextPathIdProvider idProvider = new ContextPathIdProvider();

    public void saveDevice(CollectionPersistenceContext pctx, HobsonDevice device) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.NAME, device.getName());
        map.put(PropertyConstants.TYPE, device.getType().toString());
        map.put(PropertyConstants.MANUFACTURER_NAME, device.getManufacturerName());
        map.put(PropertyConstants.MANUFACTURER_VERSION, device.getManufacturerVersion());
        map.put(PropertyConstants.MODEL_NAME, device.getModelName());
        map.put(PropertyConstants.AVAILABLE, device.isAvailable());
        map.put(PropertyConstants.LAST_CHECKIN, device.getLastCheckIn());
        map.put(PropertyConstants.PREFERRED_VARIABLE_NAME, device.getPreferredVariableName());

        pctx.setMap(idProvider.createDeviceId(device.getContext()), map);
        pctx.commit();
    }

    public HobsonDevice restoreDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(ctx));
        return new HobsonDeviceStub.Builder(ctx).
            name((String)deviceMap.get(PropertyConstants.NAME)).
            type(DeviceType.valueOf((String)deviceMap.get(PropertyConstants.TYPE))).
            manufacturerName((String)deviceMap.get(PropertyConstants.MANUFACTURER_NAME)).
            manufacturerVersion((String)deviceMap.get(PropertyConstants.MANUFACTURER_VERSION)).
            modelName((String)deviceMap.get(PropertyConstants.MODEL_NAME)).
            available((Boolean)deviceMap.get(PropertyConstants.AVAILABLE)).
            lastCheckIn((Long)deviceMap.get(PropertyConstants.LAST_CHECKIN)).
            preferredVariableName((String)deviceMap.get(PropertyConstants.PREFERRED_VARIABLE_NAME)).
            build();
    }

    public void saveDeviceVariable(CollectionPersistenceContext pctx, DeviceContext dctx, HobsonVariable var) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.PLUGIN_ID, var.getPluginId());
        map.put(PropertyConstants.DEVICE_ID, var.getDeviceId());
        map.put(PropertyConstants.NAME, var.getName());
        map.put(PropertyConstants.MASK, var.getMask().toString());
        map.put(PropertyConstants.LAST_UPDATE, var.getLastUpdate());
        map.put(PropertyConstants.VALUE, var.getValue());

        pctx.setMap(idProvider.createDeviceVariableId(dctx, var.getName()), map);
        pctx.commit();
    }

    public HobsonVariable restoreDeviceVariable(CollectionPersistenceContext pctx, DeviceContext ctx, String name) {
        Map<String,Object> varMap = pctx.getMap(idProvider.createDeviceVariableId(ctx, name));
        return new HobsonVariableStub(
            (String)varMap.get(PropertyConstants.PLUGIN_ID),
            (String)varMap.get(PropertyConstants.DEVICE_ID),
            (String)varMap.get(PropertyConstants.NAME),
            varMap.containsKey(PropertyConstants.MASK) ? HobsonVariable.Mask.valueOf((String)varMap.get(PropertyConstants.MASK)) : null,
            (Long)varMap.get(PropertyConstants.LAST_UPDATE),
            varMap.get(PropertyConstants.VALUE),
            false
        );
    }

    public void saveGlobalVariable(CollectionPersistenceContext pctx, HubContext hctx, HobsonVariable var) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.PLUGIN_ID, var.getPluginId());
        map.put(PropertyConstants.NAME, var.getName());
        map.put(PropertyConstants.MASK, var.getMask().toString());
        map.put(PropertyConstants.LAST_UPDATE, var.getLastUpdate());
        map.put(PropertyConstants.VALUE, StringConversionUtil.createTypedValueString(var.getValue()));

        pctx.setMap(idProvider.createGlobalVariableId(hctx, var.getName()), map);
        pctx.commit();
    }

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

    public void deleteDeviceBootstrap(CollectionPersistenceContext pctx, String id) {
        pctx.removeMap(id);
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
                pctx.setMap(idProvider.createTaskPropertyId(task.getContext(), name), pmap);
            }
        }

        // save task conditions
        if (task.hasConditions()) {
            for (PropertyContainer pc : task.getConditions()) {
                Map<String,Object> cmap = new HashMap<>();
                if (pc.getId() != null) {
                    cmap.put("id", pc.getId());
                    cmap.put("name", pc.getName());
                    cmap.put("context", pc.getContainerClassContext().toString());
                    pctx.setMap(idProvider.createTaskConditionMetaId(task.getContext(), pc.getId()), cmap);
                    for (String pvalName : pc.getPropertyValues().keySet()) {
                        Map<String, Object> pvalmap = new HashMap<>();
                        pvalmap.put("name", pvalName);
                        pvalmap.put("value", StringConversionUtil.createTypedValueString(pc.getPropertyValues().get(pvalName)));
                        pctx.setMap(idProvider.createTaskConditionValueId(task.getContext(), pc.getId(), pvalName), pvalmap);
                    }
                } else {
                    throw new HobsonNotFoundException("Unable to save condition with null ID: " + pc.getContainerClassContext());
                }
            }
        }

        // save task action set ID
        map.put("actionSetId", task.getActionSet().getId());

        pctx.setMap(idProvider.createTaskMetaId(task.getContext()), map);
        pctx.commit();
    }

    public HobsonTask restoreTask(CollectionPersistenceContext pctx, TaskContext taskContext) {
        Map<String,Object> taskMap = pctx.getMap(idProvider.createTaskMetaId(taskContext));

        HobsonTask task = new HobsonTask(taskContext, (String)taskMap.get("name"), (String)taskMap.get("description"), null, null, null);

        // restore properties
        List<Map<String,Object>> mapList = pctx.getMapsWithPrefix(idProvider.createTaskPropertiesId(taskContext));
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                task.setProperty((String) map.get("name"), map.get("value"));
            }
        }

        // restore conditions
        List<PropertyContainer> conditions = new ArrayList<>();
        mapList = pctx.getMapsWithPrefix(idProvider.createTaskConditionMetasId(taskContext));
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                // restore condition values
                Map<String, Object> values = new HashMap<>();
                String conditionId = (String) map.get("id");
                List<Map<String, Object>> valueList = pctx.getMapsWithPrefix(idProvider.createTaskConditionValuesId(taskContext, conditionId));
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

    public void deleteTask(CollectionPersistenceContext pctx, TaskContext context) {
        pctx.removeMap(idProvider.createTaskMetaId(context));
        pctx.commit();
    }

    public PresenceEntity restorePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        Map<String,Object> map = pctx.getMap(idProvider.createPresenceEntityId(pectx));
        if (map != null && map.size() > 0) {
            return new PresenceEntity(pectx, (String)map.get("name"), (Long)map.get("lastUpdate"));
        }
        return null;
    }

    public void savePresenceEntity(CollectionPersistenceContext pctx, PresenceEntity pe) {
        String key = idProvider.createPresenceEntityId(pe.getContext());

        Map<String,Object> map = pctx.getMap(key);
        map.put("context", pe.getContext().toString());
        map.put("name", pe.getName());
        map.put("lastUpdate", pe.getLastUpdate());

        pctx.setMap(key, map);
        pctx.commit();
    }

    public void deletePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        pctx.removeMap(idProvider.createPresenceEntityId(pectx));
        pctx.commit();
    }

    public PresenceLocation restorePresenceLocation(CollectionPersistenceContext pctx, PresenceLocationContext plctx) {
        if (plctx != null) {
            Map<String, Object> map = pctx.getMap(idProvider.createPresenceLocationId(plctx));
            if (map != null && map.size() > 0) {
                return new PresenceLocation(plctx, (String) map.get("name"), (Double) map.get("latitude"), (Double) map.get("longitude"), (Double) map.get("radius"), (Integer) map.get("beaconMajor"), (Integer) map.get("beaconMinor"));
            }
        }
        return null;
    }

    public void savePresenceLocation(CollectionPersistenceContext pctx, PresenceLocation pl) {
        String key = idProvider.createPresenceLocationId(pl.getContext());

        Map<String,Object> map = pctx.getMap(key);
        map.put("context", pl.getContext().toString());
        map.put("name", pl.getName());
        map.put("latitude", pl.getLatitude());
        map.put("longitude", pl.getLongitude());
        map.put("radius", pl.getRadius());
        map.put("beaconMajor", pl.getBeaconMajor());
        map.put("beaconMinor", pl.getBeaconMinor());

        pctx.setMap(key, map);
        pctx.commit();
    }

    public void deletePresenceLocation(CollectionPersistenceContext pctx, PresenceLocationContext plctx) {
        pctx.removeMap(idProvider.createPresenceLocationId(plctx));
        pctx.commit();
    }

    public void saveActionSet(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        String key = idProvider.createActionSetId(ctx, actionSet.getId());

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
        String key = idProvider.createActionSetId(ctx, actionSetId);

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
        String key = idProvider.createActionId(ctx, action.getId());

        Map<String,Object> map = pctx.getMap(key);
        map.put("id", action.getId());
        map.put("pluginId", action.getContainerClassContext().getPluginId());
        map.put("containerClassId", action.getContainerClassContext().getContainerClassId());

        saveActionProperties(ctx, pctx, action);

        pctx.setMap(key, map);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, TaskManager manager, HubContext ctx, String actionId) {
        String key = idProvider.createActionId(ctx, actionId);

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
        String key = idProvider.createActionPropertiesId(ctx, action.getId());

        Map<String,Object> map = pctx.getMap(key);

        for (String k : action.getPropertyValues().keySet()) {
            map.put(k, StringConversionUtil.createTypedValueString(action.getPropertyValues().get(k)));
        }

        pctx.setMap(key, map);
    }

    public Map<String,Object> restoreActionProperties(HubContext ctx, CollectionPersistenceContext pctx, String actionId) {
        String key = idProvider.createActionPropertiesId(ctx, actionId);

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
