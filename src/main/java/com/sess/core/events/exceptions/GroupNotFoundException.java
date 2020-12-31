package com.sess.core.events.exceptions;

import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.OperationAppException;

public class GroupNotFoundException extends OperationAppException {
    public GroupNotFoundException(Error error) {
        super(error);
    }
}
