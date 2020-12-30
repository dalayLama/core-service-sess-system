package com.sess.core.components.validator.javax.converters;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;

@Component
public class MessageConstraintConverter implements ConstraintConverter {

    @Override
    public String apply(ConstraintViolation<?> constraintViolation) {
        return constraintViolation.getMessage();
    }

}
