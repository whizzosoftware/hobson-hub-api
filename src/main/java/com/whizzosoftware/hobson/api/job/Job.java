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
import io.netty.util.concurrent.Future;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A Job provides information about and control of an Action execution's lifecycle. This information includes its
 * start time, current status, status messages, etc.
 *
 * @author Dan Noguerol
 */
public class Job implements JobInfo, JobContext {
    private String id;
    private Action action;
    private long startTime;
    private long timeoutInterval;
    private JobStatus status;
    private List<String> messages = Collections.synchronizedList(new ArrayList<String>());

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

    @Override
    public List<String> getMessages() {
        return messages;
    }

    @Override
    public JobStatus getStatus() {
        return status;
    }

    public boolean isInProgress() {
        return (status.equals(JobStatus.InProgress));
    }

    public boolean hasExceededTimeout(long now) {
        return (status.equals(JobStatus.TimedOut) || (status.equals(JobStatus.InProgress) && now - startTime > timeoutInterval));
    }

    /**
     * Starts the job.
     *
     * @return a Future representing the completion of the job Action's onStartup method.
     */
    public Future start() {
        this.startTime = System.currentTimeMillis();
        this.status = JobStatus.InProgress;
        return action.getEventLoopExecutor().executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                action.onStart(Job.this);
            }
        });
    }

    /**
     * Sends an event to the job which its associated Action can process.
     *
     * @param name the event name
     * @param prop a property object (specific to the event)
     */
    public void event(final String name, final Object prop) {
        action.getEventLoopExecutor().executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                action.onEvent(Job.this, name, prop);
            }
        });
    }

    /**
     * Stops this job.
     */
    public void stop() {
        action.getEventLoopExecutor().executeInEventLoop(new Runnable() {
            @Override
            public void run() {
                action.onStop(Job.this);
            }
        });
    }

    /**
     * Updates the job with a new status message.
     *
     * @param msg the message to add
     */
    public void update(String msg) {
        messages.add(msg);
    }

    /**
     * Completes the job.
     *
     * @param msg the completion message
     */
    public void complete(String msg) {
        messages.add(msg);
        status = JobStatus.Complete;
    }

    /**
     * Fails the job.
     *
     * @param msg a failure message
     */
    public void fail(String msg) {
        messages.add(msg);
        status = JobStatus.Failed;
    }

    /**
     * Flag the job as timed out.
     */
    public void timeout() {
        status = JobStatus.TimedOut;
    }
}
