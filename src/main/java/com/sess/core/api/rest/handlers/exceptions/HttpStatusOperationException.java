package com.sess.core.api.rest.handlers.exceptions;

import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.OperationAppException;
import org.springframework.http.HttpStatus;

public class HttpStatusOperationException extends OperationAppException {

    private final HttpStatus status;

    public HttpStatusOperationException(Error error, HttpStatus status) {
        super(error);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
