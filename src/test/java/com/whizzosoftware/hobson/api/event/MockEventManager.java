package com.whizzosoftware.hobson.api.event;

import com.whizzosoftware.hobson.api.hub.HubContext;

import java.util.ArrayList;
import java.util.List;

public class MockEventManager implements EventManager {
    private List<HobsonEvent> events = new ArrayList<>();

    @Override
    public void addListener(HubContext ctx, Object listener) {

    }

    @Override
    public void addListener(HubContext ctx, Object listener, EventCallbackInvoker runnable) {

    }

    @Override
    public void removeListener(HubContext ctx, Object listener) {

    }
//
//    @Override
//    public void removeListenerFromAllTopics(HubContext ctx, EventListener listener) {
//
//    }

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
