package com.whizzosoftware.hobson.api.event.task;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Collection;
import java.util.Map;

public class TaskRegistrationEvent extends TaskEvent {
    public static final String ID = "taskRegistration";

    private static final String PROP_TASKS = "tasks";

    public TaskRegistrationEvent(long timestamp, Collection<TaskContext> tasks) {
        super(timestamp, ID);
        setProperty(PROP_TASKS, tasks);
    }

    public TaskRegistrationEvent(Map<String,Object> properties) {
        super(properties);
    }

    public Collection<TaskContext> getTasks() {
        return (Collection<TaskContext>)getProperty(PROP_TASKS);
    }
}
