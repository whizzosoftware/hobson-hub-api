package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.hub.HubContext;
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
    public void fireTaskTrigger(TaskContext ctx) {

    }

    @Override
    public Collection<TaskConditionClass> getConditionClasses(HubContext ctx, ConditionClassType type, boolean applyConstraints) {
        return null;
    }

    @Override
    public Collection<HobsonTask> getTasks(HubContext ctx) {
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
    public void publishConditionClass(TaskConditionClass conditionClass) {

    }

    @Override
    public void updateTask(PluginContext pctx, TaskContext ctx, String name, String description, boolean enabled, List<PropertyContainer> conditions, PropertyContainerSet actionSet) {

    }

    @Override
    public void updateTaskProperties(PluginContext pctx, TaskContext ctx, Map<String, Object> properties) {

    }
}
