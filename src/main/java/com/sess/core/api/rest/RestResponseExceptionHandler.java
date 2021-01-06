package com.sess.core.api.rest;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.exceptions.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = {HttpStatusOperationException.class})
    protected ResponseEntity<Error> handleHttpOperationException(HttpStatusOperationException exception) {
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }

}
