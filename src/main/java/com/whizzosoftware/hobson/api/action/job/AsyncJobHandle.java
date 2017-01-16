/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.action.job;

import io.netty.util.concurrent.Future;

public class AsyncJobHandle {
    private String jobId;
    private Future startFuture;

    public AsyncJobHandle(String jobId, Future startFuture) {
        this.jobId = jobId;
        this.startFuture = startFuture;
    }

    public String getJobId() {
        return jobId;
    }

    public Future getStartFuture() {
        return startFuture;
    }
}
