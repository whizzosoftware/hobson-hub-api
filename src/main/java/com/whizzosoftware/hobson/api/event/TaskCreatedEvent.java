package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Map;

public class TaskCreatedEvent extends HobsonEvent {
    public static final String ID = "taskCreated";

    private static final String PROP_TASK = "task";

    public TaskCreatedEvent(long timestamp, TaskContext ctx) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);
        setProperty(PROP_TASK, ctx);
    }

    public TaskCreatedEvent(Map<String,Object> properties) {
        super(EventTopics.STATE_TOPIC, properties);
    }

    public TaskContext getTask() {
        return (TaskContext)getProperty(PROP_TASK);
    }
}
