/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

/**
 * A class that comprises information about a plugin.
 *
 * @author Dan Noguerol
 */
public class PluginDescriptor implements Comparable<PluginDescriptor> {
    private String id;
    private String name;
    private String description;
    private PluginStatus status;
    private PluginType type;
    private String currentVersionString;
    private String latestVersionString;
    private boolean configurable;

    public PluginDescriptor(String id, String name, String description, PluginType type, PluginStatus status, String currentVersionString) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.currentVersionString = currentVersionString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasDescription() {
        return (description != null);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PluginStatus getStatus() {
        return status;
    }

    public void setStatus(PluginStatus state) {
        this.status = state;
    }

    public PluginType getType() {
        return type;
    }

    public void setType(PluginType type) {
        this.type = type;
    }

    public boolean isConfigurable() {
        return configurable;
    }

    public void setConfigurable(boolean configurable) {
        this.configurable = configurable;
    }

    public boolean hasCurrentVersion() {
        return currentVersionString != null;
    }

    public String getCurrentVersionString() {
        return currentVersionString;
    }

    public boolean hasLaterVersion() {
        return latestVersionString != null;
    }

    public String getLatestVersionString() {
        return latestVersionString;
    }

    public void setLatestVersionString(String latestVersionString) {
        this.latestVersionString = latestVersionString;
    }

    public boolean isUpdate() {
        return hasCurrentVersion() && hasLaterVersion();
    }

    public String toString() {
        String s =  name + " (" + id + ")";
        if (hasCurrentVersion()) {
            s += "[c=" + currentVersionString + "]";
        }
        if (hasLaterVersion()) {
            s += "[l=" + latestVersionString + "]";
        }
        return s;
    }

    @Override
    public int compareTo(PluginDescriptor o) {
        String theirName = null;
        if (o != null) {
            theirName = o.getName();
        }

        if (getName() == null && theirName != null) {
            return 1;
        } else if (getName() == null) {
            return 0;
        } else if (theirName == null) {
            return -1;
        } else {
            return getName().compareTo(theirName);
        }
    }
}
