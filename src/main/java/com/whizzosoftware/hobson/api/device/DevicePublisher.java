/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;

/**
 * An interface containing methods related to publishing and unpublishing of devices.
 *
 * @author Dan Noguerol
 */
public interface DevicePublisher {
    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param plugin the HobsonPlugin instance performing the action
     * @param device the HobsonDevice to publish
     *
     * @since hobson-hub-api 0.1.6
     */
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device);

    /**
     * Publishes a device to the device registry and starts it.
     *
     * @param plugin the HobsonPlugin instance performing the action
     * @param device the HobsonDevice to publish
     * @param republish indicates whether this is a forced republish of an existing device
     *
     * @since hobson-hub-api 0.4.2
     */
    public void publishDevice(HobsonPlugin plugin, HobsonDevice device, boolean republish);

    /**
     * Stops and unpublishes a device associated with a specific plugin. This allows plugins that require it
     * (e.g. the RadioRA plugin) to unpublish individual devices.
     *
     * @param plugin the HobsonPlugin instance performing the action
     * @param deviceId the device ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishDevice(HobsonPlugin plugin, String deviceId);

    /**
     * Stops an unpublishes all devices associated with a specific plugin.
     *
     * @param plugin the HobsonPlugin instance performing the action
     *
     * @since hobson-hub-api 0.1.6
     */
    public void unpublishAllDevices(HobsonPlugin plugin);
}
