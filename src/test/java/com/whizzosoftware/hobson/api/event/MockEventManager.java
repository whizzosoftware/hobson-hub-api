package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockEventManager implements EventManager {
    private List<HobsonEvent> events = new ArrayList<>();

    @Override
    public void addListener(HubContext ctx, EventListener listener, String[] topics) {

    }

    @Override
    public void removeListener(HubContext ctx, EventListener listener, String[] topics) {

    }

    @Override
    public void removeListenerFromAllTopics(HubContext ctx, EventListener listener) {

    }

    @Override
    public void postEvent(HubContext ctx, HobsonEvent event) {
        events.add(event);
    }

    public List<HobsonEvent> getPostedEvents() {
        return events;
    }

    public void clearPostedEvents() {
        events.clear();
    }
}
