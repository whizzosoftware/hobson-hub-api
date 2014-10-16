/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.action.impl;

import com.whizzosoftware.hobson.api.action.HobsonAction;
import com.whizzosoftware.hobson.api.action.meta.ActionMeta;
import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for Hobson actions.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHobsonAction implements HobsonAction {
    private String pluginId;
    private String id;
    private String name;
    private VariableManager variableManager;
    private ConfigurationManager configManager;
    private List<ActionMeta> metaList = new ArrayList<ActionMeta>();

    public AbstractHobsonAction(String pluginId, String id, String name) {
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<ActionMeta> getMeta() {
        return metaList;
    }

    public void setVariableManager(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    public VariableManager getVariableManager() {
        return variableManager;
    }

    public void setConfigurationManager(ConfigurationManager configManager) {
        this.configManager = configManager;
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    protected void addMeta(ActionMeta meta) {
        metaList.add(meta);
    }
}
