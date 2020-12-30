package com.sess.core.components.message;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private final boolean isError;

    private final String errorCode;

    private final Map<Locale, String> localeTextMap = new HashMap<>();

    public Message(Map<Locale, String> localeTextMap) {
        this.isError = false;
        this.errorCode = null;
        this.localeTextMap.putAll(localeTextMap);
    }

    public Message(String errorCode, Map<Locale, String> localeTextMap) {
        this.isError = true;
        this.errorCode = errorCode;
        this.localeTextMap.putAll(localeTextMap);
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getText(Locale locale) {
        return localeTextMap.get(locale);
    }

}
