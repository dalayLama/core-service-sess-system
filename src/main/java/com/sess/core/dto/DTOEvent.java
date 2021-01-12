package com.sess.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class DTOEvent {

    private final Long id;

    private final GroupDTO group;

    private final RunningTypeDTO runningType;

    private final String eventName;

    private final float distance;

    private final String placeStart;

    private final String placeEnd;

    private final LocalDateTime plannedDtStart;

    private final LocalDateTime plannedDtEnd;

    private final String description;

    private final LocalDateTime factualDtStart;

    private final LocalDateTime factualDtEnd;

    @JsonCreator
    public DTOEvent(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "runningType") RunningTypeDTO runningType,
            @JsonProperty(value = "eventName") String eventName,
            @JsonProperty(value = "distance") float distance,
            @JsonProperty(value = "placeStart") String placeStart,
            @JsonProperty(value = "placeEnd") String placeEnd,
            @JsonProperty(value = "plannedDtStart") LocalDateTime plannedDtStart,
            @JsonProperty(value = "plannedDtEnd") LocalDateTime plannedDtEnd,
            @JsonProperty(value = "description") String description) {
        this.id = id;
        this.runningType = runningType;
        this.eventName = eventName;
        this.distance = distance;
        this.placeStart = placeStart;
        this.placeEnd = placeEnd;
        this.plannedDtStart = plannedDtStart;
        this.plannedDtEnd = plannedDtEnd;
        this.description = description;
        this.group = null;
        this.factualDtStart = null;
        this.factualDtEnd = null;
    }

    public DTOEvent(
            Long id,
            GroupDTO group,
            RunningTypeDTO runningType,
            String eventName,
            float distance,
            String placeStart,
            String placeEnd,
            LocalDateTime plannedDtStart,
            LocalDateTime plannedDtEnd,
            String description,
            LocalDateTime factualDtStart,
            LocalDateTime factualDtEnd) {
        this.id = id;
        this.group = group;
        this.runningType = runningType;
        this.eventName = eventName;
        this.distance = distance;
        this.placeStart = placeStart;
        this.placeEnd = placeEnd;
        this.plannedDtStart = plannedDtStart;
        this.plannedDtEnd = plannedDtEnd;
        this.description = description;
        this.factualDtStart = factualDtStart;
        this.factualDtEnd = factualDtEnd;
    }

    public Long getId() {
        return id;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public RunningTypeDTO getRunningType() {
        return runningType;
    }

    public String getEventName() {
        return eventName;
    }

    public float getDistance() {
        return distance;
    }

    public String getPlaceStart() {
        return placeStart;
    }

    public String getPlaceEnd() {
        return placeEnd;
    }

    public LocalDateTime getPlannedDtStart() {
        return plannedDtStart;
    }

    public LocalDateTime getPlannedDtEnd() {
        return plannedDtEnd;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getFactualDtStart() {
        return factualDtStart;
    }

    public LocalDateTime getFactualDtEnd() {
        return factualDtEnd;
    }
}
