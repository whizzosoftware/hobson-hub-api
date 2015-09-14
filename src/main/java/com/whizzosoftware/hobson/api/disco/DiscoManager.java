/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

import java.util.Collection;

/**
 * A manager interface for managing device advertisement listeners.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
public interface DiscoManager {
    /**
     * Allows a plugin to request all known external device advertisements. This is an asynchronous call that will be
     * serviced as multiple DeviceAdvertisementEvent events to the plugin's onHobsonEvent() callback.
     *
     * @param ctx the context of the plugin requesting the snapshot
     * @param protocolId the protocol ID for the advertisements requested
     */
    public void requestExternalDeviceAdvertisementSnapshot(PluginContext ctx, String protocolId);

    public Collection<DeviceAdvertisement> getInternalDeviceAdvertisements(HubContext ctx, String protocolId);

    public DeviceAdvertisement getInternalDeviceAdvertisement(HubContext ctx, String protocolId, String advId);

    /**
     * Publishes an internal DeviceAdvertisement. This will insure that the advertisement is included in any discovery
     * requests that external clients make for a particular protocol.
     *
     * @param ctx the context of the hub publishing the advertisement
     * @param advertisement the advertisement to publish
     */
    public void publishDeviceAdvertisement(HubContext ctx, DeviceAdvertisement advertisement, boolean internal);
}
