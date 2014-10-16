/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

public interface ExternalBridgeMetaAnalysisContext {
    /**
     * Adds a newly identified ExternalBridge instance.
     *
     * @param bridge the bridge instance to add
     */
    public void addExternalBridge(ExternalBridge bridge);

    /**
     * Removes a previously identified and added ExternalBridge instance.
     *
     * @param bridgeId the ID of the bridge to remove
     */
    public void removeExternalBridge(String bridgeId);
}
