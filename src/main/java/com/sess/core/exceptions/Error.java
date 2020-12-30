package com.sess.core.exceptions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Error {

    private final boolean resultOperation;

    private final Set<ErrorMessage> messages = new HashSet<>();

    public Error(boolean resultOperation, Collection<ErrorMessage> messages) {
        this.resultOperation = resultOperation;
        this.messages.addAll(messages);
    }

    public boolean isResultOperation() {
        return resultOperation;
    }

    public Collection<? extends ErrorMessage> getMessages() {
        return messages;
    }
}
