package com.whizzosoftware.hobson.api.plugin;

public class HobsonPluginDescriptor implements Comparable<HobsonPluginDescriptor> {
    private String description;
    private String id;
    private String name;
    private PluginStatus status;
    private PluginType type;
    private String version;
    private String imageUrl;

    public HobsonPluginDescriptor(String id, PluginType type, String name, String description, String version, PluginStatus status) {
        this(id, type, name, description, version, status, null);
    }

    public HobsonPluginDescriptor(String id, PluginType type, String name, String description, String version, PluginStatus status, String imageUrl) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.version = version;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    @Override
    public int compareTo(HobsonPluginDescriptor o) {
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

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PluginStatus getStatus() {
        return status;
    }

    public PluginType getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public boolean hasVersion() {
        return (version != null);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isConfigurable() {
        return false;
    }

    @Override
    public String toString() {
        String s =  name + " (" + id + ")";
        if (hasVersion()) {
            s += "[c=" + getVersion() + "]";
        }
        return s;
    }
}
