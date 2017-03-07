/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private List<String> roles;
    private Collection<String> hubs;

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

    public boolean hasRole(String roleName) {
        if (roles != null) {
            for (String r : roles) {
                if (r.equals(roleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasRoles() {
        return (roles != null && roles.size() > 0);
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public boolean hasHubs() {
        return (hubs != null && hubs.size() > 0);
    }

    public Collection<String> getHubs() {
        return hubs;
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

        public Builder roles(List<String> roles) {
            user.roles = roles;
            return this;
        }

        public Builder role(String role) {
            if (user.roles == null) {
                user.roles = new ArrayList<>();
            }
            user.roles.add(role);
            return this;
        }

        public Builder hubs(List<String> hubs) {
            user.hubs = hubs;
            return this;
        }

        public HobsonUser build() {
            return user;
        }
    }
}
