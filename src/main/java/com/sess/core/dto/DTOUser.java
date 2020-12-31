package com.sess.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sess.core.users.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class DTOUser {

    private final Long id;

    private final String nickname;

    private final String email;

    private final DTOCity city;

    private final Sex sex;

    private final LocalDateTime birthday;

    private final UUID securityKey;

    @JsonCreator
    public DTOUser(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "nickname") String nickname,
            @JsonProperty(value = "email") String email,
            @JsonProperty(value = "city") DTOCity city, Sex sex,
            @JsonProperty(value = "birthday") LocalDateTime birthday,
            @JsonProperty(value = "securityKey") UUID securityKey) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.city = city;
        this.sex = sex;
        this.birthday = birthday;
        this.securityKey = securityKey;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public DTOCity getCity() {
        return city;
    }

    public Sex getSex() {
        return sex;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public UUID getSecurityKey() {
        return securityKey;
    }
}
