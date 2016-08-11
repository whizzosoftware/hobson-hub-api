/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A generic container that defines a set of typed properties that can be assigned values. This is used to define
 * what the supported properties (configurable aspects) of an entity are. The actual assignment of real values to
 * those supported properties is done via a PropertyContainer object which has both a reference to a
 * PropertyContainerClass and a set of values for its supported properties.
 *
 * For example, a Hub will have multiple PropertyContainerClass instances that expose each element of it configuration
 * (e.g. name, log level, etc.)
 *
 * @author Dan Noguerol
 */
public class PropertyContainerClass implements Serializable { // TODO: remove
    private PropertyContainerClassContext context;
    private String name;
    private PropertyContainerClassType type;
    private String descriptionTemplate;
    private List<TypedProperty> supportedProperties;

    // TODO: create builder to streamline construction with many supported properties

    public PropertyContainerClass(PropertyContainerClassContext context, PropertyContainerClassType type) {
        this(context, null, type, null, null);
    }

    public PropertyContainerClass(PropertyContainerClassContext context, String name, PropertyContainerClassType type, String descriptionTemplate, List<TypedProperty> supportedProperties) {
        this.context = context;
        this.name = name;
        this.type = type;
        this.descriptionTemplate = descriptionTemplate;
        this.supportedProperties = supportedProperties;
    }

    public PropertyContainerClassContext getContext() {
        return context;
    }

    public void setContext(PropertyContainerClassContext context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyContainerClassType getType() {
        return type;
    }

    /**
     * Returns the description template is a String that can be used by a user interface to generate a human-readable
     * description of a property container (e.g. condition or action). The String can contain keys wrapped in
     * curly braces (e.g. {message}) that will be replaced by the property container's value at runtime.
     *
     * @return a String
     */
    public String getDescriptionTemplate() {
        return descriptionTemplate;
    }

    public void setDescriptionTemplate(String descriptionTemplate) {
        this.descriptionTemplate = descriptionTemplate;
    }

    public boolean hasSupportedProperties() {
        return (supportedProperties != null && supportedProperties.size() > 0);
    }

    public List<TypedProperty> getSupportedProperties() {
        return supportedProperties;
    }

    public TypedProperty getSupportedProperty(String id) {
        if (supportedProperties != null) {
            for (TypedProperty tp : supportedProperties) {
                if (tp.getId() != null && tp.getId().equals(id)) {
                    return tp;
                }
            }
        }
        return null;
    }

    public void setSupportedProperties(List<TypedProperty> supportedProperties) {
        this.supportedProperties = supportedProperties;
    }

    public void addSupportedProperty(TypedProperty property) {
        if (supportedProperties == null) {
            supportedProperties = new ArrayList<>();
        }
        supportedProperties.add(property);
    }

    /**
     * Evaluates whether all the property constraints associated with this property container class are met
     * (if they exist).
     *
     * @param publishedVariableNames a list of all published variable names
     *
     * @return a boolean
     */
    public boolean evaluatePropertyConstraints(Collection<String> publishedVariableNames) {
        if (supportedProperties != null) {
            for (TypedProperty tp : supportedProperties) {
                if (!tp.evaluateConstraints(publishedVariableNames)) {
                    return false;
                }
            }
        }
        return true;
    }
}
