package com.sess.core.exceptions;

public class NotNullableId extends SaveException {

    public NotNullableId(ErrorMessage errorMessage) {
        super(ErrorBuilder.singletonMessage(false, errorMessage));
    }

}
