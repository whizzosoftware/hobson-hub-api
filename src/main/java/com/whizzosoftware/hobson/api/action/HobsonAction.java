/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.action.meta.ActionMetaData;
import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.variable.VariableManager;

import java.util.Collection;
import java.util.Map;

/**
 * An interface for actions that Hobson can perform.
 *
 * @author Dan Noguerol
 */
public interface HobsonAction {
    /**
     * Returns the action context.
     *
     * @return an ActionContext instance
     */
    public ActionContext getContext();

    /**
     * Returns the name of the action.
     *
     * @return the action name
     */
    public String getName();

    /**
     * Sets the VariableManager the action should use when it executes.
     *
     * @param variableManager a VariableManager instance
     */
    public void setVariableManager(VariableManager variableManager);

    /**
     * Returns meta-data about the action.
     *
     * @return a Collection of ActionMeta instances
     */
    public Collection<ActionMetaData> getMetaData();

    /**
     * Executes the action.
     *
     * @param hubManager a HubManager instance that the action can use to get info about the Hub
     * @param properties a Map of action properties
     */
    public void execute(HubManager hubManager, Map<String,Object> properties);
}
