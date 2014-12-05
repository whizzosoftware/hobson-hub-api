/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A DeviceAdvertisementListener receives DeviceAdvertisements for a particular protocol via a callback. This enables
 * them to analyze advertisements, check for devices they know how to interact with and publish them as Hobson devices
 * if appropriate.
 *
 * @author Dan Noguerol
 */
public interface DeviceAdvertisementListener {
    /**
     * Callback made when a device advertisement of possible interest is detected.
     *
     * @param advertisement the advertisement
     */
    public void onDeviceAdvertisement(DeviceAdvertisement advertisement);
}
