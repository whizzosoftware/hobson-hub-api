package com.whizzosoftware.hobson.api.variable;

public class GlobalVariable {
    GlobalVariableDescription desc;
    Long lastUpdate;
    Object value;

    public GlobalVariable(GlobalVariableDescription desc, Long lastUpdate, Object value) {
        this.desc = desc;
        this.lastUpdate = lastUpdate;
        this.value = value;
    }

    public GlobalVariableDescription getDescription() {
        return desc;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public Object getValue() {
        return value;
    }
}
