package com.sess.core.components.validator.javax.converters;

import javax.validation.ConstraintViolation;
import java.util.function.Function;

public interface ConstraintConverter extends Function<ConstraintViolation<?>, String> {
}
