package com.sess.core.dto.adapters.exceptions;

import com.sess.core.exceptions.CoreAppException;

public class ConvertToDTOException extends CoreAppException {

    public ConvertToDTOException() {
    }

    public ConvertToDTOException(String message) {
        super(message);
    }

    public ConvertToDTOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertToDTOException(Throwable cause) {
        super(cause);
    }

    public ConvertToDTOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
