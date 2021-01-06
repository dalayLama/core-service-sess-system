package com.sess.core.exceptions;

public class SaveException extends OperationAppException {

    public SaveException(Error error) {
        super(error);
    }

    public SaveException(String message, Error error) {
        super(message, error);
    }

    public SaveException(String message, Throwable cause, Error error) {
        super(message, cause, error);
    }

    public SaveException(Throwable cause, Error error) {
        super(cause, error);
    }

}
