/*******************************************************************************
 * Copyright (c) 2014 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.config.manager.ConfigurationManager;
import com.whizzosoftware.hobson.api.device.HobsonDevice;
import com.whizzosoftware.hobson.api.device.manager.DeviceManager;
import com.whizzosoftware.hobson.api.disco.ExternalBridgeMetaAnalyzer;
import com.whizzosoftware.hobson.api.disco.manager.DiscoManager;
import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.event.VariableUpdateRequestEvent;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import com.whizzosoftware.hobson.api.variable.manager.VariableManager;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.bootstrap.api.config.ConfigurationMetaData;
import com.whizzosoftware.hobson.bootstrap.api.plugin.PluginStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

/**
 * A base class that implements several HobsonPlugin methods. This provides a good starting point for third-party
 * HobsonPlugin implementations.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractHobsonPlugin implements HobsonPlugin {
    private DeviceManager deviceManager;
    private VariableManager variableManager;
    private ConfigurationManager configManager;
    private DiscoManager discoManager;
    private String pluginId;
    private String version;
    private PluginStatus status = new PluginStatus(PluginStatus.Status.INITIALIZING);
    private final List<ConfigurationMetaData> configMeta = new ArrayList<ConfigurationMetaData>();

    public AbstractHobsonPlugin(String pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    public String getId() {
        return pluginId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public PluginStatus getStatus() {
        return status;
    }

    @Override
    public Collection<ConfigurationMetaData> getConfigurationMetaData() {
        return configMeta;
    }

    protected void addConfigurationMetaData(ConfigurationMetaData metaData) {
        configMeta.add(metaData);
    }

    @Override
    public boolean isConfigurable() {
        return (configMeta.size() > 0);
    }

    @Override
    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Override
    public void setVariableManager(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public void setConfigurationManager(ConfigurationManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setDiscoManager(DiscoManager discoManager) {
        this.discoManager = discoManager;
    }

    @Override
    public void setDeviceConfigurationProperty(String id, String name, Object value, boolean overwrite) {
        validateConfigurationManager();
        configManager.setDeviceConfigurationProperty(getId(), id, name, value, overwrite);
    }

    @Override
    public HobsonVariable getDeviceVariable(String deviceId, String variableName) {
        validateVariableManager();
        return variableManager.getDeviceVariable(getId(), deviceId, variableName);
    }

    @Override
    public void publishGlobalVariable(HobsonVariable variable) {
        validateVariableManager();
        variableManager.publishGlobalVariable(getId(), variable);
    }

    @Override
    public void publishDeviceVariable(String deviceId, HobsonVariable variable) {
        validateVariableManager();
        variableManager.publishDeviceVariable(getId(), deviceId, variable);
    }

    @Override
    public void fireVariableUpdateNotifications(List<VariableUpdate> updates) {
        validateVariableManager();
        variableManager.fireVariableUpdateNotifications(updates);
    }

    @Override
    public void fireVariableUpdateNotification(VariableUpdate variableUpdate) {
        validateVariableManager();
        variableManager.fireVariableUpdateNotification(variableUpdate);
    }

    @Override
    public void onDeviceConfigurationUpdate(String deviceId, Dictionary config) {
        getDevice(deviceId).onDeviceConfigurationUpdate(config);
    }

    @Override
    public void onHobsonEvent(HobsonEvent event) {
        if (event instanceof VariableUpdateRequestEvent) {
            VariableUpdateRequestEvent vure = (VariableUpdateRequestEvent)event;
            if (vure.getPluginId() == null || vure.getPluginId().equals(getId())) {
                onSetDeviceVariable(vure.getDeviceId(), vure.getName(), vure.getValue());
            }
        }
    }

    @Override
    public void onSetDeviceVariable(String deviceId, String variableName, Object value) {
        getDevice(deviceId).onSetVariable(variableName, value);
    }

    /**
     * Sets the plugin version string.
     *
     * @param version the version string
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Sets the plugin status.
     *
     * @param status the status
     */
    protected void setStatus(PluginStatus status) {
        this.status = status;
    }

    /**
     * Sets a plugin configuration property.
     *
     * @param pluginId the plugin ID
     * @param name the name of the property
     * @param value the value of the property
     */
    protected void setPluginConfigurationProperty(String pluginId, String name, Object value) {
        validateConfigurationManager();
        configManager.setPluginConfigurationProperty(pluginId, name, value);
    }

    /**
     * Publishes a new device and invokes its start() method.
     *
     * @param device the device to publish
     */
    protected void publishAndStartDevice(HobsonDevice device) {
        validateDeviceManager();
        deviceManager.publishAndStartDevice(device);
    }

    /**
     * Publishes a new ExternalBridgeMetaAnalyzer.
     *
     * @param analyzer the analyzer to publish
     */
    protected void publishExternalBridgeMetaAnalyzer(ExternalBridgeMetaAnalyzer analyzer) {
        validateDiscoManager();
        discoManager.publishExternalBridgeMetaAnalyzer(analyzer);
    }

    /**
     * Indicates whether a specific device ID has already been published.
     *
     * @param deviceId the device ID of interest
     *
     * @return a boolean
     */
    protected boolean hasDevice(String deviceId) {
        return deviceManager.hasDevice(getId(), deviceId);
    }

    /**
     * Retrieves a HobsonDevice with the specific device ID.
     *
     * @param deviceId the device ID
     *
     * @return a HobsonDevice instance
     * @throws com.whizzosoftware.hobson.bootstrap.api.HobsonNotFoundException if the device ID was not found
     */
    protected HobsonDevice getDevice(String deviceId) {
        validateDeviceManager();
        return deviceManager.getDevice(getId(), deviceId);
    }

    /**
     * Retrieves all HobsonDevices that have been published by this plugin.
     *
     * @return a Collection of HobsonDevice instances
     */
    protected Collection<HobsonDevice> getAllDevices() {
        return deviceManager.getAllPluginDevices(getId());
    }

    /**
     * Unpublishes a published device after invoking its stop() method.
     *
     * @param deviceId the device ID to unpublish
     */
    protected void stopAndUnpublishDevice(String deviceId) {
        validateVariableManager();
        validateDeviceManager();
        variableManager.unpublishAllDeviceVariables(getId(), deviceId);
        deviceManager.stopAndUnpublishDevice(getId(), deviceId);
    }

    /**
     * Unpublishes all published devices associates with a plugin after invoking their stop() methods.
     */
    protected void stopAndUnpublishAllDevices() {
        validateVariableManager();
        validateDeviceManager();
        variableManager.unpublishAllPluginVariables(getId());
        deviceManager.stopAndUnpublishAllDevices(getId());
    }

    private void validateDeviceManager() {
        if (deviceManager == null) {
            throw new HobsonRuntimeException("No device manager has been set");
        }
    }

    private void validateConfigurationManager() {
        if (configManager == null) {
            throw new HobsonRuntimeException("No configuration manager has been set");
        }
    }

    private void validateVariableManager() {
        if (variableManager == null) {
            throw new HobsonRuntimeException("No variable manager has been set");
        }
    }

    private void validateDiscoManager() {
        if (discoManager == null) {
            throw new HobsonRuntimeException("No disco manager has been set");
        }
    }
}
