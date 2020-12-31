package com.sess.core.events.exceptions;

import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.OperationAppException;

public class UserNotFoundException extends OperationAppException {
    public UserNotFoundException(Error error) {
        super(error);
    }
}
