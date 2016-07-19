/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.data;

import com.whizzosoftware.hobson.api.HobsonNotFoundException;
import com.whizzosoftware.hobson.api.variable.VariableContext;

import javax.inject.Inject;
import java.util.*;

/**
 * An implementation of TelemetryManager that delegates operations to a DataStreamMetaStore and a
 * DataStreamDataStore.
 *
 * @author Dan Noguerol
 */
public class DelegatingTelemetryManager implements TelemetryManager {
    private DataStreamMetaStore metaStore;
    private DataStreamDataStore dataStore;

    @Inject
    public DelegatingTelemetryManager(DataStreamMetaStore metaStore, DataStreamDataStore dataStore) {
        this.metaStore = metaStore;
        this.dataStore = dataStore;
    }

    @Override
    public boolean isStub() {
        return false;
    }

    @Override
    public String createDataStream(String userId, String name, Collection<DataStreamField> fields, Collection<String> tags) {
        return metaStore.createDataStream(userId, name, fields, tags);
    }

    @Override
    public Collection<DataStream> getDataStreams(String userId) {
        Collection<DataStream> results = metaStore.getDataStreams(userId);
        if (results != null) {
            return results;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public DataStream getDataStream(String dataStreamId) {
        DataStream ds = metaStore.getDataStream(dataStreamId);
        if (ds != null) {
            return ds;
        } else {
            throw new HobsonNotFoundException("Unable to find data stream: " + dataStreamId);
        }
    }

    @Override
    public Set<VariableContext> getMonitoredVariables(String userId) {
        return metaStore.getMonitoredVariables(userId);
    }

    @Override
    public void addData(String dataStreamId, long now, Map<String, Object> data) {
        dataStore.addData(dataStreamId, now, data);
    }

    @Override
    public Collection<TemporalValueSet> getData(String dataStreamId, long endTime, TelemetryInterval interval) {
        return dataStore.getData(dataStreamId, endTime, interval);
    }
}
