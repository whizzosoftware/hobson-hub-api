/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

/**
 * A class that represents the current status of a Hobson plugin.
 *
 * @author Dan Noguerol
 */
public class PluginStatus {
    private Status status;
    private String message;

    /**
     * Constructor.
     *
     * @param status the plugin status
     */
    public PluginStatus(Status status) {
        this(status, null);
    }

    /**
     * Constructor.
     *
     * @param status the plugin status
     * @param message a message associated with the plugin status
     */
    public PluginStatus(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor.
     *
     * @param status the plugin status as a String
     * @param message a message associated with the plugin status
     */
    public PluginStatus(String status, String message) {
        this.status = Status.valueOf(status.toUpperCase());
        this.message = message;
    }

    /**
     * Returns a status enumeration.
     *
     * @return a Status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns a human-readable message regarding the plugin status (for example, the cause of a failure)
     *
     * @return a String
     */
    public String getMessage() {
        return message;
    }

    public String toString() {
        return this.status.name();
    }

    public static PluginStatus notInstalled() {
        return new PluginStatus(Status.NOT_INSTALLED);
    }

    public static PluginStatus notInstalled(String message) {
        return new PluginStatus(Status.NOT_INSTALLED, message);
    }

    public static PluginStatus stopped() {
        return new PluginStatus(Status.STOPPED);
    }

    public static PluginStatus stopped(String message) {
        return new PluginStatus(Status.STOPPED, message);
    }

    public static PluginStatus initializing() {
        return new PluginStatus(Status.INITIALIZING);
    }

    public static PluginStatus notConfigured(String message) {
        return new PluginStatus(Status.NOT_CONFIGURED, message);
    }

    public static PluginStatus failed(String message) {
        return new PluginStatus(Status.FAILED, message);
    }

    public static PluginStatus running() {
        return new PluginStatus(Status.RUNNING);
    }

    public static PluginStatus configChange() {
        return new PluginStatus(Status.CONFIG_CHANGE);
    }

    public static PluginStatus stopping() {
        return new PluginStatus(Status.STOPPING);
    }

    public enum Status {
        /**
         * The plugin is not installed.
         */
        NOT_INSTALLED,
        /**
         * The plugin is installed but not running.
         */
        STOPPED,
        /**
         * The plugin is initializing.
         */
        INITIALIZING,
        /**
         * The plugin has not been configured.
         */
        NOT_CONFIGURED,
        /**
         * The plugin failed to start.
         */
        FAILED,
        /**
         * The plugin is successfully running.
         */
        RUNNING,
        /**
         * The plugin has a configuration change pending.
         */
        CONFIG_CHANGE,
        /**
         * The plugin is in the process of stopping.
         */
        STOPPING
    }
}
