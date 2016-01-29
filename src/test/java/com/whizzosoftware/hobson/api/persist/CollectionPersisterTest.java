package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.device.*;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.task.HobsonTask;
import com.whizzosoftware.hobson.api.task.TaskContext;
import com.whizzosoftware.hobson.api.task.TaskManager;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
import com.whizzosoftware.hobson.api.task.action.TaskActionExecutor;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.telemetry.DataStream;
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

        TaskContext tctx = TaskContext.createLocal("taskId1");
        HobsonTask task = new HobsonTask(tctx, "My Task", "My Desc", props, conditions, new PropertyContainerSet("actionSetId1"));

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

        // check map conditions
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

        // delete the task
        cp.deleteTask(cpctx, task.getContext());

        // confirm that the task cannot be resotred
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

        assertTrue(pctx.hasMap("users:local:hubs:local:actions:action1"));
        map = pctx.getMap("users:local:hubs:local:actions:action1");
        assertEquals("plugin1", map.get("pluginId"));
        assertEquals("cc1", map.get("containerClassId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("users:local:hubs:local:actions:action1:properties"));
        map = pctx.getMap("users:local:hubs:local:actions:action1:properties");
        assertEquals("Sbar", map.get("foo"));

        // test restore
        MockTaskManager taskManager = new MockTaskManager();
        PropertyContainerSet as2 = cp.restoreActionSet(HubContext.createLocal(), pctx, taskManager, "set1");
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
        actions.add(new PropertyContainer("action1", PropertyContainerClassContext.create("local", "local", "plugin", null, "foo"), Collections.singletonMap("foo", (Object) "bar")));
        actions.add(new PropertyContainer("action2", PropertyContainerClassContext.create("local", "local", "plugin", null, "foo"), Collections.singletonMap("bar", (Object) "foo")));
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

        assertTrue(pctx.hasMap("users:local:hubs:local:actions:action1"));
        map = pctx.getMap("users:local:hubs:local:actions:action1");
        assertEquals("plugin", map.get("pluginId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("users:local:hubs:local:actions:action1:properties"));
        map = pctx.getMap("users:local:hubs:local:actions:action1:properties");
        assertEquals("Sbar", map.get("foo"));

        assertTrue(pctx.hasMap("users:local:hubs:local:actions:action2:properties"));
        map = pctx.getMap("users:local:hubs:local:actions:action2:properties");
        assertEquals("Sfoo", map.get("bar"));

        // test restore
        MockTaskManager taskManager = new MockTaskManager();
        PropertyContainerSet as2 = cp.restoreActionSet(HubContext.createLocal(), pctx, taskManager, "set1");
        assertEquals("set1", as2.getId());
        assertEquals("Action Set 1", as2.getName());

        assertEquals(2, as2.getProperties().size());

        PropertyContainer ta = as2.getProperties().get(0);
        assertEquals("local", ta.getContainerClassContext().getUserId());
        assertEquals("local", ta.getContainerClassContext().getHubId());
        assertEquals("plugin", ta.getContainerClassContext().getPluginId());
        assertEquals("action1", ta.getId());
        assertNotNull(ta.getPropertyValues());
        assertEquals(1, ta.getPropertyValues().size());
        assertEquals("bar", ta.getPropertyValues().get("foo"));

        ta = as2.getProperties().get(1);
        assertEquals("local", ta.getContainerClassContext().getUserId());
        assertEquals("local", ta.getContainerClassContext().getHubId());
        assertEquals("plugin", ta.getContainerClassContext().getPluginId());
        assertEquals("action2", ta.getId());
        assertNotNull(ta.getPropertyValues());
        assertEquals(1, ta.getPropertyValues().size());
        assertEquals("foo", ta.getPropertyValues().get("bar"));
    }

    @Test
    public void testSaveAndRestoreDevice() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();
        DeviceContext dctx = DeviceContext.create(hctx, "plugin1", "device1");

        // create and save device
        HobsonDeviceStub device = new HobsonDeviceStub.Builder(dctx).
            name("foo").
            type(DeviceType.LIGHTBULB).
            manufacturerName("Mfg1").
            manufacturerVersion("1.0").
            modelName("model").
            preferredVariableName(VariableConstants.ON).
            build();
        cp.saveDevice(cpc, device);

        // confirm device restores properly
        HobsonDevice d = cp.restoreDevice(cpc, dctx);
        assertEquals("foo", d.getName());
        assertEquals(DeviceType.LIGHTBULB, d.getType());
        assertEquals("Mfg1", d.getManufacturerName());
        assertEquals("1.0", d.getManufacturerVersion());
        assertEquals("model", d.getModelName());
        assertEquals(VariableConstants.ON, d.getPreferredVariableName());

        // confirm device is added to set of hub devices
        Set<Object> s = cpc.getSet(idProvider.createDevicesId(hctx));
        assertNotNull(s);
        assertEquals(1, s.size());
        assertTrue(s.contains(idProvider.createDeviceId(dctx)));
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

        HobsonDevice d = cp.restoreDevice(cpc, dctx);
        assertNull(d);
    }

    @Test
    public void testSaveAndRestoreDeviceVariable() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.create("user1", "hub1");
        DeviceContext dctx = DeviceContext.create(hctx, "plugin1", "device1");
        VariableContext vctx = VariableContext.create(dctx, "foo");

        ImmutableHobsonVariable mhv = new ImmutableHobsonVariable(vctx, HobsonVariable.Mask.READ_ONLY, "bar", 1000L, VariableMediaType.IMAGE_JPG);
        cp.saveDeviceVariable(cpc, mhv);

        Map<String,Object> map = cpc.getMap(idProvider.createVariableId(vctx));
        assertEquals("plugin1", map.get(PropertyConstants.PLUGIN_ID));
        assertEquals("device1", map.get(PropertyConstants.DEVICE_ID));
        assertEquals("foo", map.get(PropertyConstants.NAME));
        assertEquals("bar", map.get(PropertyConstants.VALUE));
        assertEquals("READ_ONLY", map.get(PropertyConstants.MASK));
        assertEquals(VariableMediaType.IMAGE_JPG.toString(), map.get(PropertyConstants.MEDIA_TYPE));
        assertEquals(1000L, map.get(PropertyConstants.LAST_UPDATE));

        // confirm variable is restorable
        HobsonVariable hv = cp.restoreDeviceVariable(cpc, dctx, "foo");
        assertNotNull(hv);
        assertEquals("user1", hv.getContext().getUserId());
        assertEquals("hub1", hv.getContext().getHubId());
        assertEquals("plugin1", hv.getPluginId());
        assertEquals("device1", hv.getDeviceId());
        assertEquals("foo", hv.getName());
        assertEquals("bar", hv.getValue());
        assertEquals(HobsonVariable.Mask.READ_ONLY, hv.getMask());
        assertEquals(VariableMediaType.IMAGE_JPG, hv.getMediaType());
        assertEquals(1000L, (long)hv.getLastUpdate());

        // confirm list of device variables is correct
        Set<Object> set = cpc.getSet(idProvider.createDeviceVariablesId(dctx));
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains("foo"));
    }

    @Test
    public void testSaveAndRestoreDevicePassport() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.create("user1", "hub1");

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
    }

    @Test
    public void testSaveAndRestoreDataStream() {
        IdProvider idProvider = new ContextPathIdProvider();
        CollectionPersister cp = new CollectionPersister(idProvider);
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        HubContext hctx = HubContext.createLocal();

        VariableContext vctx = VariableContext.create(hctx, "plugin1", "device1", "foo");
        Collection<VariableContext> data = Collections.singletonList(vctx);
        DataStream ds = new DataStream("user1", "id", "Test", data);
        cp.saveDataStream(cpc, ds);

        ds = cp.restoreDataStream(cpc, "user1", "id");
        assertEquals("id", ds.getId());
        assertEquals("Test", ds.getName());
        assertEquals(1, ds.getVariables().size());
        assertEquals(vctx, ds.getVariables().iterator().next());
    }

    public class MockTaskManager implements TaskManager {
        @Override
        public void createTask(HubContext ctx, String name, String description, List<PropertyContainer> conditions, PropertyContainerSet actionSet) {

        }

        @Override
        public void deleteTask(TaskContext ctx) {

        }

        @Override
        public void executeActionSet(HubContext ctx, String actionSetId) {

        }

        @Override
        public void fireTaskTrigger(TaskContext ctx) {

        }

        @Override
        public TaskActionClass getActionClass(PropertyContainerClassContext ctx) {
            return new TaskActionClass(ctx, "", "") {
                @Override
                public List<TypedProperty> createProperties() {
                    return null;
                }

                @Override
                public TaskActionExecutor getExecutor() {
                    return null;
                }
            };
        }

        @Override
        public PropertyContainerSet getActionSet(HubContext ctx, String actionSetId) {
            return null;
        }

        @Override
        public Collection<TaskActionClass> getAllActionClasses(HubContext ctx, boolean applyConstraints) {
            return null;
        }

        @Override
        public Collection<PropertyContainerSet> getAllActionSets(HubContext ctx) {
            return null;
        }

        @Override
        public Collection<TaskConditionClass> getAllConditionClasses(HubContext ctx, ConditionClassType type, boolean applyConstraints) {
            return null;
        }

        @Override
        public Collection<HobsonTask> getAllTasks(HubContext ctx) {
            return null;
        }

        @Override
        public TaskConditionClass getConditionClass(PropertyContainerClassContext ctx) {
            return null;
        }

        @Override
        public HobsonTask getTask(TaskContext ctx) {
            return null;
        }

        @Override
        public void publishActionClass(TaskActionClass actionClass) {
        }

        @Override
        public PropertyContainerSet publishActionSet(HubContext ctx, String name, List<PropertyContainer> actions) {
            return null;
        }

        @Override
        public void publishConditionClass(TaskConditionClass conditionClass) {

        }

        @Override
        public void unpublishAllActionClasses(PluginContext ctx) {

        }

        @Override
        public void unpublishAllActionSets(PluginContext ctx) {

        }

        @Override
        public void unpublishAllConditionClasses(PluginContext ctx) {

        }

        @Override
        public void updateTask(TaskContext ctx, String name, String description, List<PropertyContainer> conditions, PropertyContainerSet actionSet) {

        }

        @Override
        public void updateTaskProperties(TaskContext ctx, Map<String, Object> properties) {

        }
    }

}
