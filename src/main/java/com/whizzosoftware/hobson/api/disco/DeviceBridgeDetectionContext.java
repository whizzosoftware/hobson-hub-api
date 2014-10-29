/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * An interface passed to DeviceBridgeDetector objects to allow them to add or remove DeviceBridges.
 *
 * @author Dan Noguerol
 */
public interface DeviceBridgeDetectionContext {
    /**
     * Adds a newly identified DeviceBridge instance.
     *
     * @param bridge the bridge instance to add
     */
    public void addDeviceBridge(DeviceBridge bridge);

    /**
     * Removes a previously added DeviceBridge instance.
     *
     * @param bridgeId the ID of the bridge to remove
     */
    public void removeDeviceBridge(String bridgeId);
}
