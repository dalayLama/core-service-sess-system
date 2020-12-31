package com.sess.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DTOCity {

    private final long id;

    private final String address;

    @JsonCreator
    public DTOCity(
            @JsonProperty(value = "id") long id,
            @JsonProperty(value = "address") String address) {
        this.id = id;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
