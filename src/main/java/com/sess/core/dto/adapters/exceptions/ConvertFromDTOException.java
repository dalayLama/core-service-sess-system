package com.sess.core.dto.adapters.exceptions;

import com.sess.core.exceptions.CoreAppException;

public class ConvertFromDTOException extends CoreAppException {

    public ConvertFromDTOException() {
    }

    public ConvertFromDTOException(String message) {
        super(message);
    }

    public ConvertFromDTOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertFromDTOException(Throwable cause) {
        super(cause);
    }

    public ConvertFromDTOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
