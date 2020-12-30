package com.sess.core.users.registration.exceptions;

import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.OperationAppException;

public class RegistrationException extends OperationAppException {

    public RegistrationException(Error error) {
        super(error);
    }

    public RegistrationException(String message, Error error) {
        super(message, error);
    }

    public RegistrationException(String message, Throwable cause, Error error) {
        super(message, cause, error);
    }

    public RegistrationException(Throwable cause, Error error) {
        super(cause, error);
    }

}
