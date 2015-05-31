package com.whizzosoftware.hobson.api.task.action;

import java.util.Map;

public interface TaskActionExecutor {
    public void execute(Map<String,Object> propertyValues);
}
