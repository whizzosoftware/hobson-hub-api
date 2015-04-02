/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.user;

/**
 * Encapsulates information about a Hobson user.
 *
 * @author Dan Noguerol
 */
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {
        private User user = new User();

        public Builder id(String id) {
            user.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            user.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            user.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public User build() {
            return user;
        }
    }
}
