package com.sess.core.exceptions;

public class UpdateException extends SaveException {

    public UpdateException(Error error) {
        super(error);
    }

    public UpdateException(String message, Error error) {
        super(message, error);
    }

    public UpdateException(String message, Throwable cause, Error error) {
        super(message, cause, error);
    }

    public UpdateException(Throwable cause, Error error) {
        super(cause, error);
    }
}
