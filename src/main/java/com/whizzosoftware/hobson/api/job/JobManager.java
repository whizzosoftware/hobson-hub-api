/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.job;

import com.whizzosoftware.hobson.api.action.Action;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;

/**
 * An interface for creating and managing jobs.
 *
 * @author Dan Noguerol
 */
public interface JobManager {
    AsyncJobHandle createJob(PluginContext ctx, Action action, long timeoutInterval);
    JobInfo getJobInfo(HubContext ctx, String jobId);
    void postEvent(PluginContext ctx, String name, Object o);
}
