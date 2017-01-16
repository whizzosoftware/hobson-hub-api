package com.whizzosoftware.hobson.api.event.task;

import com.whizzosoftware.hobson.api.task.TaskContext;

import java.util.Map;

public class TaskCreatedEvent extends TaskEvent {
    public static final String ID = "taskCreated";

    private static final String PROP_TASK = "task";

    public TaskCreatedEvent(long timestamp, TaskContext ctx) {
        super(timestamp, ID);
        setProperty(PROP_TASK, ctx);
    }

    public TaskCreatedEvent(Map<String,Object> properties) {
        super(properties);
    }

    public TaskContext getTask() {
        return (TaskContext)getProperty(PROP_TASK);
    }
}
