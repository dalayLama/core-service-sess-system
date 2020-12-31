package com.sess.core.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class DTOOnlyDataEvent {

    private final long id;

    private final String name;

    private final LocalDateTime startDTime;

    private final LocalDateTime endDTime;

    private final Map<String, Object> details;

    public DTOOnlyDataEvent(long id, String name, LocalDateTime startDTime, LocalDateTime endDTime, Map<String, Object> details) {
        this.id = id;
        this.name = name;
        this.startDTime = startDTime;
        this.endDTime = endDTime;
        this.details = details;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartDTime() {
        return startDTime;
    }

    public LocalDateTime getEndDTime() {
        return endDTime;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
