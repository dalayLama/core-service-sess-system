package com.sess.core.events.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.SaveException;

public class NotChangeableEventException extends SaveException {
    public NotChangeableEventException(ErrorMessage errMsg) {
        super(ErrorBuilder.singletonMessage(false, errMsg));
    }
}
