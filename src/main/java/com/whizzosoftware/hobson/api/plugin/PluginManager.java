package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.config.Configuration;

import java.io.File;
import java.util.Map;

public interface PluginManager {
    /**
     * Returns a data file for a specific plugin.
     *
     * @param pluginId the plugin ID requesting the file
     * @param filename the name of the data file
     *
     * @return a File instance (or null if not found)
     */
    public File getDataFile(String pluginId, String filename);

    /**
     * Retrieves a specific plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return a HobsonPlugin instance (or null if not found)
     */
    public HobsonPlugin getPlugin(String userId, String hubId, String pluginId);

    /**
     * Retrieve a list of all plugins.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param includeRemoteInfo indicates whether online Hobson plugin directory information should be included
     *
     * @return a PluginList
     */
    public PluginList getPlugins(String userId, String hubId, boolean includeRemoteInfo);

    /**
     * Returns the plugin level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param plugin the plugin
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getPluginConfiguration(String userId, String hubId, HobsonPlugin plugin);

    /**
     * Returns the plugin level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return a Dictionary (or null if there is no configuration)
     */
    public Configuration getPluginConfiguration(String userId, String hubId, String pluginId);

    /**
     * Returns the current local version of a plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     *
     * @return the current version in x.x.x format
     */
    public String getPluginCurrentVersion(String userId, String hubId, String pluginId);

    /**
     * Installs a specific version of a plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID to install
     * @param pluginVersion the plugin version to install
     */
    public void installPlugin(String userId, String hubId, String pluginId, String pluginVersion);

    /**
     * Sets the plugin level configuration.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     * @param config the plugin configuration
     */
    public void setPluginConfiguration(String userId, String hubId, String pluginId, Configuration config);

    /**
     * Sets an individual plugin level configuration property.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID
     * @param name the configuration property name
     * @param value the configuration property value
     */
    public void setPluginConfigurationProperty(String userId, String hubId, String pluginId, String name, Object value);

    /**
     * Allows a listener to receive a callback when a plugin's configuration changes.
     *
     * @param pluginId the plugin ID
     * @param listener the listener object to be called
     */
    public void registerForPluginConfigurationUpdates(String pluginId, PluginConfigurationListener listener);

    /**
     * Reloads the specified plugin.
     *
     * @param userId the user ID that owns the hub
     * @param hubId the hub ID
     * @param pluginId the plugin ID to reload
     */
    public void reloadPlugin(String userId, String hubId, String pluginId);

    /**
     * Allows a plugin to cease receiving callbacks when its configuration changes.
     *
     * @param pluginId the plugin ID requesting the un-registration
     * @param listener the plugin that previously requested the callback
     */
    public void unregisterForPluginConfigurationUpdates(String pluginId, PluginConfigurationListener listener);
}
