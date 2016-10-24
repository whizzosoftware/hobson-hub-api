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

import com.whizzosoftware.hobson.api.action.Action;
import com.whizzosoftware.hobson.api.action.ActionLifecycleContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import io.netty.util.concurrent.Future;

import java.util.*;

/**
 * A Job provides information about and control of an Action execution's lifecycle. This information includes its
 * start time, current status, status messages, etc.
 *
 * @author Dan Noguerol
 */
public class Job implements JobInfo, ActionLifecycleContext {
    private Action action;
    private String id;
    private long startTime;
    private List<String> statusMessages = Collections.synchronizedList(new ArrayList<String>());
    private JobStatus status;
    private long timeoutInterval;

    /**
     * Constructor.
     *
     * @param action the action associated with this job
     * @param timeoutInterval the maximum interval the job is allowed to run before being considered timed out
     */
    public Job(Action action, long timeoutInterval) {
        this.action = action;
        this.timeoutInterval = timeoutInterval;

        id = UUID.randomUUID().toString();
        status = JobStatus.Initialized;
    }

    public String getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean hasExceededTimeout(long now) {
        return (status.equals(JobStatus.TimedOut) || (status.equals(JobStatus.InProgress) && now - startTime > timeoutInterval));
    }

    public boolean isAssociatedWithPlugin(PluginContext ctx) {
        return action.isAssociatedWithPlugin(ctx);
    }

    public boolean isComplete() {
        return (status.equals(JobStatus.Complete));
    }

    public boolean isInProgress() {
        return (status.equals(JobStatus.InProgress));
    }

    /**
     * Sends a message to the job which its associated Action can process.
     *
     * @param name the message name
     * @param prop a property object associated with the message
     */
    public Future message(final String name, final Object prop) {
        return action.sendMessage(this, name, prop);
    }

    /**
     * Starts the job.
     *
     * @return a Future representing the completion of the job Action's onStartup method.
     */
    public Future start() {
        this.startTime = System.currentTimeMillis();
        this.status = JobStatus.InProgress;
        return action.start(this);
    }

    /**
     * Stops this job.
     */
    public void stop() {
        action.stop(this);
    }

    /**
     * Flag the job as timed out.
     */
    public void timeout() {
        status = JobStatus.TimedOut;
    }

    //=================================================================================
    // JobInfo methods
    //=================================================================================

    @Override
    public JobStatus getStatus() {
        return status;
    }

    @Override
    public List<String> getStatusMessages() {
        return statusMessages;
    }

    //=================================================================================
    // ActionLifecycleContext methods
    //=================================================================================

    @Override
    public void complete() {
        complete(null);
    }

    @Override
    public void complete(String statusMsg) {
        if (statusMsg != null) {
            statusMessages.add(statusMsg);
        }
        status = JobStatus.Complete;
    }

    @Override
    public void fail(String statusMsg) {
        statusMessages.add(statusMsg);
        status = JobStatus.Failed;
    }

    @Override
    public void update(String statusMsg) {
        statusMessages.add(statusMsg);
    }
}
