/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.config;

import java.util.Dictionary;

/**
 * A class representing an e-mail configuration.
 *
 * @author Dan Noguerol
 */
public class EmailConfiguration {
    public static final String PROP_MAIL_SERVER = "mail.server";
    public static final String PROP_MAIL_SECURE = "mail.secure";
    public static final String PROP_MAIL_USERNAME = "mail.username";
    public static final String PROP_MAIL_PASSWORD = "mail.password";
    public static final String PROP_MAIL_SENDER = "mail.sender";

    private String server;
    private Boolean secure;
    private String username;
    private String password;
    private String senderAddress;

    public boolean hasServer() {
        return (server != null);
    }

    public String getServer() {
        return server;
    }

    public boolean hasSecure() {
        return (secure != null);
    }

    public Boolean isSecure() {
        return secure;
    }

    public boolean hasUsername() {
        return (username != null);
    }

    public String getUsername() {
        return username;
    }

    public boolean hasPassword() {
        return (password != null);
    }

    public String getPassword() {
        return password;
    }

    public boolean hasSenderAddress() {
        return (senderAddress != null);
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public static class Builder {
        private EmailConfiguration config = new EmailConfiguration();

        public Builder dictionary(Dictionary d) {
            config.server = (String)d.get(PROP_MAIL_SERVER);
            config.secure = (Boolean)d.get(PROP_MAIL_SECURE);
            config.username = (String)d.get(PROP_MAIL_USERNAME);
            config.password = (String)d.get(PROP_MAIL_PASSWORD);
            config.senderAddress = (String)d.get(PROP_MAIL_SENDER);
            return this;
        }

        public Builder server(String mailServer) {
            config.server = mailServer;
            return this;
        }

        public Builder secure(Boolean smtps) {
            config.secure = smtps;
            return this;
        }

        public Builder username(String username) {
            config.username = username;
            return this;
        }

        public Builder password(String password) {
            config.password = password;
            return this;
        }

        public Builder senderAddress(String senderAddress) {
            config.senderAddress = senderAddress;
            return this;
        }

        public EmailConfiguration build() {
            return config;
        }
    }
}
