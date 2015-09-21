/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device.store;

import com.whizzosoftware.hobson.api.device.DeviceBootstrap;
import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.Collection;

/**
 * An interface for persistent storage of device bootstrap data.
 *
 * @author Dan Noguerol
 */
public interface DeviceBootstrapStore {
    /**
     * Returns all stored device bootstrap data.
     *
     * @param ctx the hub context
     *
     * @return a Collection of DeviceBootstrap objects
     */
    Collection<DeviceBootstrap> getAllBootstraps(HubContext ctx);

    /**
     * Returns a device bootstrap with a specific ID.
     *
     * @param hctx the hub context
     * @param id the device bootstrap ID
     *
     * @return a DeviceBootstrap instance (or null if not found)
     */
    DeviceBootstrap getBootstrap(HubContext hctx, String id);

    /**
     * Returns whether a device bootstrap exists for the specified device ID.
     *
     * @param hctx the hub context
     * @param deviceId the device ID
     *
     * @return a boolean
     */
    boolean hasBootstrapForDeviceId(HubContext hctx, String deviceId);

    /**
     * Returns a device bootstrap with a specific device ID.
     *
     * @param hctx the hub context
     * @param deviceId the device ID
     *
     * @return a DeviceBootstrap instance (or null if not found)
     */
    DeviceBootstrap getBoostrapForDeviceId(HubContext hctx, String deviceId);

    /**
     * Saves a device bootstrap to persistent storage.
     *
     * @param hctx the hub context
     * @param bootstrap the device bootstrap to save
     *
     * @return the DeviceBootstrap instance in its post-saved state
     */
    DeviceBootstrap saveBootstrap(HubContext hctx, DeviceBootstrap bootstrap);

    /**
     * Deletes a device bootstrap.
     *
     * @param hctx the hub context
     * @param id the device bootstrap ID
     */
    void deleteBootstrap(HubContext hctx, String id);
}
