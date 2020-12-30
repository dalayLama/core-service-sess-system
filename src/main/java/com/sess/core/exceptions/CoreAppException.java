package com.sess.core.exceptions;

public class CoreAppException extends RuntimeException {

    public CoreAppException() {
    }

    public CoreAppException(String message) {
        super(message);
    }

    public CoreAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreAppException(Throwable cause) {
        super(cause);
    }

    public CoreAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
