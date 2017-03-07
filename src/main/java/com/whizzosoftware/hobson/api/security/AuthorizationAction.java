/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.api.security;

/**
 * A set of defined actions that a user is potentially allowed to perform against one or more resources.
 *
 * @author Dan Noguerol
 */
public class AuthorizationAction {
    public static final String DATASTREAM_CREATE = "dataStream:create";
    public static final String DATASTREAM_DELETE = "dataStream:delete";
    public static final String DATASTREAM_READ = "dataStream:read";
    public static final String DATASTREAM_UPDATE = "dataStream:update";
    public static final String DEVICE_CONFIGURE = "device:configure";
    public static final String DEVICE_CREATE = "device:create";
    public static final String DEVICE_DELETE = "device:delete";
    public static final String DEVICE_EXECUTE = "device:execute";
    public static final String DEVICE_READ = "device:read";
    public static final String DEVICE_UPDATE = "device:update";
    public static final String HUB_CONFIGURE = "hub:configure";
    public static final String HUB_EXECUTE = "hub:execute";
    public static final String HUB_READ = "hub:read";
    public static final String JOB_READ = "job:read";
    public static final String JOB_DELETE = "job:delete";
    public static final String PLUGIN_CONFIGURE = "plugin:configure";
    public static final String PLUGIN_DELETE = "plugin:delete";
    public static final String PLUGIN_EXECUTE = "plugin:execute";
    public static final String PLUGIN_INSTALL = "plugin:install";
    public static final String PLUGIN_READ = "plugin:read";
    public static final String PRESENCE_READ = "presence:read";
    public static final String PRESENCE_UPDATE = "presence:update";
    public static final String PRESENCE_DELETE = "presence:delete";
    public static final String TASK_CREATE = "task:create";
    public static final String TASK_DELETE = "task:delete";
    public static final String TASK_READ = "task:read";
    public static final String TASK_UPDATE = "task:update";
    public static final String USER_CREATE = "user:create";
    public static final String USER_READ = "user:read";
}
