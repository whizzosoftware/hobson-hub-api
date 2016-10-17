package com.whizzosoftware.hobson.api.variable;

public class GlobalVariable {
    GlobalVariableDescriptor desc;
    Long lastUpdate;
    Object value;

    public GlobalVariable(GlobalVariableDescriptor desc, Long lastUpdate, Object value) {
        this.desc = desc;
        this.lastUpdate = lastUpdate;
        this.value = value;
    }

    public GlobalVariableDescriptor getDescription() {
        return desc;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public Object getValue() {
        return value;
    }
}
