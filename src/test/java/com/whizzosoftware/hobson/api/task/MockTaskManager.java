package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.action.Action;
import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MockTaskManager implements TaskManager {
    @Override
    public void createTask(HubContext ctx, String name, String description, List<PropertyContainer> conditions, PropertyContainerSet actionSet) {

    }

    @Override
    public void deleteTask(TaskContext ctx) {

    }

    @Override
    public void executeTask(TaskContext ctx) {

    }

    @Override
    public void executeActionSet(HubContext ctx, String actionSetId) {

    }

    @Override
    public void fireTaskTrigger(TaskContext ctx) {

    }

    @Override
    public ActionClass getActionClass(PropertyContainerClassContext ctx) {
        return new ActionClass(ctx, "", "", false, 2000) {
            @Override
            public Action newInstance(ActionExecutionContext ctx, PropertyContainer properties, EventLoopExecutor executor) {
                return null;
            }
        };
    }

    @Override
    public Collection<ActionClass> getActionClasses(PluginContext ctx) {
        return null;
    }

    @Override
    public PropertyContainerSet getActionSet(HubContext ctx, String actionSetId) {
        return null;
    }

    @Override
    public Collection<ActionClass> getAllActionClasses(HubContext ctx, boolean applyConstraints) {
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
    public void publishActionClass(ActionClass actionClass) {
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
