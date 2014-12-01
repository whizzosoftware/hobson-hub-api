/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import java.util.Dictionary;

/**
 * Interface implemented by objects that want to receive device configuration update callbacks.
 *
 * @author Dan Noguerol
 */
public interface DeviceConfigurationListener {
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config);
}
