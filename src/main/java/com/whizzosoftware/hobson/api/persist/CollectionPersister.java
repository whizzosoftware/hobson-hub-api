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

    public void deleteDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        pctx.remove(idProvider.createDeviceId(ctx));
    }

    public void deleteDevicePassport(CollectionPersistenceContext pctx, String id) {
        pctx.remove(id);
        pctx.commit();
    }

    public void deleteDeviceVariable(CollectionPersistenceContext pctx, VariableContext vctx) {
        pctx.remove(idProvider.createVariableId(vctx));
    }

    public void deleteTask(CollectionPersistenceContext pctx, TaskContext context) {
        pctx.remove(idProvider.createTaskId(context));
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

    public String getActionSetIdFromKey(HubContext ctx, String key) {
        return key.substring(key.lastIndexOf(HubContext.DELIMITER) + 1);
    }

    public boolean hasDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(ctx));
        return (deviceMap != null && deviceMap.size() > 0);
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

    public Map<String,Object> restoreActionProperties(HubContext ctx, CollectionPersistenceContext pctx, String actionId) {
        String key = idProvider.createActionPropertiesId(ctx, actionId);

        Map<String,Object> map = pctx.getMap(key);

        Map<String,Object> resultMap = new HashMap<>();
        for (String k : map.keySet()) {
            resultMap.put(k, StringConversionUtil.castTypedValueString((String) map.get(k)));
        }

        return resultMap;
    }

    public PropertyContainerSet restoreActionSet(HubContext ctx, CollectionPersistenceContext pctx, TaskManager manager, String actionSetId) {
        String key = idProvider.createActionSetId(ctx, actionSetId);

        Map<String,Object> map = pctx.getMap(key);

        if (map != null && map.size() > 0) {
            PropertyContainerSet tas = new PropertyContainerSet(actionSetId);
            if (map.containsKey(PropertyConstants.NAME)) {
                tas.setName((String)map.get(PropertyConstants.NAME));
            }
            List<PropertyContainer> actions = new ArrayList<>();
            for (Object o : pctx.getSet(idProvider.createActionSetActionsId(ctx, actionSetId))) {
                actions.add(restoreAction(pctx, manager, ctx, (String)o));
            }
            tas.setProperties(actions);
            return tas;
        } else {
            throw new HobsonNotFoundException("Unable to find action set: " + actionSetId);
        }
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

    public DevicePassport restoreDevicePassport(CollectionPersistenceContext pctx, HubContext hctx, String id) {
        Map<String,Object> map = pctx.getMap(id);
        if (map != null) {
            String deviceId = (String) map.get(PropertyConstants.DEVICE_ID);
            if (deviceId != null) {
                DevicePassport db = new DevicePassport(
                        hctx,
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
        Map<String,Object> taskMap = pctx.getMap(idProvider.createTaskId(tctx));

        HobsonTask task = new HobsonTask(
                tctx,
                (String)taskMap.get(PropertyConstants.NAME),
                (String)taskMap.get(PropertyConstants.DESCRIPTION),
                null,
                null,
                new PropertyContainerSet((String)taskMap.get(PropertyConstants.ACTION_SET_ID))
        );

        // restore properties
        Map<String,Object>  map = pctx.getMap(idProvider.createTaskPropertiesId(tctx));
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
                                values
                        )
                );
            }
            task.setConditions(conditions);
        }

        pctx.commit();

        return task;
    }

    public HobsonUser restoreUser(CollectionPersistenceContext pctx, String userId) {
        UserAccount account = null;

        Map<String,Object> userMap = pctx.getMap(idProvider.createUserId(userId));
        Set<Object> hubSet = pctx.getSet(idProvider.createUserHubsId(userId));
        if (userMap.containsKey(PropertyConstants.EXPIRE_TIME)) {
            account = new UserAccount((long)userMap.get(PropertyConstants.EXPIRE_TIME), (hubSet != null && hubSet.size() > 0));
        }

        return new HobsonUser.Builder(userId).
            givenName((String)userMap.get(PropertyConstants.GIVEN_NAME)).
            familyName((String)userMap.get(PropertyConstants.FAMILY_NAME)).
            email((String)userMap.get(PropertyConstants.EMAIL)).
            account(account).
            build();
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

    public void saveActionSet(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        String key = idProvider.createActionSetId(ctx, actionSet.getId());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, actionSet.getId());
        if (actionSet.getName() != null) {
            map.put(PropertyConstants.NAME, actionSet.getName());
        }

        List<? extends PropertyContainer> actions = actionSet.getProperties();
        for (int i=0; i < actions.size(); i++) {
            PropertyContainer action = actions.get(i);
            if (!action.hasId()) {
                action.setId(UUID.randomUUID().toString());
            }
            saveAction(ctx, pctx, action);
            pctx.addSetValue(idProvider.createActionSetActionsId(ctx, actionSet.getId()), action.getId());
        }

        pctx.setMap(key, map);

        pctx.addSetValue(idProvider.createActionSetsId(ctx), actionSet.getId());

        pctx.commit();
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
        if (config.containsKey("name")) {
            pctx.setMapValue(idProvider.createDeviceId(dctx), "name", config.get("name"));
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
        pctx.setMap(db.getId(), map);
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
        if (task.hasConditions()) {
            for (PropertyContainer pc : task.getConditions()) {
                Map<String,Object> cmap = new HashMap<>();
                if (pc.getId() != null) {
                    cmap.put(PropertyConstants.CONTAINER_CLASS_ID, pc.getContainerClassContext().getContainerClassId());
                    cmap.put(PropertyConstants.ID, pc.getId());
                    if (pc.getName() != null) {
                        cmap.put(PropertyConstants.NAME, pc.getName());
                    }
                    if (pc.getContainerClassContext().hasPluginContext()) {
                        cmap.put(PropertyConstants.PLUGIN_ID, pc.getContainerClassContext().getPluginId());
                    }
                    pctx.setMap(idProvider.createTaskConditionId(task.getContext(), pc.getId()), cmap);
                    pctx.addSetValue(idProvider.createTaskConditionsId(task.getContext()), pc.getId());
                    Map<String, Object> m = new HashMap<>();
                    for (String pvalName : pc.getPropertyValues().keySet()) {
                        m.put(pvalName, pc.getPropertyValues().get(pvalName));
                    }
                    pctx.setMap(idProvider.createTaskConditionPropertiesId(task.getContext(), pc.getId()), m);
                } else {
                    throw new HobsonNotFoundException("Unable to save condition with null ID: " + pc.getContainerClassContext());
                }
            }
        }

        // save task action set ID
        map.put(PropertyConstants.ACTION_SET_ID, task.getActionSet().getId());

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
        map.put(PropertyConstants.LAST_UPDATE, pe.getLastUpdate());

        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createPresenceEntitiesId(pe.getContext().getHubContext()), pe.getContext().getEntityId());
        pctx.commit();
    }

    public void savePresenceLocation(CollectionPersistenceContext pctx, PresenceLocation pl) {
        String key = idProvider.createPresenceLocationId(pl.getContext());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.CONTEXT, pl.getContext().toString());
        map.put(PropertyConstants.NAME, pl.getName());
        map.put(PropertyConstants.LATITUDE, pl.getLatitude());
        map.put(PropertyConstants.LONGITUDE, pl.getLongitude());
        map.put(PropertyConstants.RADIUS, pl.getRadius());
        map.put(PropertyConstants.BEACON_MAJOR, pl.getBeaconMajor());
        map.put(PropertyConstants.BEACON_MINOR, pl.getBeaconMinor());

        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createPresenceLocationsId(pl.getContext().getHubContext()), pl.getContext().getLocationId());

        pctx.commit();
    }

    public void saveUser(CollectionPersistenceContext pctx, HobsonUser user, String encPassword) {
        String key = idProvider.createUserId(user.getId());

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, user.getId());
        map.put(PropertyConstants.PASSWORD, encPassword);
        map.put(PropertyConstants.GIVEN_NAME, user.getGivenName());
        map.put(PropertyConstants.FAMILY_NAME, user.getFamilyName());
        map.put(PropertyConstants.EMAIL, user.getEmail());
        if (user.getAccount() != null) {
            map.put(PropertyConstants.EXPIRE_TIME, user.getAccount().getExpiration());
        }

        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createUsersId(), user.getId());

        pctx.commit();
    }

    public void saveUserHubs(CollectionPersistenceContext pctx, String userId, Set<String> hubIds) {
        String key = idProvider.createUserHubsId(userId);
        pctx.remove(key);
        for (String hubId : hubIds) {
            pctx.addSetValue(key, hubId);
        }
    }
}
