package com.sess.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupDTO {

    private final Long id;

    private final DTOCity city;

    private final DTOUser creator;

    private final String title;

    private final String description;

    @JsonCreator
    public GroupDTO(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "city", required = true) DTOCity city,
            @JsonProperty(value = "creator", required = true) DTOUser creator,
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "description") String description) {
        this.id = id;
        this.city = city;
        this.creator = creator;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public DTOCity getCity() {
        return city;
    }

    public DTOUser getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
