package com.whizzosoftware.hobson.api.event.task;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Collection;
import java.util.Map;

public class TaskRegistrationEvent extends TaskEvent {
    public static final String ID = "taskRegistration";

    private static final String PROP_PLUGIN_ID = "pluginId";
    private static final String PROP_TASKS = "tasks";

    public TaskRegistrationEvent(long timestamp, String pluginId, Collection<TaskContext> tasks) {
        super(timestamp, ID);
        setProperty(PROP_PLUGIN_ID, pluginId);
        setProperty(PROP_TASKS, tasks);
    }

    public TaskRegistrationEvent(Map<String,Object> properties) {
        super(properties);
    }

    public String getPluginId() {
        return (String)getProperty(PROP_PLUGIN_ID);
    }

    public Collection<TaskContext> getTasks() {
        return (Collection<TaskContext>)getProperty(PROP_TASKS);
    }
}
