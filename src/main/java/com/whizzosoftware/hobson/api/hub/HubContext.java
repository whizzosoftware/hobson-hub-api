/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.hub;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * A class that encapsulates the fully-qualified context of a hub.
 *
 * @author Dan Noguerol
 */
public class HubContext implements Serializable {
    public static final String DEFAULT_USER = "local";
    public static final String DEFAULT_HUB = "local";

    public static final String DELIMITER = ":";

    public String hubId;

    public static HubContext create(String hubId) {
        return new HubContext(hubId);
    }

    public static HubContext createLocal() {
        return create(DEFAULT_HUB);
    }

    public HubContext() {}

    public HubContext(String hubId) {
        this.hubId = hubId;
    }

    public boolean hasHubId() {
        return (hubId != null);
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public boolean equals(Object o) {
        return (
            o instanceof HubContext &&
            ((HubContext)o).hubId.equals(hubId)
        );
    }

    public int hashCode() {
        return new HashCodeBuilder().append(hubId).toHashCode();
    }

    public String toString() {
        return hubId;
    }
}
