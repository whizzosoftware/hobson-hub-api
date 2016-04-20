/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

/**
 * Encapsulates the result of user authentication including user info and an authentication token.
 *
 * @author Dan Noguerol
 */
public class UserAuthentication {
    private HobsonUser user;
    private String token;

    public UserAuthentication(HobsonUser user, String token) {
        this.user = user;
        this.token = token;
    }

    public HobsonUser getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
