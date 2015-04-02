/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;


import com.whizzosoftware.hobson.api.HobsonRuntimeException;

/**
 * Exception for configuration errors.
 *
 * @author Dan Noguerol
 */
public class ConfigurationException extends HobsonRuntimeException {
    public ConfigurationException(String msg, Throwable t) {
        super(CODE_INTERNAL_ERROR, msg, t);
    }
    public ConfigurationException(String msg) {
        super(CODE_INTERNAL_ERROR, msg);
    }
}
