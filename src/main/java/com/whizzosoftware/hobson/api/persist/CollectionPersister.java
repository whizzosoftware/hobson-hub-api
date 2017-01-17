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

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.data.DataStreamField;
import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.presence.PresenceEntity;
import com.whizzosoftware.hobson.api.presence.PresenceEntityContext;
import com.whizzosoftware.hobson.api.presence.PresenceLocation;
import com.whizzosoftware.hobson.api.presence.PresenceLocationContext;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.task.HobsonTask;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.data.DataStream;
import com.whizzosoftware.hobson.api.util.StringConversionUtil;
import com.whizzosoftware.hobson.api.variable.*;

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
        pctx.remove(idProvider.createActionPropertiesId(hctx, id).getId());
        pctx.remove(idProvider.createActionId(hctx, id).getId());
    }

    public void deleteActionSet(CollectionPersistenceContext pctx, HubContext hctx, String id) {
        String key = idProvider.createActionSetActionsId(hctx, id).getId();
        for (Object o : pctx.getSet(key)) {
            deleteAction(hctx, pctx, o.toString());
        }
        pctx.remove(key);
        pctx.remove(idProvider.createActionSetId(hctx, id).getId());
        pctx.removeFromSet(idProvider.createActionSetsId(hctx).getId(), id);
    }

    public void deleteDataStream(CollectionPersistenceContext pctx, HubContext hctx, String id) {
        String s = idProvider.createDataStreamFieldsId(hctx, id).getId();
        Set<Object> fields = pctx.getSet(s);
        for (Object key : fields) {
            pctx.remove(idProvider.createDataStreamFieldId(hctx, id, (String)key).getId());
        }
        pctx.remove(s);
        pctx.remove(idProvider.createDataStreamTagsId(hctx, id).getId());
        pctx.remove(idProvider.createDataStreamId(hctx, id).getId());
        pctx.removeFromSet(idProvider.createDataStreamsId(hctx).getId(), id);
    }

    public void deleteDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        pctx.remove(idProvider.createDeviceId(ctx).getId());
        pctx.removeFromSet(idProvider.createDevicesId(ctx.getHubContext()).getId(), idProvider.createDeviceId(ctx).getId());
    }

    public void deleteDeviceConfiguration(CollectionPersistenceContext pctx, DeviceContext ctx, boolean commit) {
        pctx.remove(idProvider.createDeviceConfigurationId(ctx).getId());
        if (commit) {
            pctx.commit();
        }
    }

    public void deleteDeviceVariable(CollectionPersistenceContext pctx, DeviceVariableContext vctx) {
        pctx.remove(idProvider.createDeviceVariableId(vctx).getId());
        pctx.removeFromSet(idProvider.createDeviceVariablesId(vctx.getDeviceContext()).getId(), vctx.getName());
    }

    public void deleteHubConfiguration(CollectionPersistenceContext pctx, HubContext hctx, boolean commit) {
        pctx.remove(idProvider.createHubConfigurationId(hctx).getId());
        if (commit) {
            pctx.commit();
        }
    }

    public void deleteLocalPluginConfiguration(CollectionPersistenceContext cpctx, PluginContext pctx, boolean commit) {
        cpctx.remove(idProvider.createLocalPluginConfigurationId(pctx).getId());
        if (commit) {
            cpctx.commit();
        }
    }

    public void deleteTask(CollectionPersistenceContext pctx, TaskContext tctx) {
        String actionSetId = (String)pctx.getMapValue(idProvider.createTaskId(tctx).getId(), PropertyConstants.ACTION_SET_ID);
        pctx.remove(idProvider.createTaskId(tctx).getId());
        pctx.removeFromSet(idProvider.createTasksId(tctx.getHubContext()).getId(), tctx.getTaskId());
        pctx.remove(idProvider.createTaskPropertiesId(tctx).getId());
        deleteTaskConditions(pctx, tctx);
        deleteActionSet(pctx, tctx.getHubContext(), actionSetId);
        pctx.commit();
    }

    public void deletePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        pctx.remove(idProvider.createPresenceEntityId(pectx).getId());
        pctx.removeFromSet(idProvider.createPresenceEntitiesId(pectx.getHubContext()).getId(), pectx.getEntityId());
        pctx.commit();
    }

    public void deletePresenceLocation(CollectionPersistenceContext pctx, PresenceLocationContext plctx) {
        pctx.remove(idProvider.createPresenceLocationId(plctx).getId());
        pctx.removeFromSet(idProvider.createPresenceLocationsId(plctx.getHubContext()).getId(), plctx.getLocationId());
        pctx.commit();
    }

    public void deleteTaskCondition(CollectionPersistenceContext pctx, TaskContext tctx, String id) {
        pctx.remove(idProvider.createTaskConditionId(tctx, id).getId());
        pctx.remove(idProvider.createTaskConditionPropertiesId(tctx, id).getId());
        pctx.removeFromSet(idProvider.createTaskConditionsId(tctx).getId(), id);
    }

    public void deleteTaskConditions(CollectionPersistenceContext pctx, TaskContext tctx) {
        for (Object o : pctx.getSet(idProvider.createTaskConditionsId(tctx).getId())) {
            deleteTaskCondition(pctx, tctx, o.toString());
        }
    }

    public String getActionSetIdFromKey(HubContext ctx, String key) {
        return key.substring(key.lastIndexOf(HubContext.DELIMITER) + 1);
    }

    public boolean hasDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(ctx).getId());
        return (deviceMap != null && deviceMap.size() > 0);
    }

    public PropertyContainer restoreAction(CollectionPersistenceContext pctx, HubContext ctx, String actionId) {
        String key = idProvider.createActionId(ctx, actionId).getId();

        Map<String,Object> map = pctx.getMap(key);

        return new PropertyContainer(
            actionId,
            PropertyContainerClassContext.create(PluginContext.create(ctx, (String) map.get(PropertyConstants.PLUGIN_ID)), (String)map.get(PropertyConstants.CONTAINER_CLASS_ID)),
            restoreActionProperties(ctx, pctx, actionId)
        );
    }

    public Map<String,Object> restoreActionProperties(HubContext ctx, CollectionPersistenceContext pctx, String actionId) {
        String key = idProvider.createActionPropertiesId(ctx, actionId).getId();

        Map<String,Object> map = pctx.getMap(key);

        Map<String,Object> resultMap = new HashMap<>();
        for (String k : map.keySet()) {
            resultMap.put(k, StringConversionUtil.castTypedValueString((String) map.get(k)));
        }

        return resultMap;
    }

    public PropertyContainerSet restoreActionSet(HubContext ctx, CollectionPersistenceContext pctx, String actionSetId) {
        String key = idProvider.createActionSetId(ctx, actionSetId).getId();

        Map<String,Object> map = pctx.getMap(key);

        if (map != null && map.size() > 0) {
            PropertyContainerSet tas = new PropertyContainerSet(actionSetId);
            if (map.containsKey(PropertyConstants.NAME)) {
                tas.setName((String)map.get(PropertyConstants.NAME));
            }
            List<PropertyContainer> actions = new ArrayList<>();
            for (Object o : pctx.getSet(idProvider.createActionSetActionsId(ctx, actionSetId).getId())) {
                actions.add(restoreAction(pctx, ctx, (String)o));
            }
            tas.setProperties(actions);
            return tas;
        }

        return null;
    }

    public DataStream restoreDataStream(CollectionPersistenceContext pctx, HubContext hctx, String dataStreamId) {
        List<DataStreamField> fields = new ArrayList<>();

        Set<Object> set = pctx.getSet(idProvider.createDataStreamFieldsId(hctx, dataStreamId).getId());
        for (Object fieldId : set) {
            Map<String,Object> map2 = pctx.getMap(idProvider.createDataStreamFieldId(hctx, dataStreamId, (String)fieldId).getId());
            fields.add(new DataStreamField((String)fieldId, (String)map2.get("name"), DeviceVariableContext.create((String)map2.get("variableId"))));
        }

        HashSet<String> tags = new HashSet<>();
        for (Object o : pctx.getSet(idProvider.createDataStreamTagsId(hctx, dataStreamId).getId())) {
            tags.add(o.toString());
        }

        Map<String,Object> map2 = pctx.getMap(idProvider.createDataStreamId(hctx, dataStreamId).getId());
        return new DataStream(dataStreamId, (String)map2.get(PropertyConstants.NAME), fields, tags);
    }

    public HobsonDeviceDescriptor restoreDevice(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(ctx).getId());
        String name = (String)deviceMap.get(PropertyConstants.NAME);
        String type = (String)deviceMap.get(PropertyConstants.TYPE);

        if (name != null && type != null) {
            // restore configuration class
            PropertyContainerClass pcc = (PropertyContainerClass)deviceMap.get("cclass");

            // restore variable descriptions
            List<DeviceVariableDescriptor> descriptions = new ArrayList<>();
            Set<Object> vdSet = pctx.getSet(idProvider.createDeviceVariablesId(ctx).getId());
            for (Object o : vdSet) {
                String vname = o.toString();
                descriptions.add(restoreDeviceVariableDescription(pctx, ctx, vname));
            }

            // restore action classes
            List<ActionClass> actionClasses = null;
            Set<Object> acSet = pctx.getSet(idProvider.createDeviceActionClassesId(ctx).getId());
            if (acSet != null && acSet.size() > 0) {
                actionClasses = new ArrayList<>();
                for (Object o : acSet) {
                    String actionClassId = o.toString();
                    actionClasses.add(restoreDeviceActionClass(pctx, ctx, actionClassId));
                }
            }

            // restore tags
            Set<String> tags = null;
            Set<Object> tagSet = pctx.getSet(idProvider.createDeviceTagsId(ctx).getId());
            if (tagSet != null && tagSet.size() > 0) {
                tags = new HashSet<>();
                for (Object o : tagSet) {
                    tags.add(o.toString());
                }
            }

            return new HobsonDeviceDescriptor.Builder(ctx).
                name(name).
                type(DeviceType.valueOf(type)).
                configurationClass(pcc).
                manufacturerName((String)deviceMap.get(PropertyConstants.MANUFACTURER_NAME)).
                manufacturerVersion((String)deviceMap.get(PropertyConstants.MANUFACTURER_VERSION)).
                modelName((String)deviceMap.get(PropertyConstants.MODEL_NAME)).
                preferredVariableName((String)deviceMap.get(PropertyConstants.PREFERRED_VARIABLE_NAME)).
                variableDescriptions(descriptions).
                actionClasses(actionClasses).
                tags(tags).
                build();
        } else {
            return null;
        }
    }

    public Map<String,Object> restoreDeviceConfiguration(CollectionPersistenceContext pctx, DeviceContext ctx) {
        Map<String,Object> map = pctx.getMap(idProvider.createDeviceConfigurationId(ctx).getId());
        return map != null ? map : new HashMap<String,Object>();
    }

    public Long restoreDeviceLastCheckIn(CollectionPersistenceContext pctx, DeviceContext dctx) {
        return (Long)pctx.getMapValue(idProvider.createDeviceId(dctx).getId(), PropertyConstants.LAST_CHECKIN);
    }

    public String restoreDeviceName(CollectionPersistenceContext pctx, DeviceContext dctx) {
        Map<String,Object> deviceMap = pctx.getMap(idProvider.createDeviceId(dctx).getId());
        return (String)deviceMap.get(PropertyConstants.NAME);
    }

    public Set<String> restoreDeviceTags(CollectionPersistenceContext pctx, DeviceContext dctx) {
        Set<String> results = new HashSet<>();
        Set<Object> tags = pctx.getSet(idProvider.createDeviceTagsId(dctx).getId());
        for (Object o : tags) {
            results.add(o.toString());
        }
        return results;
    }

    public DeviceVariableDescriptor restoreDeviceVariableDescription(CollectionPersistenceContext pctx, DeviceContext ctx, String name) {
        Map<String,Object> map = pctx.getMap(idProvider.createDeviceVariableDescriptionId(DeviceVariableContext.create(ctx, name)).getId());
        return new DeviceVariableDescriptor(
            DeviceVariableContext.create((String)map.get(PropertyConstants.CONTEXT)),
            VariableMask.valueOf((String)map.get(PropertyConstants.MASK)),
            map.containsKey(PropertyConstants.MEDIA_TYPE) ? VariableMediaType.valueOf((String)map.get(PropertyConstants.MEDIA_TYPE)) : null
        );
    }

    public ActionClass restoreDeviceActionClass(CollectionPersistenceContext pctx, DeviceContext ctx, String actionClassId) {
        Map<String,Object> map = pctx.getMap(idProvider.createDeviceActionClassId(ctx, actionClassId).getId());
        ActionClass ac = new ActionClass(
            PropertyContainerClassContext.create(ctx, actionClassId),
            (String)map.get(PropertyConstants.NAME),
            (String)map.get(PropertyConstants.DESCRIPTION),
            (Boolean)map.get(PropertyConstants.TASK_ACTION),
            (Long)map.get(PropertyConstants.TIMEOUT_INTERVAL)
        );
        ac.setSupportedProperties((List<TypedProperty>)map.get(PropertyConstants.SUPPORTED_PROPERTIES)); // TODO
        return ac;
    }

    public Map<String,Object> restoreHubConfiguration(CollectionPersistenceContext cpctx, HubContext hctx, PropertyContainerClassContext pccctx) {
        Map<String,Object> map = cpctx.getMap(idProvider.createHubConfigurationId(hctx).getId());
        return map != null ? map : new HashMap<String,Object>();
    }

    public Map<String,Object> restoreLocalPluginConfiguration(CollectionPersistenceContext cpctx, PluginContext pctx) {
        Map<String,Object> map = cpctx.getMap(idProvider.createLocalPluginConfigurationId(pctx).getId());
        return map != null ? map : new HashMap<String,Object>();

    }

    public PresenceEntity restorePresenceEntity(CollectionPersistenceContext pctx, PresenceEntityContext pectx) {
        Map<String,Object> map = pctx.getMap(idProvider.createPresenceEntityId(pectx).getId());
        if (map != null && map.size() > 0) {
            return new PresenceEntity(pectx, (String)map.get(PropertyConstants.NAME), (Long)map.get(PropertyConstants.LAST_UPDATE));
        }
        return null;
    }

    public PresenceLocation restorePresenceLocation(CollectionPersistenceContext pctx, PresenceLocationContext plctx) {
        if (plctx != null) {
            Map<String, Object> map = pctx.getMap(idProvider.createPresenceLocationId(plctx).getId());
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

    public HobsonTask restoreTask(CollectionPersistenceContext taskCtx, TaskContext tctx) {
        HobsonTask task = null;
        Map<String,Object> taskMap = taskCtx.getMap(idProvider.createTaskId(tctx).getId());
        if (taskMap != null && taskMap.size() > 0) {
            task = new HobsonTask(
                tctx,
                (String)taskMap.get(PropertyConstants.NAME),
                (String)taskMap.get(PropertyConstants.DESCRIPTION),
                taskMap.containsKey(PropertyConstants.ENABLED) ? (Boolean)taskMap.get(PropertyConstants.ENABLED) : true,
                null,
                null,
                new PropertyContainerSet((String)taskMap.get(PropertyConstants.ACTION_SET_ID))
            );

            // restore properties
            Map<String, Object> map = taskCtx.getMap(idProvider.createTaskPropertiesId(tctx).getId());
            if (map != null) {
                for (String name : map.keySet()) {
                    task.setProperty(name, map.get(name));
                }
            }

            // restore conditions
            Set<Object> set = taskCtx.getSet(idProvider.createTaskConditionsId(tctx).getId());
            if (set != null) {
                List<PropertyContainer> conditions = new ArrayList<>();
                for (Object o : set) {
                    String conditionId = (String)o;
                    map = taskCtx.getMap(idProvider.createTaskConditionId(tctx, conditionId).getId());
                    Map<String, Object> values = taskCtx.getMap(idProvider.createTaskConditionPropertiesId(tctx, conditionId).getId());
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
        String key = idProvider.createActionId(ctx, action.getId()).getId();

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, action.getId());
        map.put(PropertyConstants.PLUGIN_ID, action.getContainerClassContext().getPluginId());
        map.put(PropertyConstants.CONTAINER_CLASS_ID, action.getContainerClassContext().getContainerClassId());

        saveActionProperties(ctx, pctx, action);

        pctx.setMap(key, map);
    }

    public void saveActionProperties(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainer action) {
        String key = idProvider.createActionPropertiesId(ctx, action.getId()).getId();

        Map<String,Object> map = new HashMap<>();

        for (String k : action.getPropertyValues().keySet()) {
            map.put(k, StringConversionUtil.createTypedValueString(action.getPropertyValues().get(k)));
        }

        pctx.setMap(key, map);
    }

    public String saveActionSet(HubContext ctx, CollectionPersistenceContext pctx, PropertyContainerSet actionSet) {
        if (actionSet.hasId()) {
            deleteActionSet(pctx, ctx, actionSet.getId());
        } else {
            actionSet.setId(UUID.randomUUID().toString());
        }

        String key = idProvider.createActionSetId(ctx, actionSet.getId()).getId();

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
                pctx.addSetValue(idProvider.createActionSetActionsId(ctx, actionSet.getId()).getId(), action.getId());
            }
        }

        pctx.setMap(key, map);

        pctx.addSetValue(idProvider.createActionSetsId(ctx).getId(), actionSet.getId());

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
        pctx.setMap(idProvider.createTaskConditionId(tctx, pc.getId()).getId(), cmap);
        pctx.addSetValue(idProvider.createTaskConditionsId(tctx).getId(), pc.getId());
        if (pc.hasPropertyValues()) {
            Map<String, Object> m = new HashMap<>();
            for (String pvalName : pc.getPropertyValues().keySet()) {
                m.put(pvalName, pc.getPropertyValues().get(pvalName));
            }
            pctx.setMap(idProvider.createTaskConditionPropertiesId(tctx, pc.getId()).getId(), m);
        }
    }

    public void saveDataStream(CollectionPersistenceContext pctx, HubContext hctx, DataStream dataStream) {
        // save data stream meta data
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.ID, dataStream.getId());
        map.put(PropertyConstants.NAME, dataStream.getName());

        pctx.setMap(idProvider.createDataStreamId(hctx, dataStream.getId()).getId(), map);

        // save data stream variables
        for (DataStreamField dsf : dataStream.getFields()) {
            Map<String,Object> map2 = new HashMap<>();
            String dsfid = dsf.getId();
            if (dsfid == null) {
                throw new HobsonRuntimeException("Data stream field with no ID found");
            }
            String fid = idProvider.createDataStreamFieldId(hctx, dataStream.getId(), dsfid).getId();
            map2.put("id", dsfid);
            map2.put("name", dsf.getName());
            map2.put("variableId", dsf.getVariable().toString());
            pctx.setMap(fid, map2);

            if (dataStream.hasTags()) {
                pctx.setSet(idProvider.createDataStreamTagsId(hctx, dataStream.getId()).getId(), (Set<Object>)(Set<?>)dataStream.getTags());
            }

            pctx.addSetValue(idProvider.createDataStreamFieldsId(hctx, dataStream.getId()).getId(), dsfid);
        }

        // add data stream ID to set of hub data streams
        pctx.addSetValue(idProvider.createDataStreamsId(hctx).getId(), dataStream.getId());

        // commit
        pctx.commit();
    }

    public void saveDevice(CollectionPersistenceContext pctx, HobsonDeviceDescriptor device) {
        // create device map
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.NAME, device.getName());
        map.put(PropertyConstants.TYPE, device.getType() != null ? device.getType().toString() : null);
        map.put("cclass", device.getConfigurationClass());
        map.put(PropertyConstants.MANUFACTURER_NAME, device.getManufacturerName());
        map.put(PropertyConstants.MANUFACTURER_VERSION, device.getManufacturerVersion());
        map.put(PropertyConstants.MODEL_NAME, device.getModelName());
        map.put(PropertyConstants.PREFERRED_VARIABLE_NAME, device.getPreferredVariableName());

        // save the map
        String deviceId = idProvider.createDeviceId(device.getContext()).getId();
        pctx.setMap(deviceId, map);

        // save the device variables
        if (device.hasVariableDescriptions()) {
            for (DeviceVariableDescriptor vd : device.getVariables()) {
                saveDeviceVariableDescription(pctx, vd);
            }
        }

        // save the action classes
        if (device.hasActionClasses()) {
            for (ActionClass ac : device.getActionClasses()) {
                saveDeviceActionClass(pctx, ac);
            }
        }

        // save the tags
        if (device.hasTags()) {
            saveDeviceTags(pctx, device.getContext(), device.getTags());
        }

        // save device to list of hub devices
        pctx.addSetValue(idProvider.createDevicesId(device.getContext().getHubContext()).getId(), deviceId);

        // save device to list of plugin devices
        pctx.addSetValue(idProvider.createPluginDevicesId(device.getContext().getPluginContext()).getId(), deviceId);

        // commit
        pctx.commit();
    }

    public void saveDeviceConfiguration(CollectionPersistenceContext pctx, DeviceContext dctx, Map<String,Object> config) {
        pctx.setMap(idProvider.createDeviceConfigurationId(dctx).getId(), config);
        // also set the device name specifically if it has changed
        if (config.containsKey(PropertyConstants.NAME)) {
            pctx.setMapValue(idProvider.createDeviceId(dctx).getId(), PropertyConstants.NAME, config.get(PropertyConstants.NAME));
        }
        pctx.commit();
    }

    public void saveDeviceLastCheckIn(CollectionPersistenceContext pctx, DeviceContext dctx, long lastCheckin) {
        pctx.setMapValue(idProvider.createDeviceId(dctx).getId(), PropertyConstants.LAST_CHECKIN, lastCheckin);
        pctx.commit();
    }

    public void saveDeviceName(CollectionPersistenceContext pctx, DeviceContext dctx, String name) {
        String deviceId = idProvider.createDeviceId(dctx).getId();
        Map<String,Object> deviceMap = pctx.getMap(deviceId);
        Map<String,Object> map = new HashMap<>(deviceMap);
        map.put(PropertyConstants.NAME, name);
        pctx.setMap(deviceId, map);
        pctx.commit();

    }

    public void saveDeviceTags(CollectionPersistenceContext pctx, DeviceContext dctx, Set<String> tags) {
        String deviceTagId = idProvider.createDeviceTagsId(dctx).getId();
        pctx.setSet(deviceTagId, new HashSet<Object>(tags));
        pctx.commit();
    }

    public void saveGlobalVariable(CollectionPersistenceContext pctx, GlobalVariableDescriptor desc, GlobalVariable val) {
        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.HUB_ID, desc.getHubId());
        map.put(PropertyConstants.PLUGIN_ID, desc.getPluginId());
        map.put(PropertyConstants.NAME, desc.getName());
        map.put(PropertyConstants.LAST_UPDATE, val.getLastUpdate());
        map.put(PropertyConstants.VALUE, StringConversionUtil.createTypedValueString(val.getValue()));

        pctx.setMap(idProvider.createGlobalVariableId(desc.getContext()).getId(), map);
        pctx.commit();
    }

    public void saveHubConfiguration(CollectionPersistenceContext cpctx, HubContext hctx, Map<String,Object> config) {
        cpctx.setMap(idProvider.createHubConfigurationId(hctx).getId(), config);
        cpctx.commit();
    }

    public void saveLocalPluginConfiguration(CollectionPersistenceContext cpctx, PluginContext pctx, Map<String,Object> config) {
        cpctx.setMap(idProvider.createLocalPluginConfigurationId(pctx).getId(), config);
        cpctx.commit();
    }

    public void saveTask(CollectionPersistenceContext pctx, HobsonTask task) {
        Map<String,Object> map = new HashMap<>();

        map.put(PropertyConstants.NAME, task.getName());
        map.put(PropertyConstants.TASK_ID, task.getContext().getTaskId());
        map.put(PropertyConstants.ENABLED, task.isEnabled());
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
            pctx.setMap(idProvider.createTaskPropertiesId(task.getContext()).getId(), m);
        }

        // save task conditions
        deleteTaskConditions(pctx, task.getContext());
        if (task.hasConditions()) {
            for (PropertyContainer pc : task.getConditions()) {
                saveCondition(pctx, task.getContext(), pc);
            }
        }

        // save task action set
        if (task.getActionSet().hasId()) {
            map.put(PropertyConstants.ACTION_SET_ID, task.getActionSet().getId());
        } else {
            throw new HobsonRuntimeException("Attempt to save task without action set ID");
        }

        // set the task map
        pctx.setMap(idProvider.createTaskId(task.getContext()).getId(), map);

        // add task ID to task id list
        pctx.addSetValue(idProvider.createTasksId(task.getContext().getHubContext()).getId(), task.getContext().getTaskId());

        pctx.commit();
    }

    public void savePresenceEntity(CollectionPersistenceContext pctx, PresenceEntity pe) {
        String key = idProvider.createPresenceEntityId(pe.getContext()).getId();

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.CONTEXT, pe.getContext().toString());
        map.put(PropertyConstants.NAME, pe.getName());
        if (pe.getLastUpdate() != null) {
            map.put(PropertyConstants.LAST_UPDATE, pe.getLastUpdate());
        }

        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createPresenceEntitiesId(pe.getContext().getHubContext()).getId(), pe.getContext().getEntityId());
        pctx.commit();
    }

    public void savePresenceLocation(CollectionPersistenceContext pctx, PresenceLocation pl) {
        String key = idProvider.createPresenceLocationId(pl.getContext()).getId();

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
        pctx.addSetValue(idProvider.createPresenceLocationsId(pl.getContext().getHubContext()).getId(), pl.getContext().getLocationId());

        pctx.commit();
    }

    public void saveDeviceVariableDescription(CollectionPersistenceContext pctx, DeviceVariableDescriptor vd) {
        String key = idProvider.createDeviceVariableDescriptionId(vd.getContext()).getId();

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.CONTEXT, vd.getContext().toString());
        map.put(PropertyConstants.NAME, vd.getContext().getName());
        map.put(PropertyConstants.MASK, vd.getMask().toString());
        if (vd.hasMediaType()) {
            map.put(PropertyConstants.MEDIA_TYPE, vd.getMediaType().toString());
        }
        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createDeviceVariablesId(vd.getContext().getDeviceContext()).getId(), vd.getContext().getName());

        pctx.commit();
    }

    public void saveDeviceActionClass(CollectionPersistenceContext pctx, ActionClass ac) {
        DeviceContext dctx = DeviceContext.create(ac.getContext().getPluginContext(), ac.getContext().getDeviceId());
        String key = idProvider.createDeviceActionClassId(dctx, ac.getContext().getContainerClassId()).getId();

        Map<String,Object> map = new HashMap<>();
        map.put(PropertyConstants.NAME, ac.getName());
        map.put(PropertyConstants.DESCRIPTION, ac.getDescription());
        map.put(PropertyConstants.TASK_ACTION, ac.isTaskAction());
        map.put(PropertyConstants.TIMEOUT_INTERVAL, ac.getTimeoutInterval());
        map.put(PropertyConstants.SUPPORTED_PROPERTIES, ac.getSupportedProperties()); // TODO
        pctx.setMap(key, map);
        pctx.addSetValue(idProvider.createDeviceActionClassesId(dctx).getId(), ac.getContext().getContainerClassId());

        pctx.commit();
    }
}
