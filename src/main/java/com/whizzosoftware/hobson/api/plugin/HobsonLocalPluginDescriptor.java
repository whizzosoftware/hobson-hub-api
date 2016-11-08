package com.whizzosoftware.hobson.api.plugin;

import com.whizzosoftware.hobson.api.action.ActionClass;
import com.whizzosoftware.hobson.api.property.PropertyContainerClass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HobsonLocalPluginDescriptor extends HobsonPluginDescriptor {
    private Map<String,ActionClass> actionClasses;
    private PropertyContainerClass configurationClass;
    private PluginContext context;

    public HobsonLocalPluginDescriptor(PluginContext context, PluginType type, String name, String description, String version, PluginStatus status) {
        super(context.getPluginId(), type, name, description, version, status);
        this.context = context;
    }

    public boolean hasActionClasses() {
        return (actionClasses != null && actionClasses.size() > 0);
    }

    public ActionClass getActionClass(String id) {
        return actionClasses.get(id);
    }

    public Collection<ActionClass> getActionClasses() {
        return actionClasses.values();
    }

    public PropertyContainerClass getConfigurationClass() {
        return configurationClass;
    }

    public PluginContext getContext() {
        return context;
    }

    @Override
    public boolean isConfigurable() {
        return (configurationClass != null && configurationClass.hasSupportedProperties());
    }

    public void setActionClasses(Collection<ActionClass> actionClasses) {
        if (actionClasses != null) {
            this.actionClasses = new HashMap<>();
            for (ActionClass ac : actionClasses) {
                this.actionClasses.put(ac.getContext().getContainerClassId(), ac);
            }
        }
    }

    public void setConfigurationClass(PropertyContainerClass configClass) {
        this.configurationClass = configClass;
    }
}
