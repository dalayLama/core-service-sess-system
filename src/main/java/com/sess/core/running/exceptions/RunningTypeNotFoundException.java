package com.sess.core.running.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.SaveException;

public class RunningTypeNotFoundException extends SaveException {
    public RunningTypeNotFoundException(ErrorMessage errMsg) {
        super(ErrorBuilder.singletonMessage(false, errMsg));
    }
}
