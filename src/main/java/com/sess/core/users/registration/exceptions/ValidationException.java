package com.sess.core.users.registration.exceptions;

import com.sess.core.ErrorsCodes;
import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.ErrorMessage;

import java.util.Collection;
import java.util.stream.Collectors;

public class ValidationException extends RegistrationException {

    public ValidationException(Collection<String> messages) {
        super(new Error(
                false,
                messages.stream()
                        .map(m -> new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, m))
                        .collect(Collectors.toSet())
        ));
    }

}
