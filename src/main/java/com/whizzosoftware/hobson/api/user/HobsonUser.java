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
public class HobsonUser {
    private String id;
    private String givenName;
    private String familyName;
    private String email;
    private UserAccount account;

    public HobsonUser(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public UserAccount getAccount() {
        return account;
    }

    public boolean isLocal() {
        return (account == null);
    }

    public boolean isRemote() {
        return (account != null);
    }

    public static class Builder {
        private HobsonUser user;

        public Builder(String id) {
            user = new HobsonUser(id);
        }

        public Builder givenName(String givenName) {
            user.givenName = givenName;
            return this;
        }

        public Builder familyName(String familyName) {
            user.familyName = familyName;
            return this;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder account(UserAccount account) {
            user.account = account;
            return this;
        }

        public HobsonUser build() {
            return user;
        }
    }
}
