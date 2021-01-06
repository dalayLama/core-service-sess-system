package com.sess.core.users.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.OperationAppException;

public class UserNotFoundException extends OperationAppException {
    public UserNotFoundException(ErrorMessage error) {
        super(ErrorBuilder.singletonMessage(false, error));
    }
}
