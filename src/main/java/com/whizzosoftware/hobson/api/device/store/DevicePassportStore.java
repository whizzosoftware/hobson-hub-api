/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.store;

import com.whizzosoftware.hobson.api.device.DevicePassport;
import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.Collection;

/**
 * An interface for persistent storage of device passports.
 *
 * @author Dan Noguerol
 */
public interface DevicePassportStore {
    /**
     * Returns all stored device passports.
     *
     * @param ctx the hub context
     *
     * @return a Collection of DevicePassport objects
     */
    Collection<DevicePassport> getAllPassports(HubContext ctx);

    /**
     * Returns a device passport with a specific ID.
     *
     * @param hctx the hub context
     * @param id the device passport ID
     *
     * @return a DevicePassport instance (or null if not found)
     */
    DevicePassport getPassport(HubContext hctx, String id);

    /**
     * Returns whether a device passport exists for the specified device ID.
     *
     * @param hctx the hub context
     * @param deviceId the device ID
     *
     * @return a boolean
     */
    boolean hasPassportForDeviceId(HubContext hctx, String deviceId);

    /**
     * Returns a device passport with a specific device ID.
     *
     * @param hctx the hub context
     * @param deviceId the device ID
     *
     * @return a DevicePassport instance (or null if not found)
     */
    DevicePassport getPassportForDeviceId(HubContext hctx, String deviceId);

    /**
     * Saves a device passport to persistent storage.
     *
     * @param hctx the hub context
     * @param passport the device passport to save
     *
     * @return the DevicePassport instance in its post-saved state
     */
    DevicePassport savePassport(HubContext hctx, DevicePassport passport);

    /**
     * Deletes a device passport.
     *
     * @param hctx the hub context
     * @param id the device passport ID
     */
    void deletePassport(HubContext hctx, String id);
}
