package com.whizzosoftware.hobson.api.persist;

public class CollectionPersisterTest {
//    @Test
//    public void testSaveAndRestoreActionSetWithOneItem() {
//        // test save
//
//        TaskActionSet as = new TaskActionSet(HubContext.createLocal(), "set1", "Action Set 1");
//        as.setActions(Collections.singletonList(new TaskAction(TaskActionClassContext.createLocal("plugin", "foo"), "action1", Collections.singletonMap("foo", (Object) "bar"), null)));
//
//        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();
//
//        CollectionPersister cp = new CollectionPersister();
//        cp.saveActionSet(pctx, as);
//
//        assertTrue(pctx.hasMap("local:local:actionSets:set1"));
//        Map<String,String> map = pctx.getMap("local:local:actionSets:set1");
//        assertEquals("set1", map.get("id"));
//        assertEquals("Action Set 1", map.get("name"));
//        assertEquals("action1", map.get("actions"));
//
//        assertTrue(pctx.hasMap("local:local:actions:action1"));
//        map = pctx.getMap("local:local:actions:action1");
//        assertEquals("plugin", map.get("pluginId"));
//        assertEquals("action1", map.get("id"));
//
//        assertTrue(pctx.hasMap("local:local:actions:action1:properties"));
//        map = pctx.getMap("local:local:actions:action1:properties");
//        assertEquals("Sbar", map.get("foo"));
//
//        // test restore
//        MockTaskManager taskManager = new MockTaskManager();
//        TaskActionSet as2 = cp.restoreActionSet(pctx, taskManager, HubContext.createLocal(), "set1");
//        assertEquals("local", as2.getContext().getUserId());
//        assertEquals("local", as2.getContext().getHubId());
//        assertEquals("set1", as2.getId());
//        assertEquals("Action Set 1", as2.getName());
//
//        assertEquals(1, as2.getActions().size());
//
//        TaskAction ta = as2.getActions().get(0);
//        assertEquals("local", as2.getContext().getUserId());
//        assertEquals("local", as2.getContext().getHubId());
//        assertEquals("action1", ta.getId());
//        assertNotNull(ta.getPropertyValues());
//        assertEquals(1, ta.getPropertyValues().size());
//        assertEquals("bar", ta.getPropertyValues().get("foo"));
//    }
//
//    @Test
//    public void testSaveAndRestoreActionSetWithTwoItems() {
//        // test save
//
//        List<TaskAction> actions = new ArrayList<>();
//        TaskActionSet as = new TaskActionSet(HubContext.createLocal(), "set1", "Action Set 1");
//        actions.add(new TaskAction(TaskActionClassContext.createLocal("plugin", "foo"), "action1", Collections.singletonMap("foo", (Object) "bar"), null));
//        actions.add(new TaskAction(TaskActionClassContext.createLocal("plugin", "foo"), "action2", Collections.singletonMap("bar", (Object) "foo"), null));
//        as.setActions(actions);
//
//        MockCollectionPersistenceContext pctx = new MockCollectionPersistenceContext();
//
//        CollectionPersister cp = new CollectionPersister();
//        cp.saveActionSet(pctx, as);
//
//        assertTrue(pctx.hasMap("local:local:actionSets:set1"));
//        Map<String,String> map = pctx.getMap("local:local:actionSets:set1");
//        assertEquals("set1", map.get("id"));
//        assertEquals("Action Set 1", map.get("name"));
//        assertEquals("action1,action2", map.get("actions"));
//
//        assertTrue(pctx.hasMap("local:local:actions:action1"));
//        map = pctx.getMap("local:local:actions:action1");
//        assertEquals("plugin", map.get("pluginId"));
//        assertEquals("action1", map.get("id"));
//
//        assertTrue(pctx.hasMap("local:local:actions:action1:properties"));
//        map = pctx.getMap("local:local:actions:action1:properties");
//        assertEquals("Sbar", map.get("foo"));
//
//        assertTrue(pctx.hasMap("local:local:actions:action2:properties"));
//        map = pctx.getMap("local:local:actions:action2:properties");
//        assertEquals("Sfoo", map.get("bar"));
//
//        // test restore
//        MockTaskManager taskManager = new MockTaskManager();
//        TaskActionSet as2 = cp.restoreActionSet(pctx, taskManager, HubContext.createLocal(), "set1");
//        assertEquals("local", as2.getContext().getUserId());
//        assertEquals("local", as2.getContext().getHubId());
//        assertEquals("set1", as2.getId());
//        assertEquals("Action Set 1", as2.getName());
//
//        assertEquals(2, as2.getActions().size());
//
//        TaskAction ta = as2.getActions().get(0);
//        assertEquals("local", as2.getContext().getUserId());
//        assertEquals("local", as2.getContext().getHubId());
//        assertEquals("action1", ta.getId());
//        assertNotNull(ta.getPropertyValues());
//        assertEquals(1, ta.getPropertyValues().size());
//        assertEquals("bar", ta.getPropertyValues().get("foo"));
//
//        ta = as2.getActions().get(1);
//        assertEquals("local", as2.getContext().getUserId());
//        assertEquals("local", as2.getContext().getHubId());
//        assertEquals("action2", ta.getId());
//        assertNotNull(ta.getPropertyValues());
//        assertEquals(1, ta.getPropertyValues().size());
//        assertEquals("foo", ta.getPropertyValues().get("bar"));
//    }
//
//    public class MockTaskManager implements TaskManager {
//        @Override
//        public void createTask(PluginContext pluginContext, String name, TaskConditionSet conditionSet, TaskActionSet actionSet) {
//
//        }
//
//        @Override
//        public void deleteTask(TaskContext ctx) {
//
//        }
//
//        @Override
//        public void executeActionSet(HubContext ctx, String actionSetId) {
//
//        }
//
//        @Override
//        public void executeTask(TaskContext ctx) {
//
//        }
//
//        @Override
//        public TaskActionClass getActionClass(final TaskActionClassContext ctx) {
//            return new TaskActionClass(ctx, "foo", null, null);
//        }
//
//        @Override
//        public TaskActionSet getActionSet(HubContext ctx, String actionSetId) {
//            return null;
//        }
//
//        @Override
//        public Collection<TaskActionClass> getAllActionClasses(HubContext ctx) {
//            return null;
//        }
//
//        @Override
//        public Collection<TaskActionSet> getAllActionSets(HubContext ctx) {
//            return null;
//        }
//
//        @Override
//        public Collection<TaskConditionClass> getAllConditionClasses(HubContext ctx) {
//            return null;
//        }
//
//        @Override
//        public Collection<HobsonTask> getAllTasks(HubContext ctx) {
//            return null;
//        }
//
//        @Override
//        public TaskConditionClass getConditionClass(TaskConditionClassContext ctx) {
//            return null;
//        }
//
//        @Override
//        public HobsonTask getTask(TaskContext ctx) {
//            return null;
//        }
//
//        @Override
//        public void publishActionClass(TaskActionClassContext context, String name, List<TypedProperty> properties, TaskActionExecutor executor) {
//
//        }
//
//        @Override
//        public TaskActionSet publishActionSet(HubContext ctx, String name, List<TaskAction> actions) {
//            return null;
//        }
//
//        @Override
//        public void publishConditionClass(TaskConditionClassContext ctx, String name, List<TypedProperty> properties) {
//
//        }
//
//        @Override
//        public void publishTask(HobsonTask task) {
//
//        }
//
//        @Override
//        public void unpublishAllActionClasses(PluginContext ctx) {
//
//        }
//
//        @Override
//        public void unpublishAllActionSets(PluginContext ctx) {
//
//        }
//
//        @Override
//        public void unpublishAllConditionClasses(PluginContext ctx) {
//
//        }
//
//        @Override
//        public void unpublishAllTasks(PluginContext ctx) {
//
//        }
//
//        @Override
//        public void updateTask(TaskContext ctx, String name, TaskConditionSet conditionSet, TaskActionSet actionSet) {
//
//        }
//    }
}
