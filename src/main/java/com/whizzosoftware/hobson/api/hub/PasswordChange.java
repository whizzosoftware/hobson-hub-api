/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

/**
 * A class representing a password change.
 *
 * @author Dan Noguerol
 */
public class PasswordChange {
    private static final String PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"'@#$%&*()_+=|<>?{}\\\\\\[\\]~-]).{8,14}";

    private String currentPassword;
    private String newPassword;

    public PasswordChange(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Indicates whether the new password meets complexity requirements.
     *
     * @return a boolean
     */
    public boolean isValid() {
        return newPassword.matches(PATTERN);
    }
}
