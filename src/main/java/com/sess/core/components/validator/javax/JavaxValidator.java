package com.sess.core.components.validator.javax;

import com.sess.core.components.validator.Validator;
import com.sess.core.components.validator.javax.converters.ConstraintConverter;
import com.sess.core.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JavaxValidator implements Validator {

    private final javax.validation.Validator validator;

    private final ConstraintConverter constraintConverter;

    public JavaxValidator(javax.validation.Validator validator, ConstraintConverter constraintConverter) {
        this.validator = validator;
        this.constraintConverter = constraintConverter;
    }

    @Override
    public Set<String> validate(Object object) {
        return toSetMessages(validator.validate(object));
    }

    @Override
    public void throwExceptionIfHasErrors(Object object) throws ValidationException {
        Set<String> result = validate(object);
        if (!result.isEmpty()) {
            throw new ValidationException(result);
        }
    }

    private Set<String> toSetMessages(Set<? extends ConstraintViolation<?>> constraints) {
        return constraints.isEmpty() ? Collections.emptySet() :
                constraints.stream().map(constraintConverter).collect(Collectors.toSet());
    }

}
