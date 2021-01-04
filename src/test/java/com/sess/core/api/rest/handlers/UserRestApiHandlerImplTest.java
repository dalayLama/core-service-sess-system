package com.sess.core.api.rest.handlers;

import com.sess.core.ErrorsCodes;
import com.sess.core.TestUtils;
import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.adapters.CityDTOAdapter;
import com.sess.core.dto.adapters.CityDTOAdapterImpl;
import com.sess.core.dto.adapters.UserDTOAdapter;
import com.sess.core.dto.adapters.UserDTOAdapterImpl;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.cities.City;
import com.sess.core.users.User;
import com.sess.core.users.registration.UserService;
import com.sess.core.exceptions.SaveException;
import com.sess.core.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserRestApiHandlerImplTest {

    @Test
    public void shouldExecuteAllExpectedMethods() {
        UserService registrationService = mock(UserService.class);
        CityDTOAdapter cityAdapter = spy(new CityDTOAdapterImpl());
        UserDTOAdapter userAdapter = spy(new UserDTOAdapterImpl(cityAdapter));
        MessageService messageService = mock(MessageService.class);
        when(registrationService.register(any(User.class)))
                .thenReturn(TestUtils.createUser());

        DTOUser dtoUser = TestUtils.createDTOUser(null);

        UserRestApiHandlerImpl userRestApiHandler = new UserRestApiHandlerImpl(registrationService, userAdapter, messageService);
        DTOUser register = userRestApiHandler.register(dtoUser);

        assertThat(register.getId()).isNotNull();
        verify(userAdapter).convertFromDTO(dtoUser);
        verify(cityAdapter).convertFromDTO(dtoUser.getCity());
        verify(registrationService).register(any(User.class));
        verify(userAdapter).convertToDTO(any(User.class));
        verify(cityAdapter).convertToDTO(any(City.class));
    }

    @Test
    public void shouldThrownConvertFromDtoException() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        DTOUser dtoUser = TestUtils.createDTOUser(null);
        UserService registrationService = mock(UserService.class);
        CityDTOAdapter cityAdapter = spy(new CityDTOAdapterImpl());
        UserDTOAdapter userAdapter = spy(new UserDTOAdapterImpl(cityAdapter));
        MessageService messageService = mock(MessageService.class);
        when(userAdapter.convertFromDTO(dtoUser))
                .thenThrow(new ConvertFromDTOException("test exception"));
        when(messageService.sayError(MessageId.READ_DTO_USER_DATA_ERROR))
                .thenReturn(expectedErrorMessages.get(0));

        UserRestApiHandlerImpl userRestApiHandler = new UserRestApiHandlerImpl(registrationService, userAdapter, messageService);

        HttpStatusOperationException thrown = assertThrows(
                HttpStatusOperationException.class,
                () -> userRestApiHandler.register(dtoUser)
        );

        assertThat(thrown.getStatus()).isEqualTo(expectedStatus);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
        verify(registrationService, never()).register(any());
        verify(userAdapter, never()).convertToDTO(any());
        verify(messageService).sayError(MessageId.READ_DTO_USER_DATA_ERROR);
    }

    @Test
    public void shouldThrownConvertToDtoException() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        HttpStatus expectedStatus = HttpStatus.CREATED;
        DTOUser dtoUser = TestUtils.createDTOUser(null);
        UserService registrationService = mock(UserService.class);
        CityDTOAdapter cityAdapter = spy(new CityDTOAdapterImpl());
        UserDTOAdapter userAdapter = spy(new UserDTOAdapterImpl(cityAdapter));
        MessageService messageService = mock(MessageService.class);
        when(userAdapter.convertToDTO(any(User.class)))
                .thenThrow(new ConvertToDTOException("test exception"));
        when(messageService.sayError(MessageId.WRITE_DTO_USER_DATA_ERROR))
                .thenReturn(expectedErrorMessages.get(0));
        when(registrationService.register(any(User.class)))
                .thenReturn(TestUtils.createUser());

        UserRestApiHandlerImpl userRestApiHandler = new UserRestApiHandlerImpl(registrationService, userAdapter, messageService);

        HttpStatusOperationException thrown = assertThrows(
                HttpStatusOperationException.class,
                () -> userRestApiHandler.register(dtoUser)
        );

        assertThat(thrown.getStatus()).isEqualTo(expectedStatus);
        assertThat(thrown.getError().isResultOperation()).isTrue();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
        verify(userAdapter).convertFromDTO(dtoUser);
        verify(cityAdapter).convertFromDTO(dtoUser.getCity());
        verify(registrationService).register(any(User.class));
        verify(messageService).sayError(MessageId.WRITE_DTO_USER_DATA_ERROR);
    }

    @Test
    public void shouldThrownValidateException() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, "message 1")
        );
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        DTOUser dtoUser = TestUtils.createDTOUser(null);
        UserService registrationService = mock(UserService.class);
        CityDTOAdapter cityAdapter = spy(new CityDTOAdapterImpl());
        UserDTOAdapter userAdapter = spy(new UserDTOAdapterImpl(cityAdapter));
        MessageService messageService = mock(MessageService.class);
        when(registrationService.register(any(User.class)))
                .thenThrow(new ValidationException(
                        expectedErrorMessages.stream().map(ErrorMessage::getMessage).collect(Collectors.toSet())
                ));

        UserRestApiHandlerImpl userRestApiHandler = new UserRestApiHandlerImpl(registrationService, userAdapter, messageService);

        HttpStatusOperationException thrown = assertThrows(
                HttpStatusOperationException.class,
                () -> userRestApiHandler.register(dtoUser)
        );

        assertThat(thrown.getStatus()).isEqualTo(expectedStatus);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
        verify(userAdapter).convertFromDTO(dtoUser);
        verify(cityAdapter).convertFromDTO(dtoUser.getCity());
    }

    @Test
    public void shouldThrownRegistrationException() {
        List<ErrorMessage> expectedErrorMessages = List.of(
                new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, "message 1")
        );
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        DTOUser dtoUser = TestUtils.createDTOUser(null);
        UserService registrationService = mock(UserService.class);
        CityDTOAdapter cityAdapter = spy(new CityDTOAdapterImpl());
        UserDTOAdapter userAdapter = spy(new UserDTOAdapterImpl(cityAdapter));
        MessageService messageService = mock(MessageService.class);
        when(registrationService.register(any(User.class)))
                .thenThrow(new SaveException(ErrorBuilder.listMessages(false, expectedErrorMessages)));

        UserRestApiHandlerImpl userRestApiHandler = new UserRestApiHandlerImpl(registrationService, userAdapter, messageService);

        HttpStatusOperationException thrown = assertThrows(
                HttpStatusOperationException.class,
                () -> userRestApiHandler.register(dtoUser)
        );

        assertThat(thrown.getStatus()).isEqualTo(expectedStatus);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedErrorMessages);
        verify(userAdapter).convertFromDTO(dtoUser);
        verify(cityAdapter).convertFromDTO(dtoUser.getCity());
    }

}