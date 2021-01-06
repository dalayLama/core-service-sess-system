package com.sess.core.groups.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.OperationAppException;

public class GroupNotFoundException extends OperationAppException {
    public GroupNotFoundException(ErrorMessage errorMessage) {
        super(ErrorBuilder.singletonMessage(false, errorMessage));
    }
}
