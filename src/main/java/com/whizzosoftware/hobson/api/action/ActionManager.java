/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.action.job.AsyncJobHandle;
import com.whizzosoftware.hobson.api.action.job.JobInfo;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerSet;

import java.util.Collection;

/**
 * An interface for managing Hobson actions.
 *
 * @author Dan Noguerol
 */
public interface ActionManager {
    /**
     * Adds a status message to all running jobs associated with a plugin.
     *
     * @param ctx the plugin context
     * @param msgName the message name
     * @param props a property object
     */
    void addJobStatusMessage(PluginContext ctx, String msgName, Object props);

    /**
     * Executes an action.
     *
     * @param action the action's property container
     *
     * @return an AsyncJobHandle
     */
    AsyncJobHandle executeAction(PropertyContainer action);

    /**
     * Executes an action set.
     *
     * @param actionSet the property container set
     *
     * @return an AsyncJobHandle
     */
    AsyncJobHandle executeActionSet(PropertyContainerSet actionSet);

    /**
     * Returns a published action class.
     *
     * @param ctx the property container class context
     *
     * @return an ActionClass
     */
    ActionClass getActionClass(PropertyContainerClassContext ctx);

    /**
     * Returns all action classes published by a plugin.
     *
     * @param ctx the plugin context
     *
     * @return a Collection of ActionClass instances
     */
    Collection<ActionClass> getActionClasses(PluginContext ctx);

    /**
     * Returns all published action classes.
     *
     * @param ctx the context of the hub that published the action classes
     * @param applyConstraints only return condition classes for which the constraints of their typed properties can be
     *                         met by the currently available system services (i.e. don't show the user things they
     *                         can't do)
     *
     * @return a Collection of TaskActionClass instances
     */
    Collection<ActionClass> getActionClasses(HubContext ctx, boolean applyConstraints);

    /**
     * Returns information about a running job.
     *
     * @param ctx the hub context
     * @param jobId the job ID
     *
     * @return a JobInfo instance
     */
    JobInfo getJobInfo(HubContext ctx, String jobId);

    /**
     * Indicates whether an ActionClass has been published.
     *
     * @param ctx the property container class context
     *
     * @return a boolean
     */
    boolean hasActionClass(PropertyContainerClassContext ctx);

    /**
     * Publishes an action class.
     *
     * @param actionClass the action class to publish
     */
    void publishActionClass(ActionClass actionClass);

    /**
     * Unpublish all action providers published by a plugin.
     *
     * @param ctx the plugin context
     */
    void unpublishActionClasses(PluginContext ctx);
}