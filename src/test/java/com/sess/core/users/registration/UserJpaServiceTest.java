package com.sess.core.users.registration;

import com.sess.core.ErrorsCodes;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.components.validator.Validator;
import com.sess.core.dao.repositories.UserJpaRepository;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.users.User;
import com.sess.core.exceptions.NotNullableId;
import com.sess.core.exceptions.SaveException;
import com.sess.core.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserJpaServiceTest {

    @Test
    public void shouldThrowNotNullableUserIdExceptionWhenUserDoesNotHaveId() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        MessageService messageService = mock(MessageService.class);
        UserJpaRepository repository = mock(UserJpaRepository.class);
        Validator validator = mock(Validator.class);
        when(messageService.sayError(MessageId.NOT_NULLABLE_USER_ID))
                .thenReturn(expectedErrorMessages.get(0));
        UserJpaService service = new UserJpaService(repository, messageService, validator);
        User user = new User();
        user.setId(2L);

        NotNullableId thrown = assertThrows(
                NotNullableId.class,
                () -> service.register(user)
        );

        verify(validator, never()).validate(any());
        verify(messageService).sayError(MessageId.NOT_NULLABLE_USER_ID);
        verify(repository, never()).save(any());
        verify(messageService, never()).sayError(MessageId.FAILED_SAVE_NEW_USER);
        List<ErrorMessage> errorMessages = new ArrayList<>(thrown.getError().getMessages());
        assertThat(errorMessages).containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
    }

    @Test
    public void shouldThrownValidateException() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, "message 1")
        );
        User user = new User();
        Validator validator = mock(Validator.class);
        MessageService messageService = mock(MessageService.class);
        UserJpaRepository repository = mock(UserJpaRepository.class);
        when(validator.validate(user))
                .thenReturn(Collections.singleton(expectedErrorMessages.get(0).getMessage()));

        UserJpaService service = new UserJpaService(repository, messageService, validator);
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> service.register(user)
        );

        verify(validator).validate(user);
        verify(repository, never()).save(user);
        verify(messageService, never()).sayError(MessageId.NOT_NULLABLE_USER_ID);
        verify(messageService, never()).sayError(MessageId.FAILED_SAVE_NEW_USER);

        List<ErrorMessage> errorMessages = new ArrayList<>(thrown.getError().getMessages());
        assertThat(errorMessages).containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
    }

    @Test
    public void shouldThrowRegistrationExceptionWhenFailedSave() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        User user = new User();
        user.setId(null);
        Validator validator = mock(Validator.class);
        MessageService messageService = mock(MessageService.class);
        UserJpaRepository repository = mock(UserJpaRepository.class);
        when(validator.validate(user)).thenReturn(Collections.emptySet());
        when(messageService.sayError(MessageId.FAILED_SAVE_NEW_USER))
                .thenReturn(expectedErrorMessages.get(0));
        when(repository.save(user))
                .thenThrow(new RuntimeException("Test exception"));

        UserJpaService service = new UserJpaService(repository, messageService, validator);
        SaveException thrown = assertThrows(
                SaveException.class,
                () -> service.register(user)
        );

        verify(validator).validate(user);
        verify(messageService, never()).sayError(MessageId.NOT_NULLABLE_USER_ID);
        verify(repository).save(user);
        verify(messageService).sayError(MessageId.FAILED_SAVE_NEW_USER);
        List<ErrorMessage> errorMessages = new ArrayList<>(thrown.getError().getMessages());
        assertThat(errorMessages).containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
    }

}