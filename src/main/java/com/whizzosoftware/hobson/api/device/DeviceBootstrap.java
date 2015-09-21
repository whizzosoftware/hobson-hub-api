/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

/**
 * Represents the bootstrap information an external device needs to communicate with the Hobson Hub.
 *
 * @author Dan Noguerol
 */
public class DeviceBootstrap {
    private String id;
    private String deviceId;
    private long creationTime;
    private Long bootstrapTime;
    private String secret;

    public DeviceBootstrap(String id, String deviceId, long creationTime) {
        this(id, deviceId, creationTime, null);
    }

    public DeviceBootstrap(String id, String deviceId, long creationTime, Long bootstrapTime) {
        this.id = id;
        this.deviceId = deviceId;
        this.creationTime = creationTime;
        this.bootstrapTime = bootstrapTime;
    }

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean hasBootstrapTime() {
        return (bootstrapTime != null);
    }

    public Long getBootstrapTime() {
        return bootstrapTime;
    }

    public void setBootstrapTime(Long bootstrapTime) {
        this.bootstrapTime = bootstrapTime;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
