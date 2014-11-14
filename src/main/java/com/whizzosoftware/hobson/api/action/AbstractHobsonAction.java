/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action;

import com.whizzosoftware.hobson.api.action.meta.ActionMeta;
import com.whizzosoftware.hobson.api.config.ConfigurationManager;
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
    private String pluginId;
    private String id;
    private String name;
    private VariableManager variableManager;
    private ConfigurationManager configManager;
    private List<ActionMeta> metaList = new ArrayList<ActionMeta>();

    /**
     * Constructor.
     *
     * @param pluginId the ID Of the plugin creating the action
     * @param id the action ID
     * @param name the action name
     *
     * @since hobson-hub-api 0.1.6
     */
    public AbstractHobsonAction(String pluginId, String id, String name) {
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
    }

    /**
     * Retrieves the plugin ID.
     *
     * @return the plugin ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public String getPluginId() {
        return pluginId;
    }

    /**
     * Returns the action ID.
     *
     * @return the action ID
     *
     * @since hobson-hub-api 0.1.6
     */
    public String getId() {
        return id;
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
     * Returns meta-data about the action.
     *
     * @return a Collection of ActionMeta objects
     *
     * @since hobson-hub-api 0.1.6
     */
    public Collection<ActionMeta> getMeta() {
        return metaList;
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
     * Sets the ConfigurationManager instance in use by this action.
     *
     * @param configManager a ConfigurationManager instance
     *
     * @since hobson-hub-api 0.1.6
     */
    public void setConfigurationManager(ConfigurationManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Returns the ConfigurationManager instance in use by this action.
     *
     * @return a ConfigurationManager instance
     *
     * @since hobson-hub-api 0.1.6
     */
    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    /**
     * Adds meta-data about this action.
     *
     * @param meta the ActionMeta object to add
     *
     * @since hobson-hub-api 0.1.6
     */
    protected void addMeta(ActionMeta meta) {
        metaList.add(meta);
    }
}
