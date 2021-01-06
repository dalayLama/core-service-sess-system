package com.sess.core.groups.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.SaveException;

public class UserAlreadyExistInGroup extends SaveException {
    public UserAlreadyExistInGroup(ErrorMessage errorMessage) {
        super(ErrorBuilder.singletonMessage(false, errorMessage));
    }
}
