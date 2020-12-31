package com.sess.core.components.message;

import com.sess.core.ErrorsCodes;
import com.sess.core.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryMessageService implements MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMessageService.class);

    private final Locale defaultLocale;

    private final Map<MessageId, Message> messages = new HashMap<>();

    private final Message notFoundMessage;

    private final Message notFoundErrorMessage;

    public InMemoryMessageService(Locale defaultLocale, Message notFoundMessage, Message notFoundErrorMessage) {
        this.defaultLocale = defaultLocale;
        this.notFoundMessage = notFoundMessage;
        this.notFoundErrorMessage = notFoundErrorMessage;
    }

    public void init() {
        messages.putAll(Map.of(
                MessageId.UNIQUE_EMAIL_VIOLATION,
                new Message(ErrorsCodes.VALIDATE_ERROR,
                        Map.of(Locale.RU, "Пользователь с таким \"email\" уже существует")),
                MessageId.UNIQUE_NICKNAME_VIOLATION,
                new Message(ErrorsCodes.VALIDATE_ERROR,
                        Map.of(Locale.RU, "Пользователь с указанным \"Ник\"-ом уже существует")),
                MessageId.NOT_NULLABLE_USER_ID,
                new Message(ErrorsCodes.NOT_NULLABLE_USER_ID,
                        Map.of(Locale.RU, "У нового пользователя должен быть пустой идентификатор")),
                MessageId.FAILED_SAVE_NEW_USER,
                new Message(ErrorsCodes.FAILED_SAVE_NEW_USER,
                        Map.of(Locale.RU, "Не удалось зарегистрировать нового пользователя")),
                MessageId.READ_DTO_USER_DATA_ERROR,
                new Message(ErrorsCodes.READ_DTO_USER_DATA_ERROR,
                        Map.of(Locale.RU, "Не удалось обработать данные нового пользователя")),
                MessageId.WRITE_DTO_USER_DATA_ERROR,
                new Message(ErrorsCodes.WRITE_DTO_USER_DATA_ERROR,
                        Map.of(Locale.RU, "Не удалось сформировать ответ для пользователя"))
        ));
    }

    @Override
    public String say(MessageId messageId) {
        return getText(messageId, defaultLocale);
    }

    @Override
    public ErrorMessage sayError(MessageId messageId) {
        return getError(messageId, defaultLocale);
    }

    @Override
    public String say(Locale locale, MessageId messageId) {
        return getText(messageId, locale);
    }

    @Override
    public ErrorMessage sayError(Locale locale, MessageId messageId) {
        return getError(messageId, locale);
    }


    private String getText(MessageId messageId, Locale locale) {
        Message message = messages.get(messageId);
        if (Objects.isNull(message)) {
            LOG.warn("Message {} wasn't found", messageId);
            return notFoundMessage.getText(locale);
        }
        return message.getText(locale);
    }

    private ErrorMessage getError(MessageId messageId, Locale locale) {
        Message message = messages.get(messageId);
        if (Objects.isNull(message)) {
            LOG.warn("Message {} wasn't found", messageId);
            return generateErrorMessage(notFoundErrorMessage, locale);
        }
        return generateErrorMessage(message, locale);
    }

    private ErrorMessage generateErrorMessage(Message message, Locale locale) {
        return new ErrorMessage(message.getErrorCode(), message.getText(locale));
    }

}
