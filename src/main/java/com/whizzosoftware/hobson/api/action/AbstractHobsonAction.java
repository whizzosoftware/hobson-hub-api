/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.action.meta.ActionMetaData;
import com.whizzosoftware.hobson.api.variable.VariableManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for Hobson actions.
 *
 * @author Dan Noguerol
 * @since hobson-hub-api 0.1.6
 */
abstract public class AbstractHobsonAction implements HobsonAction {
    private ActionContext ctx;
    private String name;
    private VariableManager variableManager;
    private List<ActionMetaData> metaList = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param ctx the context of the action
     * @param name the action name
     *
     * @since hobson-hub-api 0.1.6
     */
    public AbstractHobsonAction(ActionContext ctx, String name) {
        this.ctx = ctx;
        this.name = name;
    }

    /**
     * Retrieves the action context.
     *
     * @return the plugin ID
     *
     * @since hobson-hub-api 0.5.0
     */
    public ActionContext getContext() {
        return ctx;
    }

    /**
     * Returns the action name.
     *
     * @return the action name
     *
     * @since hobson-hub-api 0.1.6
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the VariableManager instance used by this action.
     *
     * @param variableManager a VariableManager instance
     *
     * @since hobson-hub-api 0.1.6
     */
    public void setVariableManager(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    /**
     * Returns the VariableManager instance in use by this action.
     *
     * @return a VariableManager instance
     *
     * @since hobson-hub-api 0.1.6
     */
    public VariableManager getVariableManager() {
        return variableManager;
    }

    /**
     * Returns metadata about the action.
     *
     * @return a Collection of ActionMeta objects
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<ActionMetaData> getMetaData() {
        return metaList;
    }

    /**
     * Adds metadata about this action.
     *
     * @param meta the ActionMeta object to add
     *
     * @since hobson-hub-api 0.1.6
     */
    protected void addMetaData(ActionMetaData meta) {
        metaList.add(meta);
    }
}
