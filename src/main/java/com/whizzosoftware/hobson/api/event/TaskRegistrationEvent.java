package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Collection;
import java.util.Map;

public class TaskRegistrationEvent extends HobsonEvent {
    public static final String ID = "taskRegistration";

    private static final String PROP_TASKS = "tasks";

    public TaskRegistrationEvent(long timestamp, Collection<TaskContext> tasks) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
        setProperty(PROP_TASKS, tasks);
    }

    public TaskRegistrationEvent(Map<String,Object> properties) {
        super(EventTopics.STATE_TOPIC, properties);
    }

    public Collection<TaskContext> getTasks() {
        return (Collection<TaskContext>)getProperty(PROP_TASKS);
    }
}
