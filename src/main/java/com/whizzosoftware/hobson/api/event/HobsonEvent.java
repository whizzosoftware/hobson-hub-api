package com.whizzosoftware.hobson.api.event;

import org.osgi.service.event.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract base class for Hobson-specific events.
 *
 * @author Dan Noguerol
 */
abstract public class HobsonEvent {
    /**
     * The event name property.
     */
    public static final String PROP_EVENT_ID = "eventId";

    private String topic;
    private String eventId;

    public HobsonEvent(String topic, String eventId) {
        this.topic = topic;
        this.eventId = eventId;
    }

    public HobsonEvent(Event event) {
        eventId = (String)event.getProperty(PROP_EVENT_ID);
        readProperties(event);
    }

    public String getEventId() {
        return eventId;
    }

    /**
     * Returns an OSGi Event object.
     *
     * @return an Event
     */
    public Event getEvent() {
        Map map = new HashMap();
        writeProperties(map);
        map.put(PROP_EVENT_ID, getEventId());
        return new Event(topic, map);
    }

    public String toString() {
        return eventId;
    }

    /**
     * Returns the event ID of an OSGi Event object.
     *
     * @param event the event object
     *
     * @return a String or null if the event has no ID
     */
    static public String readEventId(Event event) {
        return (String)event.getProperty(PROP_EVENT_ID);
    }

    abstract void readProperties(Event event);
    abstract void writeProperties(Map properties);
}
