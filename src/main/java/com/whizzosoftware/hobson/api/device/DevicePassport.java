/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.device;

/**
 * Represents information an external device needs to communicate with the Hobson Hub.
 *
 * @author Dan Noguerol
 */
public class DevicePassport {
    private String id;
    private String deviceId;
    private long creationTime;
    private Long activationTime;
    private String secret;

    public DevicePassport(String id, String deviceId, long creationTime) {
        this(id, deviceId, creationTime, null);
    }

    public DevicePassport(String id, String deviceId, long creationTime, Long activationTime) {
        this.id = id;
        this.deviceId = deviceId;
        this.creationTime = creationTime;
        this.activationTime = activationTime;
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

    public boolean isActivated() {
        return (activationTime != null);
    }

    public Long getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Long activationTime) {
        this.activationTime = activationTime;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
