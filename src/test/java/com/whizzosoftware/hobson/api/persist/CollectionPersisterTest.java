package com.whizzosoftware.hobson.api.persist;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.device.HobsonDeviceStub;
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
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.ImmutableHobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.api.variable.VariableMediaType;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class CollectionPersisterTest {
    @Test
    public void testSaveAndRestoreTask() {
        MockCollectionPersistenceContext cpctx = new MockCollectionPersistenceContext();

        Map<String,Object> props = new HashMap<>();
        props.put("foo", "bar");
        props.put("bar", "foo");

        List<PropertyContainer> conditions = new ArrayList<>();
        Map<String,Object> values = new HashMap<>();
        values.put("foo", "bar");
        values.put("devices", Collections.singletonList(DeviceContext.createLocal("plugin1", "device1")));
        values.put("device", DeviceContext.createLocal("plugin2", "device2"));
        conditions.add(new PropertyContainer("condition1", "My Condition", PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cclass1"), values));

        HobsonTask task = new HobsonTask(TaskContext.createLocal("taskId1"), "My Task", "My Desc", props, conditions, new PropertyContainerSet("actionSetId1"));

        CollectionPersister cp = new CollectionPersister();
        cp.saveTask(cpctx, task);

        Map<String,Object> m = cpctx.getMap("local:hubs:local:tasks:taskMeta:taskId1");
        assertNotNull(m);
        assertEquals("My Task", m.get("name"));
        assertEquals("My Desc", m.get("description"));
        assertEquals("actionSetId1", m.get("actionSetId"));

        // check map task properties
        m = cpctx.getMap("local:hubs:local:tasks:properties:taskId1:foo");
        assertNotNull(m);
        assertEquals("foo", m.get("name"));
        assertEquals("bar", m.get("value"));
        m = cpctx.getMap("local:hubs:local:tasks:properties:taskId1:bar");
        assertNotNull(m);
        assertEquals("bar", m.get("name"));
        assertEquals("foo", m.get("value"));

        // check map conditions
        m = cpctx.getMap("local:hubs:local:tasks:conditions:taskId1:conditionMeta:condition1");
        assertNotNull(m);
        assertEquals("My Condition", m.get("name"));
        assertEquals("local:local:plugin1:null:cclass1", m.get("context"));
        m = cpctx.getMap("local:hubs:local:tasks:conditions:taskId1:conditionValues:condition1:foo");
        assertNotNull(m);
        assertEquals("foo", m.get("name"));
        assertEquals("Sbar", m.get("value"));

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
    }

    @Test
    public void testSaveAndRestoreActionSetWithOneItem() {
        // test save
        PropertyContainerSet as = new PropertyContainerSet("set1", "Action Set 1", Collections.singletonList(new PropertyContainer("action1", "Action 1", PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cc1"), Collections.singletonMap("foo", (Object) "bar"))));

        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();

        CollectionPersister cp = new CollectionPersister();
        cp.saveActionSet(HubContext.createLocal(), pctx, as);

        assertTrue(pctx.hasMap("local:hubs:local:actionSets:set1"));
        Map<String,Object> map = pctx.getMap("local:hubs:local:actionSets:set1");
        assertEquals("set1", map.get("id"));
        assertEquals("Action Set 1", map.get("name"));
        assertEquals("action1", map.get("actions"));

        assertTrue(pctx.hasMap("local:hubs:local:actions:action1"));
        map = pctx.getMap("local:hubs:local:actions:action1");
        assertEquals("plugin1", map.get("pluginId"));
        assertEquals("cc1", map.get("containerClassId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("local:hubs:local:actions:action1:properties"));
        map = pctx.getMap("local:hubs:local:actions:action1:properties");
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
        // test save
        List<PropertyContainer> actions = new ArrayList<>();
        PropertyContainerSet as = new PropertyContainerSet("set1", "Action Set 1", null);
        actions.add(new PropertyContainer("action1", PropertyContainerClassContext.create("local", "local", "plugin", null, "foo"), Collections.singletonMap("foo", (Object) "bar")));
        actions.add(new PropertyContainer("action2", PropertyContainerClassContext.create("local", "local", "plugin", null, "foo"), Collections.singletonMap("bar", (Object) "foo")));
        as.setProperties(actions);

        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();

        CollectionPersister cp = new CollectionPersister();
        cp.saveActionSet(HubContext.createLocal(), pctx, as);

        assertTrue(pctx.hasMap("local:hubs:local:actionSets:set1"));
        Map<String,Object> map = pctx.getMap("local:hubs:local:actionSets:set1");
        assertEquals("set1", map.get("id"));
        assertEquals("Action Set 1", map.get("name"));
        assertEquals("action1,action2", map.get("actions"));

        assertTrue(pctx.hasMap("local:hubs:local:actions:action1"));
        map = pctx.getMap("local:hubs:local:actions:action1");
        assertEquals("plugin", map.get("pluginId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("local:hubs:local:actions:action1:properties"));
        map = pctx.getMap("local:hubs:local:actions:action1:properties");
        assertEquals("Sbar", map.get("foo"));

        assertTrue(pctx.hasMap("local:hubs:local:actions:action2:properties"));
        map = pctx.getMap("local:hubs:local:actions:action2:properties");
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
        CollectionPersister cp = new CollectionPersister();
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        DeviceContext dctx = DeviceContext.createLocal("plugin1", "device1");

        HobsonDeviceStub device = new HobsonDeviceStub.Builder(dctx).
            name("foo").
            type(DeviceType.LIGHTBULB).
            manufacturerName("Mfg1").
            manufacturerVersion("1.0").
            modelName("model").
            preferredVariableName(VariableConstants.ON).
            build();
        cp.saveDevice(cpc, device);

        Map<String,Object> map = cpc.getMap("local:hubs:local:devices:plugin1:device1");
        assertNotNull(map);
        assertEquals("foo", map.get("name"));
        assertEquals("LIGHTBULB", map.get("type"));
        assertEquals("Mfg1", map.get("manufacturerName"));
        assertEquals("1.0", map.get("manufacturerVersion"));
        assertEquals("model", map.get("modelName"));
        assertEquals(VariableConstants.ON, map.get("preferredVariableName"));

        HobsonDevice d = cp.restoreDevice(cpc, dctx);
        assertEquals("foo", d.getName());
        assertEquals(DeviceType.LIGHTBULB, d.getType());
        assertEquals("Mfg1", d.getManufacturerName());
        assertEquals("1.0", d.getManufacturerVersion());
        assertEquals("model", d.getModelName());
        assertEquals(VariableConstants.ON, d.getPreferredVariableName());
    }

    @Test
    public void testRestoreIncompleteDevice() {
        CollectionPersister cp = new CollectionPersister();
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
        CollectionPersister cp = new CollectionPersister();
        CollectionPersistenceContext cpc = new MockCollectionPersistenceContext();
        DeviceContext dctx = DeviceContext.createLocal("plugin1", "device1");

        ImmutableHobsonVariable mhv = new ImmutableHobsonVariable("plugin1", "device1", "foo", HobsonVariable.Mask.READ_ONLY, "bar", VariableMediaType.IMAGE_JPG, 1000L);
        cp.saveDeviceVariable(cpc, dctx, mhv);

        Map<String,Object> map = cpc.getMap("local:hubs:local:variables:device:plugin1:device1:foo");
        assertEquals("plugin1", map.get(PropertyConstants.PLUGIN_ID));
        assertEquals("device1", map.get(PropertyConstants.DEVICE_ID));
        assertEquals("foo", map.get(PropertyConstants.NAME));
        assertEquals("bar", map.get(PropertyConstants.VALUE));
        assertEquals("READ_ONLY", map.get(PropertyConstants.MASK));
        assertEquals(VariableMediaType.IMAGE_JPG.toString(), map.get(PropertyConstants.MEDIA_TYPE));
        assertEquals(1000L, map.get(PropertyConstants.LAST_UPDATE));

        HobsonVariable hv = cp.restoreDeviceVariable(cpc, dctx, "foo");
        assertNotNull(hv);
        assertEquals("plugin1", hv.getPluginId());
        assertEquals("device1", hv.getDeviceId());
        assertEquals("foo", hv.getName());
        assertEquals("bar", hv.getValue());
        assertEquals(HobsonVariable.Mask.READ_ONLY, hv.getMask());
        assertEquals(VariableMediaType.IMAGE_JPG, hv.getMediaType());
        assertEquals(1000L, (long)hv.getLastUpdate());
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
