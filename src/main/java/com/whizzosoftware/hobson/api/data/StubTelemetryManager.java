/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.data;

import com.whizzosoftware.hobson.api.variable.VariableContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A stub manager used as a placeholder when no functional telemetry manager is available.
 *
 * @author Dan Noguerol
 */
public class StubTelemetryManager implements TelemetryManager {
    @Override
    public boolean isStub() {
        return true;
    }

    @Override
    public String createDataStream(String userId, String name, Collection<DataStreamField> fields, Collection<String> tags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<DataStream> getDataStreams(String userId) {
        return null;
    }

    @Override
    public DataStream getDataStream(String dataStreamId) {
        return null;
    }

    @Override
    public Set<VariableContext> getMonitoredVariables(String userId) {
        return null;
    }

    @Override
    public void addData(String dataStreamId, long now, Map<String, Object> data) {
    }

    @Override
    public List<TemporalValueSet> getData(String dataStreamId, long endTime, TelemetryInterval interval) {
        return null;
    }
}
