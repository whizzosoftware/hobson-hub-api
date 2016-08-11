package com.whizzosoftware.hobson.api.task.action;

import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import io.netty.util.concurrent.Future;

import java.util.Map;

public interface ActionExecutionContext {
    Future setDeviceVariable(DeviceVariableContext dvctx, Object value);
    Future setDeviceVariables(Map<DeviceVariableContext,Object> values);
}
