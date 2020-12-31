package com.sess.core.events;

import java.util.Map;

public class EventRun extends Event {

    private Event event;

    private Float destination;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Float getDestination() {
        return destination;
    }

    public void setDestination(Float destination) {
        this.destination = destination;
    }

    @Override
    public Map<String, Object> getOwnDetails() {
        return Map.of(
                "destination", destination
        );
    }
}
