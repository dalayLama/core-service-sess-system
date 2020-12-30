package com.sess.core.exceptions;

import java.util.Objects;

public class ErrorMessage {

    private final String code;

    private final String message;

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorMessage)) return false;
        ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage());
    }
}
