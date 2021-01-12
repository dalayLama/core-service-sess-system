package com.sess.core.events.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.OperationAppException;

public class EventNotFound extends OperationAppException {

    public EventNotFound(ErrorMessage errMsg) {
        super(ErrorBuilder.singletonMessage(false, errMsg));
    }

}
