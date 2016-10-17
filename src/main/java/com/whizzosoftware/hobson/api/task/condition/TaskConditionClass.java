/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.task.condition;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.*;

import java.util.List;

/**
 * A class that describes a condition and the properties required to evaluate it.
 *
 * @author Dan Noguerol
 */
abstract public class TaskConditionClass extends PropertyContainerClass {
    private String name;
    private String descriptionTemplate;

    /**
     * Constructor.
     *
     * @param context the context of the plugin associated with the class
     * @param id the ID of the condition class
     * @param name the name of the condition class
     * @param descriptionTemplate a description template for the condition class. See {@link TaskConditionClass#getDescriptionTemplate() getDescriptionTemplate} for more information.
     */
    public TaskConditionClass(PluginContext context, String id, String name, String descriptionTemplate) {
        this(PropertyContainerClassContext.create(context, id), name, descriptionTemplate);
    }

    /**
     * Constructor.
     *
     * @param context the context of the plugin associated with the class
     * @param name the name of the condition class
     * @param descriptionTemplate a description template for the condition class. See {@link TaskConditionClass#getDescriptionTemplate() getDescriptionTemplate} for more information.
     */
    public TaskConditionClass(PropertyContainerClassContext context, String name, String descriptionTemplate) {
        super(context, PropertyContainerClassType.CONDITION);
        this.name = name;
        this.descriptionTemplate = descriptionTemplate;
        setSupportedProperties(createProperties());
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the description template is a String that can be used by a user interface to generate a human-readable
     * description of the condition class. The String can contain keys wrapped in curly braces (e.g. {message}) that
     * will be replaced by the property container's value at runtime.
     *
     * @return a String
     */
    public String getDescriptionTemplate() {
        return descriptionTemplate;
    }

    /**
     * Returns the type of the condition class (e.g. trigger or evaluator).
     *
     * @return a ConditionClassType enum value
     */
    abstract public ConditionClassType getConditionClassType();

    /**
     * Performs evaluation of the condition class against a PropertyContainer of values.
     *
     * @param context an evaluation context
     * @param values the values to use for the evaluation
     *
     * @return a boolean indicating whether the condition is true or not
     */
    abstract public boolean evaluate(ConditionEvaluationContext context, PropertyContainer values);

    /**
     * Returns a list of the properties associated with this condition class.
     *
     * @return a List of TypedProperty instances (or null if the condition class has no properties)
     */
    abstract protected List<TypedProperty> createProperties();
}
