package com.sess.core.users.registration.exceptions;

import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;

public class NotNullableUserId extends RegistrationException {

    public NotNullableUserId(ErrorMessage errorMessage) {
        super(ErrorBuilder.singletonMessage(false, errorMessage));
    }

}
