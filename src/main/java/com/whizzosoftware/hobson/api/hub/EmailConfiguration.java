/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import java.util.Dictionary;

/**
 * A class representing an e-mail configuration.
 *
 * @author Dan Noguerol
 */
public class EmailConfiguration {
    public static final String PROP_MAIL_SERVER = "mail.server";
    public static final String PROP_MAIL_SMTPS = "mail.smtps";
    public static final String PROP_MAIL_USERNAME = "mail.username";
    public static final String PROP_MAIL_PASSWORD = "mail.password";
    public static final String PROP_MAIL_SENDER = "mail.sender";

    private String mailServer;
    private Boolean smtps;
    private String username;
    private String password;
    private String senderAddress;

    public EmailConfiguration(Dictionary d) {
        this.mailServer = (String)d.get(PROP_MAIL_SERVER);
        this.smtps = (Boolean)d.get(PROP_MAIL_SMTPS);
        this.username = (String)d.get(PROP_MAIL_USERNAME);
        this.password = (String)d.get(PROP_MAIL_PASSWORD);
        this.senderAddress = (String)d.get(PROP_MAIL_SENDER);
    }

    public EmailConfiguration(String mailServer, Boolean smtps, String username, String password, String senderAddress) {
        this.mailServer = mailServer;
        this.smtps = smtps;
        this.username = username;
        this.password = password;
        this.senderAddress = senderAddress;
    }

    public String getMailServer() {
        return mailServer;
    }

    public Boolean isSMTPS() {
        return smtps;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSenderAddress() {
        return senderAddress;
    }
}
