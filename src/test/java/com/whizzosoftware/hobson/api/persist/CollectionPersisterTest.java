package com.whizzosoftware.hobson.api.persist;

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
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class CollectionPersisterTest {
    @Test
    public void testSaveAndRestoreActionSetWithOneItem() {
        // test save
        PropertyContainerSet as = new PropertyContainerSet("set1", "Action Set 1", Collections.singletonList(new PropertyContainer("action1", "Action 1", PropertyContainerClassContext.create(PluginContext.createLocal("plugin1"), "cc1"), Collections.singletonMap("foo", (Object) "bar"))));

        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();

        CollectionPersister cp = new CollectionPersister();
        cp.saveActionSet(HubContext.createLocal(), pctx, as);

        assertTrue(pctx.hasMap("local:local:actionSets:set1"));
        Map<String,Object> map = pctx.getMap("local:local:actionSets:set1");
        assertEquals("set1", map.get("id"));
        assertEquals("Action Set 1", map.get("name"));
        assertEquals("action1", map.get("actions"));

        assertTrue(pctx.hasMap("local:local:actions:action1"));
        map = pctx.getMap("local:local:actions:action1");
        assertEquals("plugin1", map.get("pluginId"));
        assertEquals("cc1", map.get("containerClassId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("local:local:actions:action1:properties"));
        map = pctx.getMap("local:local:actions:action1:properties");
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
        actions.add(new PropertyContainer("action1", PropertyContainerClassContext.create("local", "local", "plugin", "foo"), Collections.singletonMap("foo", (Object) "bar")));
        actions.add(new PropertyContainer("action2", PropertyContainerClassContext.create("local", "local", "plugin", "foo"), Collections.singletonMap("bar", (Object) "foo")));
        as.setProperties(actions);

        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();

        CollectionPersister cp = new CollectionPersister();
        cp.saveActionSet(HubContext.createLocal(), pctx, as);

        assertTrue(pctx.hasMap("local:local:actionSets:set1"));
        Map<String,Object> map = pctx.getMap("local:local:actionSets:set1");
        assertEquals("set1", map.get("id"));
        assertEquals("Action Set 1", map.get("name"));
        assertEquals("action1,action2", map.get("actions"));

        assertTrue(pctx.hasMap("local:local:actions:action1"));
        map = pctx.getMap("local:local:actions:action1");
        assertEquals("plugin", map.get("pluginId"));
        assertEquals("action1", map.get("id"));

        assertTrue(pctx.hasMap("local:local:actions:action1:properties"));
        map = pctx.getMap("local:local:actions:action1:properties");
        assertEquals("Sbar", map.get("foo"));

        assertTrue(pctx.hasMap("local:local:actions:action2:properties"));
        map = pctx.getMap("local:local:actions:action2:properties");
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
