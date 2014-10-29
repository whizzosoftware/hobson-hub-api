/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A class representing meta data about a device bridge. This information represents raw data collected from an
 * "unknown" device bridge. It will be the job of a DeviceBridgeDetector to convert this information into an actual
 * DeviceBridge instance if it can successfully identify it.
 *
 * @author Dan Noguerol
 */
public class DeviceBridgeMetaData {
    private String scannerId;
    private String data;

    /**
     * Constructor.
     *
     * @param scannerId the ID of the DeviceBridgeScanner that created the meta data
     * @param data the bridge meta data
     */
    public DeviceBridgeMetaData(String scannerId, String data) {
        this.scannerId = scannerId;
        this.data = data;
    }

    public String getScannerId() {
        return scannerId;
    }

    public String getData() {
        return data;
    }
}
