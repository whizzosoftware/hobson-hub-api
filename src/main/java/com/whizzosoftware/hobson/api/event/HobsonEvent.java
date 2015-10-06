package com.whizzosoftware.hobson.api.event;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract base class for Hobson-specific events.
 *
 * @author Dan Noguerol
 */
abstract public class HobsonEvent {
    /**
     * The event timestamp property.
     */
    public static final String PROP_TIMESTAMP = "timestamp";
    /**
     * The event ID property.
     */
    public static final String PROP_EVENT_ID = "eventId";

    private String topic;
    private Map<String,Object> properties;

    public HobsonEvent(Long timestamp, String topic, String eventId) {
        this.topic = topic;
        setProperty(PROP_TIMESTAMP, timestamp);
        setProperty(PROP_EVENT_ID, eventId);
    }

    public HobsonEvent(String topic, Map<String,Object> properties) {
        this.topic = topic;
        this.properties = properties;
    }

    public String getTopic() {
        return topic;
    }

    public Long getTimestamp() {
        return (Long)getProperty(PROP_TIMESTAMP);
    }

    public String getEventId() {
        return (String)getProperty(PROP_EVENT_ID);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String toString() {
        return getEventId();
    }

    protected Object getProperty(String key) {
        if (properties != null) {
            return properties.get(key);
        } else {
            return null;
        }
    }

    protected void setProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    /**
     * Returns the event ID of a property map.
     *
     * @param properties the property map
     *
     * @return a String or null if the event has no ID
     */
    public static String readEventId(Map<String,Object> properties) {
        return (String)properties.get(PROP_EVENT_ID);
    }
}
