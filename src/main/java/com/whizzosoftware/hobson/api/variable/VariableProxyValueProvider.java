/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.variable;

/**
 * An interface for classes that can perform value substitutions on variables.
 *
 * @author Dan Noguerol
 */
public interface VariableProxyValueProvider {
    /**
     * Returns the proxy type for this provider.
     *
     * @return a VariableProxyType instance
     */
    VariableProxyType getProxyType();

    /**
     * Returns the proxy value for a variable.
     *
     * @param v the variable
     *
     * @return a value
     */
    Object getProxyValue(HobsonVariable v);
}
