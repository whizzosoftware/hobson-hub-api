/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

/**
 * A class representing meta information about an external bridge. This information represents an "unknown" external
 * bridge. It will be the job of an ExternalBridgeMetaAnalyzer to covert the meta information into an actual
 * ExternalBridge instance if it can successfully identify it.
 *
 * @author Dan Noguerol
 */
public class ExternalBridgeMeta {
    private String scannerId;
    private String data;

    public ExternalBridgeMeta(String scannerId, String data) {
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
