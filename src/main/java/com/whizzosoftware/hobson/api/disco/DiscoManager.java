/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A manager interface for managing device advertisement listeners.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DiscoManager {
    /**
     * Allows a plugin to request all known device advertisements. This is an asynchronous call that will be serviced
     * as multiple DeviceAdvertisementEvent events to the plugin's onHobsonEvent() callback.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the ID of the plugin requesting the snapshot
     * @param protocolId the protocol ID for the advertisements requested
     */
    public void requestDeviceAdvertisementSnapshot(String userId, String hubId, String pluginId, String protocolId);

    /**
     * Fires a DeviceAdvertisement. This should perform callbacks for any registered DeviceAdvertisementListeners
     * that are available for the advertisement's protocol.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param advertisement the advertisement to publish
     */
    public void fireDeviceAdvertisement(String userId, String hubId, DeviceAdvertisement advertisement);
}
