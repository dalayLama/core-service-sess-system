package com.sess.core.components.message;

import com.sess.core.ErrorsCodes;
import com.sess.core.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
                        Map.of(Locale.RU, "Не удалось сформировать ответ для пользователя")),
                MessageId.WRITE_DTO_EVENT_DATA_ERROR,
                new Message(ErrorsCodes.WRITE_DTO_EVENT_DATA_ERROR,
                        Map.of(Locale.RU, "Не удалось сформировать ответ для данных")),
                MessageId.NOT_NULLABLE_GROUP_ID,
                new Message(ErrorsCodes.NOT_NULLABLE_GROUP_ID,
                        Map.of(Locale.RU, "У новой группы должен быть пустой идентификатор")),
                MessageId.GROUP_NOT_FOUND,
                new Message(ErrorsCodes.GROUP_NOT_FOUND,
                        Map.of(Locale.RU, "Не удалось найти группу с идентификатором {}")),
                MessageId.USER_NOT_FOUND,
                new Message(ErrorsCodes.USER_NOT_FOUND,
                        Map.of(Locale.RU, "Не удалось найти пользователя с идентификатором {}"))
        ));
        messages.putAll(Map.of(
                MessageId.USER_ALREADY_EXIST_IN_GROUP,
                new Message(ErrorsCodes.USER_ALREADY_EXIST_IN_GROUP,
                        Map.of(Locale.RU, "Пользователь с идентификатором {} уже существует в группе {}")),
                MessageId.GROUP_TITLE_ALREADY_EXISTS,
                new Message(ErrorsCodes.GROUP_TITLE_ALREADY_EXISTS,
                        Map.of(Locale.RU, "Группа с таким названием уже существует")),
                MessageId.USER_NOT_FOUND_IN_GROUP,
                new Message(ErrorsCodes.USER_NOT_FOUND_IN_GROUP,
                        Map.of(Locale.RU, "Пользователь с идентификатором {} отсутствует в группе {}")),
                MessageId.NOT_NULLABLE_RUNNING_TYPE_ID,
                new Message(ErrorsCodes.NOT_NULLABLE_RUNNING_TYPE_ID,
                        Map.of(Locale.RU, "У нового типа забега должен быть пустой идентификатор")),
                MessageId.RUNNING_TYPE_CAPTION_ALREADY_EXISTS_IN_GROUP,
                new Message(ErrorsCodes.RUNNING_TYPE_CAPTION_ALREADY_EXISTS_IN_GROUP,
                        Map.of(Locale.RU, "Наименование типа забега({}) уже существует в группе ({})")),
                MessageId.RUNNING_TYPE_NOT_FOUND,
                new Message(ErrorsCodes.RUNNING_TYPE_NOT_FOUND,
                        Map.of(Locale.RU, "Не удалось найти тип забега с идентификатором {}"))
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

    @Override
    public ErrorMessage sayError(MessageId messageId, Object... params) {
        Message message = getMessage(messageId).orElse(notFoundErrorMessage);
        String resultText = MessageFormat.format(message.getText(defaultLocale), params);
        return new ErrorMessage(message.getErrorCode(), resultText);
    }

    private Optional<Message> getMessage(MessageId messageId) {
        Message message = messages.get(messageId);
        if (Objects.isNull(message)) {
            LOG.warn("Message {} wasn't found", messageId);
        }
        return Optional.of(message);
    }

    private String getText(MessageId messageId, Locale locale) {
        return getMessage(messageId).orElse(notFoundMessage).getText(locale);
    }

    private ErrorMessage getError(MessageId messageId, Locale locale) {
        Message message = getMessage(messageId).orElse(notFoundErrorMessage);
        return generateErrorMessage(message, locale);
    }

    private ErrorMessage generateErrorMessage(Message message, Locale locale) {
        return new ErrorMessage(message.getErrorCode(), message.getText(locale));
    }

}
