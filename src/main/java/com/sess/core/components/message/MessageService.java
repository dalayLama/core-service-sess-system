package com.sess.core.components.message;

import com.sess.core.exceptions.ErrorMessage;

/**
 * The service which helping to provide messages which depend on the locale
 */
public interface MessageService {

    String say(MessageId messageId);

    ErrorMessage sayError(MessageId messageId);

    ErrorMessage sayError(MessageId messageId, Object...params);

    String say(Locale locale, MessageId messageId);

    ErrorMessage sayError(Locale locale, MessageId messageId);

}
