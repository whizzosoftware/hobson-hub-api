/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task.condition;

import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;

/**
 * An interface used by callbacks that evaluate conditions.
 *
 * @author Dan Noguerol
 */
public interface ConditionEvaluationContext {
    DeviceVariableState getDeviceVariableState(DeviceVariableContext dvctx);
}
