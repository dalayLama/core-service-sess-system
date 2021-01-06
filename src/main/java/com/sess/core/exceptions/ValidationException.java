package com.sess.core.exceptions;

import com.sess.core.ErrorsCodes;

import java.util.Collection;
import java.util.stream.Collectors;

public class ValidationException extends SaveException {

    public ValidationException(Collection<String> messages) {
        super(new Error(
                false,
                messages.stream()
                        .map(m -> new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, m))
                        .collect(Collectors.toSet())
        ));
    }

}
