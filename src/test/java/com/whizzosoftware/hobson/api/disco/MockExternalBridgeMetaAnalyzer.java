/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.disco;

public class MockExternalBridgeMetaAnalyzer implements ExternalBridgeMetaAnalyzer {
    private String pluginId;
    private String id;

    public MockExternalBridgeMetaAnalyzer(String pluginId, String id) {
        this.pluginId = pluginId;
        this.id = id;
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean identify(ExternalBridgeMetaAnalysisContext context, ExternalBridgeMeta meta) {
        return false;
    }
}
