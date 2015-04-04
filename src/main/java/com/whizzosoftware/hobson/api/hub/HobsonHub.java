/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import com.whizzosoftware.hobson.api.config.EmailConfiguration;

/**
 * A class that encapsulates information about a hub.
 *
 * @author Dan Noguerol
 */
public class HobsonHub {
    private HubContext ctx;
    private String name;
    private String version;
    private HubLocation location;
    private EmailConfiguration email;
    private String logLevel;
    private Boolean setupComplete;
    private String cloudLinkUrl;

    public HobsonHub(HubContext ctx) {
        this.ctx = ctx;
    }

    public HubContext getContext() {
        return ctx;
    }

    public boolean hasName() {
        return (name != null);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean hasLocation() {
        return (location != null);
    }

    public HubLocation getLocation() {
        return location;
    }

    public boolean hasEmail() {
        return (email != null);
    }

    public EmailConfiguration getEmail() {
        return email;
    }

    public boolean hasLogLevel() {
        return false;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public boolean hasSetupComplete() {
        return (setupComplete != null);
    }

    public Boolean isSetupComplete() {
        return setupComplete;
    }

    public boolean hasCloudLinkUrl() {
        return (cloudLinkUrl != null);
    }

    public String getCloudLinkUrl() {
        return cloudLinkUrl;
    }

    public static class Builder {
        private HobsonHub hub;

        public Builder(HubContext ctx) {
            hub = new HobsonHub(ctx);
        }

        public Builder name(String name) {
            hub.name = name;
            return this;
        }

        public Builder version(String version) {
            hub.version = version;
            return this;
        }

        public Builder location(HubLocation location) {
            hub.location = location;
            return this;
        }

        public Builder email(EmailConfiguration email) {
            hub.email = email;
            return this;
        }

        public Builder logLevel(String logLevel) {
            hub.logLevel = logLevel;
            return this;
        }

        public Builder setupComplete(Boolean setupComplete) {
            hub.setupComplete = setupComplete;
            return this;
        }

        public Builder cloudLinkUrl(String cloudLinkUrl) {
            hub.cloudLinkUrl = cloudLinkUrl;
            return this;
        }

        public HobsonHub build() {
            return hub;
        }
    }
}
