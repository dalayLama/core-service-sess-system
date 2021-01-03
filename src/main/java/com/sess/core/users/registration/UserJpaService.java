package com.sess.core.users.registration;

import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.components.validator.Validator;
import com.sess.core.dao.repositories.UserJpaRepository;
import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.users.User;
import com.sess.core.exceptions.NotNullableId;
import com.sess.core.exceptions.SaveException;
import com.sess.core.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserJpaService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserJpaService.class);

    private final UserJpaRepository userJpaRepository;

    private final MessageService messageService;

    private final Validator validator;

    public UserJpaService(UserJpaRepository userJpaRepository, MessageService messageService, Validator validator) {
        this.userJpaRepository = userJpaRepository;
        this.messageService = messageService;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User register(User user) throws SaveException {
        if (Objects.nonNull(user.getId())) {
            ErrorMessage error = messageService.sayError(MessageId.NOT_NULLABLE_USER_ID);
            throw new NotNullableId(error);
        }

        validate(user);
        try {
            return userJpaRepository.save(user);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ErrorMessage error = messageService.sayError(MessageId.FAILED_SAVE_NEW_USER);
            throw new SaveException(ErrorBuilder.singletonMessage(false, error));
        }
    }

    @Override
    public Optional<User> findById(long userId) {
        return userJpaRepository.findById(userId);
    }

    private void validate(User user) throws ValidationException {
        if (Objects.isNull(validator)) {
            return;
        }

        Set<String> messages = validator.validate(user);
        if (!messages.isEmpty()) {
            throw new ValidationException(messages);
        }
    }

}
