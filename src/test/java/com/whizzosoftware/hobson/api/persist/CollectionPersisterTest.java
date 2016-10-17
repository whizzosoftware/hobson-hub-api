/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.persist;

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
import com.whizzosoftware.hobson.api.task.MockTaskManager;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.data.DataStream;
import com.whizzosoftware.hobson.api.variable.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class CollectionPersisterTest {
    @Test
    public void testSaveRestoreDeleteTask() {
        HubContext hctx = HubContext.createLocal();
        MockCollectionPersistenceContext cpctx = new MockCollectionPersistenceContext();
        IdProvider idProvider = new ContextPathIdProvider();

        Map<String,Object> props = new HashMap<>();
        props.put("foo", "bar");
        props.put("bar", "foo");

        List<PropertyContainer> conditions = new ArrayList<>();
        Map<String,Object> values = new HashMap<>();
        values.put("foo", "bar");
        values.put("devices", Collections.singletonList(DeviceContext.createLocal("plugin1", "device1")));
        values.put("device", DeviceContext.createLocal("plugin2", "device2"));
        conditions.add(new PropertyContainer("condition1", "My Condition", PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cclass1"), values));

        TaskContext tctx = TaskContext.create(hctx, "taskId1");
        HobsonTask task = new HobsonTask(tctx, "My Task", "My Desc", props, conditions, new PropertyContainerSet("actionSetId1", "ActionSet1", Collections.singletonList(new PropertyContainer("action1", PropertyContainerClassContext.create(PluginContext.createLocal("plugin2"), "cclass2"), Collections.singletonMap("foo", (Object)"bar")))));

        CollectionPersister cp = new CollectionPersister(idProvider);

        cp.saveTask(cpctx, task);

        Map<String,Object> m = cpctx.getMap(idProvider.createTaskId(tctx));
        assertNotNull(m);
        assertEquals("My Task", m.get("name"));
        assertEquals("My Desc", m.get("description"));
        assertEquals("actionSetId1", m.get("actionSetId"));

        // check the task set
        Set<Object> s = cpctx.getSet(idProvider.createTasksId(hctx));
        assertNotNull(s);
        assertTrue(s.contains("taskId1"));

        // check map task properties
        m = cpctx.getMap(idProvider.createTaskPropertiesId(tctx));
        assertNotNull(m);
        assertEquals("bar", m.get("foo"));
        assertEquals("foo", m.get("bar"));

        // check task conditions set
        Set<Object> set = cpctx.getSet(idProvider.createTaskConditionsId(tctx));
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains("condition1"));

        // check condition map
        m = cpctx.getMap(idProvider.createTaskConditionId(tctx, "condition1"));
        assertNotNull(m);
        assertEquals("My Condition", m.get(PropertyConstants.NAME));
        assertEquals("cclass1", m.get(PropertyConstants.CONTAINER_CLASS_ID));
        assertEquals("plugin1", m.get(PropertyConstants.PLUGIN_ID));

        // check task condition properties
        m = cpctx.getMap(idProvider.createTaskConditionPropertiesId(tctx, "condition1"));
        assertNotNull(m);
        assertEquals(3, m.size());
        assertTrue(m.containsKey("device"));
        assertTrue(m.containsKey("devices"));
        assertTrue(m.containsKey("foo"));
        assertEquals("bar", m.get("foo"));

        // check the action set
        m = cpctx.getMap(idProvider.createActionSetId(hctx, "actionSetId1"));
        assertNotNull(m);
        assertEquals(2, m.size());
        assertEquals("actionSetId1", m.get("id"));
        assertEquals("ActionSet1", m.get("name"));

        // check the action set actions
        s = cpctx.getSet(idProvider.createActionSetActionsId(hctx, "actionSetId1"));
        assertNotNull(s);
        assertEquals(1, s.size());
        assertEquals("action1", s.iterator().next());

        // restore task
        task = cp.restoreTask(cpctx, task.getContext());
        assertEquals("My Task", task.getName());
        assertEquals("My Desc", task.getDescription());

        // check task properties
        assertNotNull(task.getProperties());
        assertEquals(2, task.getProperties().size());
        assertEquals("bar", task.getProperties().get("foo"));
        assertEquals("foo", task.getProperties().get("bar"));

        // check task conditions
        assertNotNull(task.getConditions());
        assertEquals(1, task.getConditions().size());
        assertEquals("plugin1", task.getConditions().get(0).getContainerClassContext().getPluginId());
        assertEquals("cclass1", task.getConditions().get(0).getContainerClassContext().getContainerClassId());
        assertEquals("condition1", task.getConditions().get(0).getId());
        assertEquals("My Condition", task.getConditions().get(0).getName());
        assertEquals("bar", task.getConditions().get(0).getPropertyValue("foo"));
        assertTrue(task.getConditions().get(0).getPropertyValue("devices") instanceof List);
        assertEquals(DeviceContext.createLocal("plugin1", "device1"), ((List) task.getConditions().get(0).getPropertyValue("devices")).get(0));
        assertTrue(task.getConditions().get(0).getPropertyValue("device") instanceof DeviceContext);
        assertEquals(DeviceContext.createLocal("plugin2", "device2"), task.getConditions().get(0).getPropertyValue("device"));

        // check task action set
        assertEquals("actionSetId1", task.getActionSet().getId());
        assertNotNull(task.getActionSet().getProperties());

        // delete the task
        cp.deleteTask(cpctx, task.getContext());

        // confirm that the task cannot be restored
        assertNull(cp.restoreTask(cpctx, task.getContext()));

        // confirm that all map entries have been cleaned up
        s = cpctx.getSet(idProvider.createTasksId(hctx));
        assertNotNull(s);
        assertFalse(s.contains("taskId1"));
        assertEquals(0, cpctx.getMap(idProvider.createTaskPropertiesId(tctx)).size());
        assertEquals(0, cpctx.getSet(idProvider.createTaskConditionsId(tctx)).size());
        assertEquals(0, cpctx.getMap(idProvider.createTaskConditionId(tctx, "condition1")).size());
        assertEquals(0, cpctx.getMap(idProvider.createTaskConditionPropertiesId(tctx, "condition1")).size());
    }

    @Test
    public void testUpdateTask() {
        HubContext hctx = HubContext.createLocal();
        MockCollectionPersistenceContext cpctx = new MockCollectionPersistenceContext();
        IdProvider idProvider = new ContextPathIdProvider();

        PropertyContainerSet actionSet = new PropertyContainerSet(
            Collections.singletonList(
                new PropertyContainer(
                    PropertyContainerClassContext.create(PluginContext.createLocal("plugin2"),
                        "cclass2"
                    ),
                    Collections.singletonMap("foo", (Object)"bar")
                )
            )
        );

        TaskContext tctx = TaskContext.create(hctx, "taskId1");
        HobsonTask task = new HobsonTask(
            tctx,
            "My Task",
            "My Desc",
            null,
            Collections.singletonList(
                new PropertyContainer(
                    PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cclass1"),
                    Collections.singletonMap("foo", (Object)"bar")
                )
            ),
            actionSet
        );

        CollectionPersister cp = new CollectionPersister(idProvider);
        cp.saveTask(cpctx, task);

        task = cp.restoreTask(cpctx, tctx);
        assertNotNull(task);
        assertEquals("My Task", task.getName());
        assertEquals("My Desc", task.getDescription());
        assertEquals(1, task.getConditions().size());
        assertEquals("cclass1", task.getConditions().get(0).getContainerClassContext().getContainerClassId());
        assertEquals(1, task.getActionSet().getProperties().size());
        assertEquals("cclass2", task.getActionSet().getProperties().iterator().next().getContainerClassContext().getContainerClassId());
        assertEquals("bar", task.getActionSet().getProperties().iterator().next().getPropertyValue("foo"));

        // update task data
        task.setName("My Task2");
        task.setDescription("My Desc2");
        task.setConditions(Collections.singletonList(
            new PropertyContainer(
                PropertyContainerClassContext.create(PluginContext.createLocal("plugin4"), "cclass6"),
                Collections.singletonMap("foo2", (Object)"bar2")
            )
        ));
        task.getActionSet().setProperties(Collections.singletonList(
            new PropertyContainer(
                PropertyContainerClassContext.create(PluginContext.createLocal("plugin3"),
                    "cclass3"
                ),
                Collections.singletonMap("bar", (Object)"foo")
            )
        ));

        // re-save task
        cp.saveTask(cpctx, task);

        task = cp.restoreTask(cpctx, tctx);
        assertEquals("My Task2", task.getName());
        assertEquals("My Desc2", task.getDescription());
        assertEquals(1, task.getConditions().size());
        assertEquals("cclass6", task.getConditions().get(0).getContainerClassContext().getContainerClassId());
        assertEquals(1, task.getActionSet().getProperties().size());
        assertEquals("cclass3", task.getActionSet().getProperties().iterator().next().getContainerClassContext().getContainerClassId());
        assertEquals("foo", task.getActionSet().getProperties().iterator().next().getPropertyValue("bar"));
    }

    @Test
    public void testSaveAndRestoreActionSetWithOneItem() {
        IdProvider idProvider = new ContextPathIdProvider();
        HubContext hctx = HubContext.createLocal();

        // test save
        PropertyContainerSet as = new PropertyContainerSet("set1", "Action Set 1", Collections.singletonList(new PropertyContainer("action1", "Action 1", PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cc1"), Collections.singletonMap("foo", (Object) "bar"))));

        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();

        CollectionPersister cp = new CollectionPersister(idProvider);
        cp.saveActionSet(HubContext.createLocal(), pctx, as);

        Map<String,Object> map = pctx.getMap(idProvider.createActionSetId(hctx, "set1"));
        assertEquals("set1", map.get("id"));
        assertEquals("Action Set 1", map.get("name"));

        Set<Object> set = pctx.getSet(idProvider.createActionSetActionsId(hctx, "set1"));
        assertTrue(set.contains("action1"));

        assertTrue(pctx.hasMap("hubs:local:actions:action1"));
        map = pctx.getMap("hubs:local:actions:action1");
        assertEquals("plugin1", map.get("pluginId"));
        assertEquals("cc1", map.get("containerClassId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("hubs:local:actions:action1:properties"));
        map = pctx.getMap("hubs:local:actions:action1:properties");
        assertEquals("Sbar", map.get("foo"));

        // test restore
        MockTaskManager taskManager = new MockTaskManager();
        PropertyContainerSet as2 = cp.restoreActionSet(HubContext.createLocal(), pctx, "set1");
        assertEquals("set1", as2.getId());
        assertEquals("Action Set 1", as2.getName());

        assertEquals(1, as2.getProperties().size());

        PropertyContainer ta = as2.getProperties().get(0);
        assertEquals("action1", ta.getId());
        assertEquals("cc1", ta.getContainerClassContext().getContainerClassId());
        assertNotNull(ta.getPropertyValues());
        assertEquals(1, ta.getPropertyValues().size());
        assertEquals("bar", ta.getPropertyValues().get("foo"));
    }

    @Test
    public void testSaveAndRestoreActionSetWithTwoItems() {
        IdProvider idProvider = new ContextPathIdProvider();
        HubContext hctx = HubContext.createLocal();

        // test save
        List<PropertyContainer> actions = new ArrayList<>();
        PropertyContainerSet as = new PropertyContainerSet("set1", "Action Set 1", null);
        actions.add(new PropertyContainer("action1", PropertyContainerClassContext.create("local", "plugin", null, "foo"), Collections.singletonMap("foo", (Object) "bar")));
        actions.add(new PropertyContainer("action2", PropertyContainerClassContext.create("local", "plugin", null, "foo"), Collections.singletonMap("bar", (Object) "foo")));
        as.setProperties(actions);

        CollectionPersistenceContext pctx = new MockCollectionPersistenceContext();

        CollectionPersister cp = new CollectionPersister(idProvider);
        cp.saveActionSet(HubContext.createLocal(), pctx, as);

        Map<String,Object> map = pctx.getMap(idProvider.createActionSetId(hctx, "set1"));
        assertEquals("set1", map.get("id"));
        assertEquals("Action Set 1", map.get("name"));

        Set<Object> set = pctx.getSet(idProvider.createActionSetActionsId(hctx, "set1"));
        assertTrue(set.contains("action1"));
        assertTrue(set.contains("action2"));

        assertTrue(pctx.hasMap("hubs:local:actions:action1"));
        map = pctx.getMap("hubs:local:actions:action1");
        assertEquals("plugin", map.get("pluginId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("hubs:local:actions:action1:properties"));
        map = pctx.getMap("hubs:local:actions:action1:properties");
        assertEquals("Sbar", map.get("foo"));

        assertTrue(pctx.hasMap("hubs:local:actions:action2:properties"));
        map = pctx.getMap("hubs:local:actions:action2:properties");
        assertEquals("Sfoo", map.get("bar"));

        // test restore
        PropertyContainerSet as2 = cp.restoreActionSet(HubContext.createLocal(), pctx, "set1");
        assertEquals("set1", as2.getId());
        assertEquals("Action Set 1", as2.getName());

        assertEquals(2, as2.getProperties().size());

        PropertyContainer ta = as2.getProperties().get(0);
        assertEquals("local", ta.getContainerClassContext().getHubId());
        assertEquals("plugin", ta.getContainerClassContext().getPluginId());
        assertEquals("action1", ta.getId());
        assertNotNull(ta.getPropertyValues());
        assertEquals(1, ta.getPropertyValues().size());
        assertEquals("bar", ta.getPropertyValues().get("foo"));

        ta = as2.getProperties().get(1);
        assertEquals("local", ta.getContainerClassContext().getHubId());
        assertEquals("plugin", ta.getContainerClassContext().getPluginId());
        assertEquals("action2", ta.getId());
        assertNotNull(ta.getPropertyValues());
        assertEquals(1, ta.getPropertyValues().size());
        assertEquals("foo", ta.getPropertyValues().get("bar"));
    }

    @Test
    public void testUpdateActionSet() {
        HubContext hctx = HubContext.createLocal();
        IdProvider idProvider = new ContextPathIdProvider();

        // test initial save
        List<PropertyContainer> actions = new ArrayList<>();
        PropertyContainerSet as = new PropertyContainerSet("set1", "Action Set 1", null);
        actions.add(new PropertyContainer("action1", PropertyContainerClassContext.create("local", "plugin", null, "foo"), Collections.singletonMap("foo", (Object)"bar")));
        actions.add(new PropertyContainer("action2", PropertyContainerClassContext.create("local", "plugin", null, "foo"), Collections.singletonMap("bar", (Object)"foo")));
        as.setProperties(actions);

        CollectionPersistenceContext pctx = new MockCollectionPersistenceContext();
        CollectionPersister cp = new CollectionPersister(idProvider);
        cp.saveActionSet(hctx, pctx, as);

        as = cp.restoreActionSet(hctx, pctx, "set1");
        assertEquals(2, as.getProperties().size());
        for (PropertyContainer pc : as.getProperties()) {
            assertTrue("action1".equals(pc.getId()) || "action2".equals(pc.getId()));
        }

        // test update
        actions = new ArrayList<>();
        as = new PropertyContainerSet("set1", "Action Set 1", null);
        actions.add(new PropertyContainer("action3", PropertyContainerClassContext.create("local", "plugin", null, "foo"), Collections.singletonMap("foo", (Object)"bar")));
        actions.add(new PropertyContainer("action4", PropertyContainerClassContext.create("local", "plugin", null, "foo"), Collections.singletonMap("bar", (Object)"foo")));
        as.setProperties(actions);

        cp.saveActionSet(hctx, pctx, as);

        as = cp.restoreActionSet(hctx, pctx, "set1");
        assertEquals(2, as.getProperties().size());
        for (PropertyContainer pc : as.getProperties()) {
            assertTrue("action3".equals(pc.getId()) || "action4".equals(pc.getId()));
        }
    }

    @Test
    public void testSaveConditionWithNoProperties() {
        IdProvider idProvider = new ContextPathIdProvider();
        HubContext hctx = HubContext.createLocal();
        CollectionPersistenceContext pctx = new MockCollectionPersistenceContext();
        CollectionPersister cp = new CollectionPersister(idProvider);

        TaskContext tctx = TaskContext.create(hctx, "task1");
        PropertyContainer pc = new PropertyContainer(PropertyContainerClassContext.create(hctx, "cc1"), null);
        cp.saveCondition(pctx, tctx, pc);
    }

    @Test
    public void testSaveRestoreDeleteDevice() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();
        DeviceContext dctx = DeviceContext.create(hctx, "plugin1", "device1");
        List<DeviceVariableDescriptor> descriptions = new ArrayList<>();
        descriptions.add(new DeviceVariableDescriptor(DeviceVariableContext.create(dctx, "foo"), VariableMask.READ_ONLY, VariableMediaType.IMAGE_PNG));
        PropertyContainerClassContext pcctx = PropertyContainerClassContext.create(dctx, "configuration");
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty.Builder("foo", "fooName", "fooDesc", TypedProperty.Type.STRING).build());
        PropertyContainerClass cclass = new PropertyContainerClass(pcctx, PropertyContainerClassType.DEVICE_CONFIG, props);

        // create and save device
        HobsonDeviceDescriptor device = new HobsonDeviceDescriptor.Builder(dctx).
            name("foo").
            type(DeviceType.LIGHTBULB).
            configurationClass(cclass).
            manufacturerName("Mfg1").
            manufacturerVersion("1.0").
            modelName("model").
            preferredVariableName(VariableConstants.ON).
            variableDescriptions(descriptions).
            build();
        cp.saveDevice(cpc, device);

        // confirm device restores properly
        HobsonDeviceDescriptor d = cp.restoreDevice(cpc, dctx);
        assertEquals("foo", d.getName());
        assertEquals(DeviceType.LIGHTBULB, d.getType());
        assertEquals("Mfg1", d.getManufacturerName());
        assertEquals("1.0", d.getManufacturerVersion());
        assertEquals("model", d.getModelName());
        assertEquals(VariableConstants.ON, d.getPreferredVariableName());
        assertNotNull(d.getVariables());
        assertEquals(1, d.getVariables().size());
        assertNotNull(d.getConfigurationClass());
        assertEquals("fooName", d.getConfigurationClass().getSupportedProperty("foo").getName());
        assertEquals("fooDesc", d.getConfigurationClass().getSupportedProperty("foo").getDescription());
        assertEquals(TypedProperty.Type.STRING, d.getConfigurationClass().getSupportedProperty("foo").getType());

        // confirm device is added to set of hub devices
        Set<Object> s = cpc.getSet(idProvider.createDevicesId(hctx));
        assertNotNull(s);
        assertEquals(1, s.size());
        assertTrue(s.contains(idProvider.createDeviceId(dctx)));

        // confirm device deletes properly
        cp.deleteDevice(cpc, dctx);
        assertNull(cp.restoreDevice(cpc, dctx));
        s = cpc.getSet(idProvider.createDevicesId(hctx));
        assertNotNull(s);
        assertEquals(0, s.size());
    }

    @Test
    public void testRestoreIncompleteDevice() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        DeviceContext dctx = DeviceContext.createLocal("plugin1", "device1");

        Map<String,Object> map = new HashMap<>();
        map.put("lastCheckIn", "L1449186686774");
        cpc.setMap("local:hubs:local:devices:plugin1:device1", map);

        HobsonDeviceDescriptor d = cp.restoreDevice(cpc, dctx);
        assertNull(d);
    }

    @Test
    public void testSaveRestoreDeleteDeviceVariable() {
//        IdProvider idProvider = new ContextPathIdProvider();
//        CollectionPersister cp = new CollectionPersister(idProvider);
//        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
//        HubContext hctx = HubContext.create("hub1");
//        DeviceContext dctx = DeviceContext.create(hctx, "plugin1", "device1");
//        DeviceVariableContext vctx = DeviceVariableContext.create(dctx, "foo");
//
//        ImmutableHobsonVariable mhv = new ImmutableHobsonVariable(vctx, HobsonVariable.Mask.READ_ONLY, "bar", 1000L, VariableMediaType.IMAGE_JPG);
//        cp.saveDeviceVariable(cpc, mhv);
//
//        Map<String,Object> map = cpc.getMap(idProvider.createVariableId(vctx));
//        assertEquals("plugin1", map.get(PropertyConstants.PLUGIN_ID));
//        assertEquals("device1", map.get(PropertyConstants.DEVICE_ID));
//        assertEquals("foo", map.get(PropertyConstants.NAME));
//        assertEquals("bar", map.get(PropertyConstants.VALUE));
//        assertEquals("READ_ONLY", map.get(PropertyConstants.MASK));
//        assertEquals(VariableMediaType.IMAGE_JPG.toString(), map.get(PropertyConstants.MEDIA_TYPE));
//        assertEquals(1000L, map.get(PropertyConstants.LAST_UPDATE));
//
//        // confirm variable is restorable
//        DeviceVariableValue hv = cp.restoreDeviceVariable(cpc, dctx, "foo");
//        assertNotNull(hv);
//        assertEquals("hub1", hv.getContext().getHubId());
//        assertEquals("plugin1", hv.getPluginId());
//        assertEquals("device1", hv.getDeviceId());
//        assertEquals("foo", hv.getName());
//        assertEquals("bar", hv.getValue());
//        assertEquals(HobsonVariable.Mask.READ_ONLY, hv.getMask());
//        assertEquals(VariableMediaType.IMAGE_JPG, hv.getMediaType());
//        assertEquals(1000L, (long)hv.getLastUpdate());
//
//        // confirm list of device variables is correct
//        Set<Object> set = cpc.getSet(idProvider.createDeviceVariablesId(dctx));
//        assertNotNull(set);
//        assertEquals(1, set.size());
//        assertTrue(set.contains("foo"));
//
//        // delete variable
//        cp.deleteDeviceVariable(cpc, vctx);
//        assertNull(cp.restoreDeviceVariable(cpc, dctx, vctx.getName()));
//        set = cpc.getSet(idProvider.createDeviceVariablesId(dctx));
//        assertNotNull(set);
//        assertEquals(0, set.size());
    }

    @Test
    public void testSaveRestoreDeleteDevicePassport() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.create("hub1");

        long now = System.currentTimeMillis();
        DevicePassport dp = new DevicePassport(hctx, "dp1", "foo", now);
        cp.saveDevicePassport(cpc, hctx, dp);

        // confirm passport is restorable
        dp = cp.restoreDevicePassport(cpc, hctx, "dp1");
        assertNotNull(dp);

        // confirm list of device passports is correct
        Set<Object> set = cpc.getSet(idProvider.createDevicePassportsId(hctx));
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains("dp1"));

        // delete passport
        cp.deleteDevicePassport(cpc, hctx, "dp1");
        assertNull(cp.restoreDevicePassport(cpc, hctx, "dp1"));
        set = cpc.getSet(idProvider.createDevicePassportsId(hctx));
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testSaveRestoreDeleteDataStream() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();

        DeviceVariableContext vctx1 = DeviceVariableContext.create(hctx, "plugin1", "device1", "foo");
        DeviceVariableContext vctx2 = DeviceVariableContext.create(hctx, "plugin2", "device2", "foo2");
        Collection<DataStreamField> fields = new ArrayList<>();
        fields.add(new DataStreamField("field1", "test", vctx1));
        fields.add(new DataStreamField("field2", "test2", vctx2));
        HashSet<String> tags = new HashSet<>();
        tags.add("tag1");
        tags.add("tag2");
        DataStream ds = new DataStream("id", "Test", fields, tags);
        cp.saveDataStream(cpc, ds);

        ds = cp.restoreDataStream(cpc, "id");
        assertEquals("id", ds.getId());
        assertEquals("Test", ds.getName());
        assertEquals(2, ds.getFields().size());

        for (DataStreamField dsf : ds.getFields()) {
            assertTrue(dsf.getId().equals("test") || dsf.getId() != null);
            assertTrue(dsf.getName().equals("test") || dsf.getName().equals("test2"));
            assertTrue(dsf.getVariable().equals(vctx1) || dsf.getVariable().equals(vctx2));
        }

        assertNotNull(ds.getTags());
        assertEquals(2, ds.getTags().size());
        assertTrue(ds.getTags().contains("tag1"));
        assertTrue(ds.getTags().contains("tag2"));

        cp.deleteDataStream(cpc, "id");
        assertEquals(0, cpc.getMap(idProvider.createDataStreamId("id")).size());
        assertEquals(0, cpc.getSet(idProvider.createDataStreamFieldsId("id")).size());
        assertEquals(0, cpc.getMap(idProvider.createDataStreamFieldId("id", "field1")).size());
        assertEquals(0, cpc.getSet(idProvider.createDataStreamTagsId("id")).size());
    }

    @Test
    public void testSaveDataStreamWithNullTags() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();
        Collection<DataStreamField> fields = new ArrayList<>();
        fields.add(new DataStreamField("field1", "test", DeviceVariableContext.create(hctx, "plugin1", "device1", "foo")));
        fields.add(new DataStreamField("field2", "test2", DeviceVariableContext.create(hctx, "plugin2", "device2", "foo2")));

        DataStream ds = new DataStream("id", "Test", fields, null);
        cp.saveDataStream(cpc, ds);

        ds = cp.restoreDataStream(cpc, "id");

        assertEquals(0, ds.getTags().size());
    }

    @Test
    public void testSaveRestoreDeletePresenceEntity() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();
        PresenceEntityContext pctx = PresenceEntityContext.create(hctx, "entity1");

        cp.savePresenceEntity(cpc, new PresenceEntity(pctx, "John"));

        PresenceEntity pe = cp.restorePresenceEntity(cpc, pctx);
        assertEquals("entity1", pe.getContext().getEntityId());
        assertEquals("John", pe.getName());

        cp.deletePresenceEntity(cpc, pctx);
        assertNull(cp.restorePresenceEntity(cpc, pctx));
    }

    @Test
    public void testSaveRestoreDeletePresenceLocation() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();
        PresenceLocationContext pctx = PresenceLocationContext.create(hctx, "location1");

        cp.savePresenceLocation(cpc, new PresenceLocation(pctx, "Home"));

        PresenceLocation pe = cp.restorePresenceLocation(cpc, pctx);
        assertEquals("location1", pe.getContext().getLocationId());
        assertEquals("Home", pe.getName());

        cp.deletePresenceLocation(cpc, pctx);
        assertNull(cp.restorePresenceLocation(cpc, pctx));
    }
}
