package com.sess.core.exceptions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ErrorBuilder {

    private boolean resultOperation;

    private final Set<ErrorMessage> messages = new HashSet<>();

    public static Error singletonMessage(boolean resultOperation, String code, String message) {
        return singletonMessage(false, new ErrorMessage(code, message));
    }

    public static Error singletonMessage(boolean resultOperation, ErrorMessage errorMessage) {
        return new ErrorBuilder(resultOperation)
                .addMessage(errorMessage)
                .build();
    }

    public static Error listMessages(boolean resultOperation, String code, Collection<String> messages) {
        ErrorBuilder builder = new ErrorBuilder(resultOperation);
        messages.forEach(m -> builder.addMessage(code, m));
        return builder.build();
    }

    public static Error listMessages(boolean resultOperation, Collection<? extends ErrorMessage> messages) {
        ErrorBuilder builder = new ErrorBuilder(resultOperation);
        messages.forEach(builder::addMessage);
        return builder.build();
    }

    public static ErrorBuilder newBuilder(boolean resultOperation) {
        return new ErrorBuilder(resultOperation);
    }

    private ErrorBuilder() {
    }

    private ErrorBuilder(boolean resultOperation) {
        this.resultOperation = resultOperation;
    }

    public ErrorBuilder setResultOperation(boolean value) {
        this.resultOperation = value;
        return this;
    }

    public ErrorBuilder addMessage(String code, String message) {
        return addMessage(new ErrorMessage(code, message));
    }

    public ErrorBuilder addMessage(ErrorMessage message) {
        this.messages.add(message);
        return this;
    }

    public Error build() {
        return new Error(resultOperation, messages);
    }

}
