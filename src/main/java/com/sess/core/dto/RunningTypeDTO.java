package com.sess.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunningTypeDTO {

    private final Long id;

    private final GroupDTO group;

    private final String caption;

    @JsonCreator
    public RunningTypeDTO(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "caption") String caption) {
        this.id = id;
        this.caption = caption;
        this.group = null;
    }

    public RunningTypeDTO(
            Long id,
            GroupDTO group,
            String caption) {
        this.id = id;
        this.group = group;
        this.caption = caption;
    }

    public Long getId() {
        return id;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public String getCaption() {
        return caption;
    }
}
