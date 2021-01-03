package com.sess.core.components.validator;

import com.sess.core.exceptions.ValidationException;

import java.util.Set;

public interface Validator {

    Set<String> validate(Object object);

    void throwExceptionIfHasErrors(Object object) throws ValidationException;

}
