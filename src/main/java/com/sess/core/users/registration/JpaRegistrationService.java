package com.sess.core.users.registration;

import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dao.repositories.UserJpaRepository;
import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.users.User;
import com.sess.core.users.registration.exceptions.NotNullableUserId;
import com.sess.core.users.registration.exceptions.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class JpaRegistrationService implements UserRegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(JpaRegistrationService.class);

    private final UserJpaRepository userJpaRepository;

    private final MessageService messageService;

    public JpaRegistrationService(UserJpaRepository userJpaRepository, MessageService messageService) {
        this.userJpaRepository = userJpaRepository;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public User register(User user) throws RegistrationException {
        if (Objects.nonNull(user.getId())) {
            ErrorMessage error = messageService.sayError(MessageId.NOT_NULLABLE_USER_ID);
            throw new NotNullableUserId(error);
        }
        try {
            return userJpaRepository.save(user);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ErrorMessage error = messageService.sayError(MessageId.FAILED_SAVE_NEW_USER);
            throw new RegistrationException(ErrorBuilder.singletonMessage(false, error));
        }
    }

}
