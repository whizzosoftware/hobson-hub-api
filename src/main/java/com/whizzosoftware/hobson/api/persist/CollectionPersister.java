/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

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
import com.whizzosoftware.hobson.api.telemetry.DataStream;
import com.whizzosoftware.hobson.api.user.HobsonUser;
import com.whizzosoftware.hobson.api.user.UserAccount;
import com.whizzosoftware.hobson.api.util.StringConversionUtil;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.ImmutableHobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableContext;
import com.whizzosoftware.hobson.api.variable.VariableMediaType;

import java.util.*;

/**
 * A class that allows persistence of Hobson objects using Java Collection classes. This can be utilized
 * by file-persistence libraries like MapDB as well as key-value stores like Redis.
 *
 * @author Dan Noguerol
 */
public class CollectionPersister {
    private IdProvider idProvider;

    public CollectionPersister(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public void deleteAction(HubContext hctx, CollectionPersistenceContext pctx, String id) {
        pctx.remove(idProvider.createActionPropertiesId(hctx, id));
        pctx.remove(idProvider.createActionId(hctx, id));
    }

    public void deleteActionSet(HubContext hctx, CollectionPersistenceContext pctx, String id) {
        String key = idProvider.createActionSetActionsId(hctx, id);
        for (Object o : pctx.getSet(key)) {
            deleteAction(hctx, pctx, o.toString());
        }
        pctx.remove(idProvider.createActionSetActionsId(hctx, id));
        pctx.remove(idProvider.createActionSetId(hctx, id));
        pctx.removeFromSet(idProvider.createActionSetsId(hctx), id);
    }

    public void deleteDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        pctx.remove(idProvider.createDeviceId(ctx));
        pctx.removeFromSet(idProvider.createDevicesId(ctx.getHubContext()), idProvider.createDeviceId(ctx));
    }

    public void deleteDevicePassport(CollectionPersistenceContext pctx, HubContext hctx, String id) {
        pctx.remove(idProvider.createDevicePassportId(hctx, id));
        pctx.removeFromSet(idProvider.createDevicePassportsId(hctx), id);
        pctx.commit();
    }

    public void deleteDeviceVariable(CollectionPersistenceContext pctx, VariableContext vctx) {
        pctx.remove(idProvider.createVariableId(vctx));
        pctx.removeFromSet(idProvider.createDeviceVariablesId(vctx.getDeviceContext()), vctx.getName());
    }

    public void deleteTask(CollectionPersistenceContext pctx, TaskContext tctx) {
        String actionSetId = (String)pctx.getMapValue(idProvider.createTaskId(tctx), PropertyConstants.ACTION_SET_ID);
        pctx.remove(idProvider.createTaskId(tctx));
        pctx.removeFromSet(idProvider.createTasksId(tctx.getHubContext()), tctx.getTaskId());
        pctx.remove(idProvider.createTaskPropertiesId(tctx));
        deleteTaskConditions(pctx, tctx);
        deleteActionSet(tctx.getHubContext(), pctx, actionSetId);
        pctx.commit();
    }

    public void deletePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        pctx.remove(idProvider.createPresenceEntityId(pectx));
        pctx.removeFromSet(idProvider.createPresenceEntitiesId(pectx.getHubContext()), pectx.getEntityId());
        pctx.commit();
    }

    public void deletePresenceLocation(CollectionPersistenceContext pctx, PresenceLocationContext plctx) {
        pctx.remove(idProvider.createPresenceLocationId(plctx));
        pctx.removeFromSet(idProvider.createPresenceLocationsId(plctx.getHubContext()), plctx.getLocationId());
        pctx.commit();
    }

    public void deleteTaskCondition(CollectionPersistenceContext pctx, TaskContext tctx, String id) {
        pctx.remove(idProvider.createTaskConditionId(tctx, id));
        pctx.remove(idProvider.createTaskConditionPropertiesId(tctx, id));
        pctx.removeFromSet(idProvider.createTaskConditionsId(tctx), id);
    }

    public void deleteTaskConditions(CollectionPersistenceContext pctx, TaskContext tctx) {
        for (Object o : pctx.getSet(idProvider.createTaskConditionsId(tctx))) {
            deleteTaskCondition(pctx, tctx, o.toString());
        }
    }

    public String getActionSetIdFromKey(HubContext ctx, String key) {
        return key.substring(key.lastIndexOf(HubContext.DELIMITER) + 1);
    }

    public boolean hasDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(ctx));
        return (deviceMap != null && deviceMap.size() > 0);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, HubContext ctx, String actionId) {
        String key = idProvider.createActionId(ctx, actionId);

        Map<String,Object> map = pctx.getMap(key);

        return new PropertyContainer(
            actionId,
            PropertyContainerClassContext.create(PluginContext.create(ctx, (String) map.get(PropertyConstants.PLUGIN_ID)), (String)map.get(PropertyConstants.CONTAINER_CLASS_ID)),
            restoreActionProperties(ctx, pctx, actionId)
        );
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

    public PropertyContainerSet restoreActionSet(HubContext ctx, CollectionPersistenceContext pctx, String actionSetId) {
        String key = idProvider.createActionSetId(ctx, actionSetId);

        Map<String,Object> map = pctx.getMap(key);

        if (map != null && map.size() > 0) {
            PropertyContainerSet tas = new PropertyContainerSet(actionSetId);
            if (map.containsKey(PropertyConstants.NAME)) {
                tas.setName((String)map.get(PropertyConstants.NAME));
            }
            List<PropertyContainer> actions = new ArrayList<>();
            for (Object o : pctx.getSet(idProvider.createActionSetActionsId(ctx, actionSetId))) {
                actions.add(restoreAction(pctx, ctx, (String)o));
            }
            tas.setProperties(actions);
            return tas;
        }

        return null;
    }

    public DataStream restoreDataStream(CollectionPersistenceContext pctx, String userId, String dataStreamId) {
        List<VariableContext> variables = new ArrayList<>();

        for (Object vctxs : pctx.getSet(idProvider.createDataStreamVariablesId(userId, dataStreamId))) {
            variables.add(VariableContext.create((String)vctxs));
        }

        Map<String,Object> map = pctx.getMap(idProvider.createDataStreamId(userId, dataStreamId));

        return new DataStream(userId, dataStreamId, (String)map.get(PropertyConstants.NAME), variables);
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

    public Long restoreDeviceLastCheckIn(CollectionPersistenceContext pctx, DeviceContext dctx) {
        return (Long)pctx.getMapValue(idProvider.createDeviceId(dctx), PropertyConstants.LAST_CHECKIN);
    }

    public DevicePassport restoreDevicePassport(CollectionPersistenceContext pctx, HubContext hctx, String passportId) {
        Map<String,Object> map = pctx.getMap(idProvider.createDevicePassportId(hctx, passportId));
        if (map != null) {
            String deviceId = (String) map.get(PropertyConstants.DEVICE_ID);
            if (deviceId != null) {
                DevicePassport db = new DevicePassport(
                        hctx,
                        passportId,
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

    public HobsonVariable restoreDeviceVariable(CollectionPersistenceContext pctx, DeviceContext ctx, String name) {
        Map<String,Object> varMap = pctx.getMap(idProvider.createVariableId(VariableContext.create(ctx, name)));
        if (varMap.size() > 0) {
            String proxyType = (String)varMap.get(PropertyConstants.MEDIA_TYPE);
            return new ImmutableHobsonVariable(
                    VariableContext.create(ctx, name),
                    varMap.containsKey(PropertyConstants.MASK) ? HobsonVariable.Mask.valueOf((String)varMap.get(PropertyConstants.MASK)) : null,
                    varMap.get(PropertyConstants.VALUE), (Long)varMap.get(PropertyConstants.LAST_UPDATE), proxyType != null ? VariableMediaType.valueOf(proxyType) : null
            );
        } else {
            return null;
        }
    }

    public PresenceEntity restorePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        Map<String,Object> map = pctx.getMap(idProvider.createPresenceEntityId(pectx));
        if (map != null && map.size() > 0) {
            return new PresenceEntity(pectx, (String)map.get(PropertyConstants.NAME), (Long)map.get(PropertyConstants.LAST_UPDATE));
        }
        return null;
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

    public HobsonTask restoreTask(CollectionPersistenceContext pctx, TaskContext tctx) {
        HobsonTask task = null;
        Map<String,Object> taskMap = pctx.getMap(idProvider.createTaskId(tctx));
        if (taskMap != null && taskMap.size() > 0) {
            task = new HobsonTask(
                    tctx,
                    (String)taskMap.get(PropertyConstants.NAME),
                    (String)taskMap.get(PropertyConstants.DESCRIPTION),
                    null,
                    null,
                    restoreActionSet(tctx.getHubContext(), pctx, (String)taskMap.get(PropertyConstants.ACTION_SET_ID))
            );

            // restore properties
            Map<String, Object> map = pctx.getMap(idProvider.createTaskPropertiesId(tctx));
            if (map != null) {
                for (String name : map.keySet()) {
                    task.setProperty(name, map.get(name));
                }
            }

            // restore conditions
            Set<Object> set = pctx.getSet(idProvider.createTaskConditionsId(tctx));
            if (set != null) {
                List<PropertyContainer> conditions = new ArrayList<>();
                for (Object o : set) {
                    String conditionId = (String)o;
                    map = pctx.getMap(idProvider.createTaskConditionId(tctx, conditionId));
                    Map<String, Object> values = pctx.getMap(idProvider.createTaskConditionPropertiesId(tctx, conditionId));
                    conditions.add(
                        new PropertyContainer(
                            conditionId,
                            (String)map.get(PropertyConstants.NAME),
                            PropertyContainerClassContext.create(
                                    PluginContext.create(tctx.getHubContext(), (String)map.get(PropertyConstants.PLUGIN_ID)),
                                    (String)map.get(PropertyConstants.CONTAINER_CLASS_ID)
                            ),
                            new HashMap<>(values)
                        )
                    );
                }
                task.setConditions(conditions);
            }
        }
        return task;
    }

    public void saveAction(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = idProvider.createActionId(ctx, action.getId());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, action.getId());
        map.put(PropertyConstants.PLUGIN_ID, action.getContainerClassContext().getPluginId());
        map.put(PropertyConstants.CONTAINER_CLASS_ID, action.getContainerClassContext().getContainerClassId());

        saveActionProperties(ctx, pctx, action);

        pctx.setMap(key, map);
    }

    public void saveActionProperties(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = idProvider.createActionPropertiesId(ctx, action.getId());

        Map<String,Object> map = new HashMap<>();

        for (String k : action.getPropertyValues().keySet()) {
            map.put(k, StringConversionUtil.createTypedValueString(action.getPropertyValues().get(k)));
        }

        pctx.setMap(key, map);
    }

    public String saveActionSet(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        if (actionSet.hasId()) {
            deleteActionSet(ctx, pctx, actionSet.getId());
        } else {
            actionSet.setId(UUID.randomUUID().toString());
        }

        String key = idProvider.createActionSetId(ctx, actionSet.getId());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, actionSet.getId());
        if (actionSet.getName() != null) {
            map.put(PropertyConstants.NAME, actionSet.getName());
        }

        List<? extends PropertyContainer> actions = actionSet.getProperties();
        if (actions != null) {
            for (PropertyContainer action : actions) {
                if (!action.hasId()) {
                    action.setId(UUID.randomUUID().toString());
                }
                saveAction(ctx, pctx, action);
                pctx.addSetValue(idProvider.createActionSetActionsId(ctx, actionSet.getId()), action.getId());
            }
        }

        pctx.setMap(key, map);

        pctx.addSetValue(idProvider.createActionSetsId(ctx), actionSet.getId());

        pctx.commit();

        return actionSet.getId();
    }

    public void saveCondition(CollectionPersistenceContext pctx, TaskContext tctx, PropertyContainer pc) {
        Map<String,Object> cmap = new HashMap<>();
        if (!pc.hasId()) {
            pc.setId(UUID.randomUUID().toString());
        }
        cmap.put(PropertyConstants.CONTAINER_CLASS_ID, pc.getContainerClassContext().getContainerClassId());
        cmap.put(PropertyConstants.ID, pc.getId());
        if (pc.getName() != null) {
            cmap.put(PropertyConstants.NAME, pc.getName());
        }
        if (pc.getContainerClassContext().hasPluginContext()) {
            cmap.put(PropertyConstants.PLUGIN_ID, pc.getContainerClassContext().getPluginId());
        }
        pctx.setMap(idProvider.createTaskConditionId(tctx, pc.getId()), cmap);
        pctx.addSetValue(idProvider.createTaskConditionsId(tctx), pc.getId());
        Map<String, Object> m = new HashMap<>();
        for (String pvalName : pc.getPropertyValues().keySet()) {
            m.put(pvalName, pc.getPropertyValues().get(pvalName));
        }
        pctx.setMap(idProvider.createTaskConditionPropertiesId(tctx, pc.getId()), m);
    }

    public void saveDataStream(CollectionPersistenceContext pctx, DataStream dataStream) {
        // save data stream meta data
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, dataStream.getId());
        map.put(PropertyConstants.NAME, dataStream.getName());

        pctx.setMap(idProvider.createDataStreamId(dataStream.getUserId(), dataStream.getId()), map);

        // save data stream variables
        String dsVarsId = idProvider.createDataStreamVariablesId(dataStream.getUserId(), dataStream.getId());
        for (VariableContext vc : dataStream.getVariables()) {
            pctx.addSetValue(dsVarsId, vc.toString());
        }

        // add data stream ID to set of hub data streams
        pctx.addSetValue(idProvider.createDataStreamsId(dataStream.getUserId()), dataStream.getId());
    }

    public void saveDevice(CollectionPersistenceContext pctx, HobsonDevice device) {
        // create device map
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.NAME, device.getName());
        map.put(PropertyConstants.TYPE, device.getType() != null ? device.getType().toString() : null);
        map.put(PropertyConstants.MANUFACTURER_NAME, device.getManufacturerName());
        map.put(PropertyConstants.MANUFACTURER_VERSION, device.getManufacturerVersion());
        map.put(PropertyConstants.MODEL_NAME, device.getModelName());
        map.put(PropertyConstants.PREFERRED_VARIABLE_NAME, device.getPreferredVariableName());

        // save the map
        String deviceId = idProvider.createDeviceId(device.getContext());
        pctx.setMap(deviceId, map);

        // save device to list of hub devices
        pctx.addSetValue(idProvider.createDevicesId(device.getContext().getHubContext()), deviceId);

        // commit
        pctx.commit();
    }

    public void saveDeviceConfiguration(CollectionPersistenceContext pctx, DeviceContext dctx, Map<String,Object> config) {
        pctx.setMap(idProvider.createDeviceConfigurationId(dctx), config);

        // also set the device name specifically if it has changed
        if (config.containsKey(PropertyConstants.NAME)) {
            pctx.setMapValue(idProvider.createDeviceId(dctx), PropertyConstants.NAME, config.get(PropertyConstants.NAME));
        }
    }

    public void saveDeviceLastCheckIn(CollectionPersistenceContext pctx, DeviceContext dctx, long lastCheckin) {
        pctx.setMapValue(idProvider.createDeviceId(dctx), PropertyConstants.LAST_CHECKIN, lastCheckin);
    }

    public void saveDevicePassport(CollectionPersistenceContext pctx, HubContext hctx, DevicePassport db) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, db.getId());
        map.put(PropertyConstants.DEVICE_ID, db.getDeviceId());
        map.put(PropertyConstants.SECRET, db.getSecret());
        map.put(PropertyConstants.CREATION_TIME, db.getCreationTime());
        if (db.isActivated()) {
            map.put(PropertyConstants.ACTIVATION_TIME, db.getActivationTime());
        }
        pctx.setMap(idProvider.createDevicePassportId(hctx, db.getId()), map);
        pctx.addSetValue(idProvider.createDevicePassportsId(hctx), db.getId());
        pctx.commit();
    }

    public void saveDeviceVariable(CollectionPersistenceContext pctx, HobsonVariable var) {
        // build a device variable map
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.PLUGIN_ID, var.getPluginId());
        map.put(PropertyConstants.DEVICE_ID, var.getDeviceId());
        map.put(PropertyConstants.NAME, var.getName());
        map.put(PropertyConstants.MASK, var.getMask() != null ? var.getMask().toString() : null);
        map.put(PropertyConstants.LAST_UPDATE, var.getLastUpdate());
        map.put(PropertyConstants.VALUE, var.getValue());
        if (var.hasMediaType()) {
            map.put(PropertyConstants.MEDIA_TYPE, var.getMediaType().toString());
        }

        // save the map
        pctx.setMap(idProvider.createVariableId(var.getContext()), map);

        // save the variable name in the device variable set
        pctx.addSetValue(idProvider.createDeviceVariablesId(var.getContext().getDeviceContext()), var.getName());

        // commit
        pctx.commit();
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

    public void saveTask(CollectionPersistenceContext pctx, HobsonTask task) {
        Map<String,Object> map = new HashMap<>();

        map.put(PropertyConstants.NAME, task.getName());
        map.put(PropertyConstants.TASK_ID, task.getContext().getTaskId());
        if (task.getDescription() != null) {
            map.put(PropertyConstants.DESCRIPTION, task.getDescription());
        }

        // save task properties
        if (task.hasProperties()) {
            Map<String,Object> taskPropMap = task.getProperties();
            Map<String,Object> m = new HashMap<>();
            for (String name : taskPropMap.keySet()) {
                m.put(name, taskPropMap.get(name));
            }
            pctx.setMap(idProvider.createTaskPropertiesId(task.getContext()), m);
        }

        // save task conditions
        deleteTaskConditions(pctx, task.getContext());
        if (task.hasConditions()) {
            for (PropertyContainer pc : task.getConditions()) {
                saveCondition(pctx, task.getContext(), pc);
            }
        }

        // save task action set
        map.put(PropertyConstants.ACTION_SET_ID, saveActionSet(task.getContext().getHubContext(), pctx, task.getActionSet()));

        // set the task map
        pctx.setMap(idProvider.createTaskId(task.getContext()), map);

        // add task ID to task id list
        pctx.addSetValue(idProvider.createTasksId(task.getContext().getHubContext()), task.getContext().getTaskId());

        pctx.commit();
    }

    public void savePresenceEntity(CollectionPersistenceContext pctx, PresenceEntity pe) {
        String key = idProvider.createPresenceEntityId(pe.getContext());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.CONTEXT, pe.getContext().toString());
        map.put(PropertyConstants.NAME, pe.getName());
        if (pe.getLastUpdate() != null) {
            map.put(PropertyConstants.LAST_UPDATE, pe.getLastUpdate());
        }

        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createPresenceEntitiesId(pe.getContext().getHubContext()), pe.getContext().getEntityId());
        pctx.commit();
    }

    public void savePresenceLocation(CollectionPersistenceContext pctx, PresenceLocation pl) {
        String key = idProvider.createPresenceLocationId(pl.getContext());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.CONTEXT, pl.getContext().toString());
        map.put(PropertyConstants.NAME, pl.getName());
        if (pl.getLatitude() != null && pl.getLongitude() != null && pl.getRadius() != null) {
            map.put(PropertyConstants.LATITUDE, pl.getLatitude());
            map.put(PropertyConstants.LONGITUDE, pl.getLongitude());
            map.put(PropertyConstants.RADIUS, pl.getRadius());
        }
        if (pl.getBeaconMajor() != null && pl.getBeaconMinor() != null) {
            map.put(PropertyConstants.BEACON_MAJOR, pl.getBeaconMajor());
            map.put(PropertyConstants.BEACON_MINOR, pl.getBeaconMinor());
        }

        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createPresenceLocationsId(pl.getContext().getHubContext()), pl.getContext().getLocationId());

        pctx.commit();
    }
}
