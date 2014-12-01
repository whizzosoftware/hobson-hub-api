package com.whizzosoftware.hobson.api.plugin;

public enum PluginType {
    /**
     * Framework plugins are part of the OSGi runtime.
     */
    FRAMEWORK,
    /**
     * Core plugins are Hobson plugins that, while technically optional, wouldn't make sense to omit.
     */
    CORE,
    /**
     * Everything else is an optional plugin.
     */
    PLUGIN
}
