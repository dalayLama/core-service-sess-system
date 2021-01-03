package com.sess.core.exceptions;

public class DeleteException extends OperationAppException {

    public DeleteException(Error error) {
        super(error);
    }

    public DeleteException(String message, Error error) {
        super(message, error);
    }

    public DeleteException(String message, Throwable cause, Error error) {
        super(message, cause, error);
    }

    public DeleteException(Throwable cause, Error error) {
        super(cause, error);
    }
}
