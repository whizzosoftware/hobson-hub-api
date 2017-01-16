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

import java.util.List;

public interface JobInfo {
    /**
     * Returns the current status of the job.
     *
     * @return a JobStatus instance
     */
    JobStatus getStatus();

    /**
     * Returns status messages associated with the job.
     *
     * @return An ordered (oldest first) list of status messages
     */
    List<String> getStatusMessages();
}
