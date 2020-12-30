package com.sess.core.users;

public enum Sex {

    MALE("Мужчина", "М"), FEMALE("Женщина", "Ж");

    private final String caption;

    private final String shortCaption;

    Sex(String caption, String shortCaption) {
        this.caption = caption;
        this.shortCaption = shortCaption;
    }

    public String getCaption() {
        return caption;
    }

    public String getShortCaption() {
        return shortCaption;
    }
}
