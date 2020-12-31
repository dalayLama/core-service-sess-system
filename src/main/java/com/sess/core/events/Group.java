package com.sess.core.events;

import com.sess.core.users.City;

/**
 * Group where occur events
 */
public class Group {

    private Long id;

    private String title;

    private City city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
