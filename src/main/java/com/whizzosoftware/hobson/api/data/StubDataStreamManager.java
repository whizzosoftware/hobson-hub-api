/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.data;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A stub manager used as a placeholder when no functional telemetry manager is available.
 *
 * @author Dan Noguerol
 */
public class StubDataStreamManager implements DataStreamManager {
    @Override
    public boolean isStub() {
        return true;
    }

    @Override
    public String createDataStream(HubContext ctx, String name, Collection<DataStreamField> fields, Set<String> tags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteDataStream(HubContext ctx, String dataStreamId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<DataStream> getDataStreams(HubContext ctx) {
        return null;
    }

    @Override
    public DataStream getDataStream(HubContext ctx, String dataStreamId) {
        return null;
    }

    @Override
    public void addData(HubContext ctx, String dataStreamId, long now, Map<String, Object> data) {
    }

    @Override
    public List<DataStreamValueSet> getData(HubContext ctx, String dataStreamId, long endTime, DataStreamInterval interval) {
        return null;
    }
}
