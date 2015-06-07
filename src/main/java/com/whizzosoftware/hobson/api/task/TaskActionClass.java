/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.task.action.TaskActionExecutor;

import java.util.List;

/**
 * A class that describes an action and the properties required to execute it.
 *
 * @author Dan Noguerol
 */
abstract public class TaskActionClass {
    private PropertyContainerClassContext context;
    private String name;
    private List<TypedProperty> properties;

    public TaskActionClass(PluginContext context, String id, String name) {
        this.context = PropertyContainerClassContext.create(context, id);
        this.name = name;
        this.properties = createProperties();
    }


    public TaskActionClass(PropertyContainerClassContext context, String name, List<TypedProperty> properties) {
        this.context = context;
        this.name = name;
        this.properties = properties;
    }

    public PropertyContainerClassContext getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public List<TypedProperty> getProperties() {
        return properties;
    };

    abstract public List<TypedProperty> createProperties();

    abstract public TaskActionExecutor getExecutor();
}
