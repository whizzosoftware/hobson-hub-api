/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task.action;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;

import java.util.List;

/**
 * A class that describes an action and the properties required to execute it.
 *
 * @author Dan Noguerol
 */
abstract public class TaskActionClass extends PropertyContainerClass {
    /**
     * Constructor.
     *
     * @param context the context of the plugin associated with the class
     * @param id the ID of the action class
     * @param name the name of the action class
     * @param descriptionTemplate a description template for the condition class. See {@link PropertyContainerClass#getDescriptionTemplate() getDescriptionTemplate} for more information.
     */
    public TaskActionClass(PluginContext context, String id, String name, String descriptionTemplate) {
        this(PropertyContainerClassContext.create(context, id), name, descriptionTemplate);
    }

    /**
     * Constructor.
     *
     * @param context the context of the plugin associated with the class
     * @param name the name of the action class
     * @param descriptionTemplate a description template for the action class. See {@link PropertyContainerClass#getDescriptionTemplate() getDescriptionTemplate} for more information.
     */
    public TaskActionClass(PropertyContainerClassContext context, String name, String descriptionTemplate) {
        super(context);
        setName(name);
        setDescriptionTemplate(descriptionTemplate);
        setSupportedProperties(createProperties());
    }

    /**
     * Returns the executor for this action class.
     *
     * @return a TaskActionExecutor instance
     */
    abstract public TaskActionExecutor getExecutor();

    /**
     * Returns a list of the properties associated with this action class.
     *
     * @return a List of TypedProperty instances (or null if the action class has no properties)
     */
    abstract protected  List<TypedProperty> createProperties();
}
