package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.variable.GlobalVariableUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalVariableUpdateEvent extends HobsonEvent {
    public static final String ID = "varUpdateNotify";
    public static final String PROP_UPDATES = "updates";

    public GlobalVariableUpdateEvent(long timestamp, GlobalVariableUpdate update) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        List<GlobalVariableUpdate> updates = new ArrayList<>();
        updates.add(update);
        setProperty(PROP_UPDATES, updates);
    }

    public GlobalVariableUpdateEvent(long timestamp, List<GlobalVariableUpdate> updates) {
        super(timestamp, EventTopics.STATE_TOPIC, ID);

        setProperty(PROP_UPDATES, updates);
    }

    public GlobalVariableUpdateEvent(Map<String,Object> properties) {
        super(EventTopics.STATE_TOPIC, properties);
    }

    public List<GlobalVariableUpdate> getUpdates() {
        return (List<GlobalVariableUpdate>)getProperty(PROP_UPDATES);
    }
}
