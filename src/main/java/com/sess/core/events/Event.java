package com.sess.core.events;

import com.sess.core.users.User;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * sport event of user
 */
public abstract class Event {

    private Long id;

    private Group group;

    private User user;

    private String name;

    private LocalDateTime startDTime;

    private LocalDateTime endDTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDTime() {
        return startDTime;
    }

    public void setStartDTime(LocalDateTime startDTime) {
        this.startDTime = startDTime;
    }

    public LocalDateTime getEndDTime() {
        return endDTime;
    }

    public void setEndDTime(LocalDateTime endDTime) {
        this.endDTime = endDTime;
    }

    public abstract Map<String, Object> getOwnDetails();
}
