package com.sess.core.exceptions;

public class OperationAppException extends CoreAppException {

    private final Error error;

    public OperationAppException(Error error) {
        this.error = error;
    }

    public OperationAppException(String message, Error error) {
        super(message);
        this.error = error;
    }

    public OperationAppException(String message, Throwable cause, Error error) {
        super(message, cause);
        this.error = error;
    }

    public OperationAppException(Throwable cause, Error error) {
        super(cause);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
