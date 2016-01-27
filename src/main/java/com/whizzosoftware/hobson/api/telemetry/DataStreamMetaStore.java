/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.telemetry;

import com.whizzosoftware.hobson.api.variable.VariableContext;

import java.util.Collection;
import java.util.Set;

/**
 * An interface for storage of data stream meta data.
 *
 * @author Dan Noguerol
 */
public interface DataStreamMetaStore {
    /**
     * Creates a new data stream.
     *
     * @param userId the user ID that owns the data stream
     * @param name the name of the data stream
     * @param variables the variable contexts that comprise the data stream
     *
     * @return the ID of the new data stream
     */
    String createDataStream(String userId, String name, Collection<VariableContext> variables);

    /**
     * Retrieves all data streams for a user.
     *
     * @param userId the user ID that owns the data streams
     *
     * @return a Collection of DataStream objects (will be an empty collection if the user has no streams)
     */
    Collection<DataStream> getDataStreams(String userId);

    /**
     * Retrieves a specific data stream for a user.
     *
     * @param userId the user ID that owns the data stream
     * @param dataStreamId the data stream ID
     *
     * @return a DataStream instance (returns null if the data stream does not exist)
     */
    DataStream getDataStream(String userId, String dataStreamId);

    /**
     * Returns all variables referenced by a user's data streams.
     *
     * @param userId the user ID that owns the data streams
     *
     * @return a Set of VariableContext instances (returns an empty set if the user has no streams)
     */
    Set<VariableContext> getMonitoredVariables(String userId);
}
