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
import com.whizzosoftware.hobson.api.variable.ImmutableHobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableMediaType;

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
        map.put(PropertyConstants.PREFERRED_VARIABLE_NAME, device.getPreferredVariableName());

        pctx.setMap(idProvider.createDeviceId(device.getContext()), map);
        pctx.commit();
    }

    public HobsonDevice restoreDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(ctx));
        String name = (String)deviceMap.get(PropertyConstants.NAME);
        String type = (String)deviceMap.get(PropertyConstants.TYPE);

        if (name != null && type != null) {
            return new HobsonDeviceStub.Builder(ctx).
                name(name).
                type(DeviceType.valueOf(type)).
                manufacturerName((String)deviceMap.get(PropertyConstants.MANUFACTURER_NAME)).
                manufacturerVersion((String)deviceMap.get(PropertyConstants.MANUFACTURER_VERSION)).
                modelName((String)deviceMap.get(PropertyConstants.MODEL_NAME)).
                preferredVariableName((String)deviceMap.get(PropertyConstants.PREFERRED_VARIABLE_NAME)).
                build();
        } else {
            return null;
        }
    }

    public void saveDeviceVariable(CollectionPersistenceContext pctx, DeviceContext dctx, HobsonVariable var) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.PLUGIN_ID, var.getPluginId());
        map.put(PropertyConstants.DEVICE_ID, var.getDeviceId());
        map.put(PropertyConstants.NAME, var.getName());
        map.put(PropertyConstants.MASK, var.getMask().toString());
        map.put(PropertyConstants.LAST_UPDATE, var.getLastUpdate());
        map.put(PropertyConstants.VALUE, var.getValue());
        if (var.hasMediaType()) {
            map.put(PropertyConstants.MEDIA_TYPE, var.getMediaType().toString());
        }

        pctx.setMap(idProvider.createDeviceVariableId(dctx, var.getName()), map);
        pctx.commit();
    }

    public HobsonVariable restoreDeviceVariable(CollectionPersistenceContext pctx, DeviceContext ctx, String name) {
        Map<String,Object> varMap = pctx.getMap(idProvider.createDeviceVariableId(ctx, name));
        String proxyType = (String)varMap.get(PropertyConstants.MEDIA_TYPE);
        return new ImmutableHobsonVariable(
            (String)varMap.get(PropertyConstants.PLUGIN_ID),
            (String)varMap.get(PropertyConstants.DEVICE_ID),
            (String)varMap.get(PropertyConstants.NAME),
            varMap.containsKey(PropertyConstants.MASK) ? HobsonVariable.Mask.valueOf((String)varMap.get(PropertyConstants.MASK)) : null,
                varMap.get(PropertyConstants.VALUE), proxyType != null ? VariableMediaType.valueOf(proxyType) : null, (Long)varMap.get(PropertyConstants.LAST_UPDATE)
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

    public void saveDevicePassport(CollectionPersistenceContext pctx, DevicePassport db) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, db.getId());
        map.put(PropertyConstants.DEVICE_ID, db.getDeviceId());
        map.put(PropertyConstants.SECRET, db.getSecret());
        map.put(PropertyConstants.CREATION_TIME, db.getCreationTime());
        if (db.isActivated()) {
            map.put(PropertyConstants.ACTIVATION_TIME, db.getActivationTime());
        }

        pctx.setMap(db.getId(), map);
        pctx.commit();
    }

    public DevicePassport restoreDevicePassport(CollectionPersistenceContext pctx, String id) {
        Map<String,Object> map = pctx.getMap(id);
        if (map != null) {
            String deviceId = (String) map.get(PropertyConstants.DEVICE_ID);
            if (deviceId != null) {
                DevicePassport db = new DevicePassport(
                    id,
                    deviceId,
                    (Long)map.get(PropertyConstants.CREATION_TIME),
                    (Long)map.get(PropertyConstants.ACTIVATION_TIME)
                );
                db.setSecret((String)map.get(PropertyConstants.SECRET));
                return db;
            }
        }
        return null;
    }

    public void deleteDevicePassport(CollectionPersistenceContext pctx, String id) {
        pctx.removeMap(id);
        pctx.commit();
    }

    public void saveTask(CollectionPersistenceContext pctx, HobsonTask task) {
        Map<String,Object> map = new HashMap<>();

        map.put(PropertyConstants.NAME, task.getName());
        map.put(PropertyConstants.DESCRIPTION, task.getDescription());
        map.put(PropertyConstants.CONTEXT, task.getContext().toString());

        // save task properties
        if (task.hasProperties()) {
            for (String name : task.getProperties().keySet()) {
                Map<String,Object> pmap = new HashMap<>();
                pmap.put(PropertyConstants.NAME, name);
                pmap.put(PropertyConstants.VALUE, task.getProperties().get(name));
                pctx.setMap(idProvider.createTaskPropertyId(task.getContext(), name), pmap);
            }
        }

        // save task conditions
        if (task.hasConditions()) {
            for (PropertyContainer pc : task.getConditions()) {
                Map<String,Object> cmap = new HashMap<>();
                if (pc.getId() != null) {
                    cmap.put(PropertyConstants.ID, pc.getId());
                    cmap.put(PropertyConstants.NAME, pc.getName());
                    cmap.put(PropertyConstants.CONTEXT, pc.getContainerClassContext().toString());
                    pctx.setMap(idProvider.createTaskConditionMetaId(task.getContext(), pc.getId()), cmap);
                    for (String pvalName : pc.getPropertyValues().keySet()) {
                        Map<String, Object> pvalmap = new HashMap<>();
                        pvalmap.put(PropertyConstants.NAME, pvalName);
                        pvalmap.put(PropertyConstants.VALUE, StringConversionUtil.createTypedValueString(pc.getPropertyValues().get(pvalName)));
                        pctx.setMap(idProvider.createTaskConditionValueId(task.getContext(), pc.getId(), pvalName), pvalmap);
                    }
                } else {
                    throw new HobsonNotFoundException("Unable to save condition with null ID: " + pc.getContainerClassContext());
                }
            }
        }

        // save task action set ID
        map.put(PropertyConstants.ACTION_SET_ID, task.getActionSet().getId());

        pctx.setMap(idProvider.createTaskMetaId(task.getContext()), map);
        pctx.commit();
    }

    public HobsonTask restoreTask(CollectionPersistenceContext pctx, TaskContext taskContext) {
        Map<String,Object> taskMap = pctx.getMap(idProvider.createTaskMetaId(taskContext));

        HobsonTask task = new HobsonTask(
            taskContext,
            (String)taskMap.get(PropertyConstants.NAME),
            (String)taskMap.get(PropertyConstants.DESCRIPTION),
            null,
            null,
            null
        );

        // restore properties
        List<Map<String,Object>> mapList = pctx.getMapsWithPrefix(idProvider.createTaskPropertiesId(taskContext));
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                task.setProperty((String) map.get(PropertyConstants.NAME), map.get(PropertyConstants.VALUE));
            }
        }

        // restore conditions
        List<PropertyContainer> conditions = new ArrayList<>();
        mapList = pctx.getMapsWithPrefix(idProvider.createTaskConditionMetasId(taskContext));
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                // restore condition values
                Map<String, Object> values = new HashMap<>();
                String conditionId = (String) map.get(PropertyConstants.ID);
                List<Map<String, Object>> valueList = pctx.getMapsWithPrefix(idProvider.createTaskConditionValuesId(taskContext, conditionId));
                for (Map<String, Object> vmap : valueList) {
                    values.put((String) vmap.get(PropertyConstants.NAME), StringConversionUtil.castTypedValueString((String) vmap.get(PropertyConstants.VALUE)));
                }
                conditions.add(new PropertyContainer(conditionId, (String) map.get(PropertyConstants.NAME), PropertyContainerClassContext.create((String) map.get("context")), values));
            }
        }
        task.setConditions(conditions);

        // restore action set
        task.setActionSet(new PropertyContainerSet((String)taskMap.get(PropertyConstants.ACTION_SET_ID)));

        return task;
    }

    public void deleteTask(CollectionPersistenceContext pctx, TaskContext context) {
        pctx.removeMap(idProvider.createTaskMetaId(context));
        pctx.commit();
    }

    public PresenceEntity restorePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        Map<String,Object> map = pctx.getMap(idProvider.createPresenceEntityId(pectx));
        if (map != null && map.size() > 0) {
            return new PresenceEntity(pectx, (String)map.get(PropertyConstants.NAME), (Long)map.get(PropertyConstants.LAST_UPDATE));
        }
        return null;
    }

    public void savePresenceEntity(CollectionPersistenceContext pctx, PresenceEntity pe) {
        String key = idProvider.createPresenceEntityId(pe.getContext());

        Map<String,Object> map = pctx.getMap(key);
        map.put(PropertyConstants.CONTEXT, pe.getContext().toString());
        map.put(PropertyConstants.NAME, pe.getName());
        map.put(PropertyConstants.LAST_UPDATE, pe.getLastUpdate());

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
                return new PresenceLocation(
                    plctx,
                    (String) map.get(PropertyConstants.NAME),
                    (Double) map.get(PropertyConstants.LATITUDE),
                    (Double) map.get(PropertyConstants.LONGITUDE),
                    (Double) map.get(PropertyConstants.RADIUS),
                    (Integer) map.get(PropertyConstants.BEACON_MAJOR),
                    (Integer) map.get(PropertyConstants.BEACON_MINOR)
                );
            }
        }
        return null;
    }

    public void savePresenceLocation(CollectionPersistenceContext pctx, PresenceLocation pl) {
        String key = idProvider.createPresenceLocationId(pl.getContext());

        Map<String,Object> map = pctx.getMap(key);
        map.put(PropertyConstants.CONTEXT, pl.getContext().toString());
        map.put(PropertyConstants.NAME, pl.getName());
        map.put(PropertyConstants.LATITUDE, pl.getLatitude());
        map.put(PropertyConstants.LONGITUDE, pl.getLongitude());
        map.put(PropertyConstants.RADIUS, pl.getRadius());
        map.put(PropertyConstants.BEACON_MAJOR, pl.getBeaconMajor());
        map.put(PropertyConstants.BEACON_MINOR, pl.getBeaconMinor());

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
        map.put(PropertyConstants.ID, actionSet.getId());
        if (actionSet.getName() != null) {
            map.put(PropertyConstants.NAME, actionSet.getName());
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
        map.put(PropertyConstants.ACTIONS, sb.toString());

        pctx.setMap(key, map);
        pctx.commit();
    }

    public PropertyContainerSet restoreActionSet(HubContext ctx, CollectionPersistenceContext pctx, TaskManager manager, String actionSetId) {
        String key = idProvider.createActionSetId(ctx, actionSetId);

        Map<String,Object> map = pctx.getMap(key);

        if (map != null && map.size() > 0) {
            PropertyContainerSet tas = new PropertyContainerSet(actionSetId);
            if (map.containsKey(PropertyConstants.NAME)) {
                tas.setName((String)map.get(PropertyConstants.NAME));
            }
            StringTokenizer tok = new StringTokenizer((String)map.get(PropertyConstants.ACTIONS), ",");
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
        map.put(PropertyConstants.ID, action.getId());
        map.put(PropertyConstants.PLUGIN_ID, action.getContainerClassContext().getPluginId());
        map.put(PropertyConstants.CONTAINER_CLASS_ID, action.getContainerClassContext().getContainerClassId());

        saveActionProperties(ctx, pctx, action);

        pctx.setMap(key, map);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, TaskManager manager, HubContext ctx, String actionId) {
        String key = idProvider.createActionId(ctx, actionId);

        Map<String,Object> map = pctx.getMap(key);

        if (manager != null) {
            return new PropertyContainer(
                actionId,
                PropertyContainerClassContext.create(PluginContext.create(ctx, (String) map.get(PropertyConstants.PLUGIN_ID)), (String)map.get(PropertyConstants.CONTAINER_CLASS_ID)),
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
