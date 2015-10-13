/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.property;

/**
 * This is a constraint that applies to a property to determine whether it is valid and applicable. For example,
 * a constraint of "deviceVariable" means that the property is only applicable for devices of that publish a variable
 * with the name given by the constraint argument.
 *
 * @author Dan Noguerol
 */
public class TypedPropertyConstraint {
    private PropertyConstraintType type;
    private Object argument;

    public TypedPropertyConstraint(PropertyConstraintType type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    /**
     * Returns the type of constraint.
     *
     * @return a PropertyConstraintType instance
     */
    public PropertyConstraintType getType() {
        return type;
    }

    /**
     * Returns an argument that is associated with the constraint. This will be specific to the constraint type.
     *
     * @return an Object (or null if there is no argument)
     */
    public Object getArgument() {
        return argument;
    }
}
