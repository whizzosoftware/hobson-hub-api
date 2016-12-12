/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************
*/
package com.whizzosoftware.hobson.api.hub;

/**
 * An interface for objects that can provide an OpenID Connect configuration.
 *
 * @author Dan Noguerol
 */
public interface OIDCConfigProvider {
    /**
     * Returns an OIDC configuration.
     *
     * @return an OIDCConfig instance
     */
    OIDCConfig getConfig();
}
