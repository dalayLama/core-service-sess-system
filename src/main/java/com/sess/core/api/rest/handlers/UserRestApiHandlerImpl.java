package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.adapters.UserDTOAdapter;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.exceptions.ErrorBuilder;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.users.User;
import com.sess.core.users.registration.UserRegistrationService;
import com.sess.core.exceptions.SaveException;
import com.sess.core.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserRestApiHandlerImpl implements UserRestApiHandler {

    private final UserRegistrationService registrationService;

    private final UserDTOAdapter adapter;

    private final MessageService messageService;

    public UserRestApiHandlerImpl(
            UserRegistrationService registrationService,
            UserDTOAdapter adapter,
            MessageService messageService) {
        this.registrationService = registrationService;
        this.adapter = adapter;
        this.messageService = messageService;
    }

    @Override
    public DTOUser register(DTOUser user) throws HttpStatusOperationException {
        try {
            User entity = adapter.convertFromDTO(user);
            User registeredUser = registrationService.register(entity);
            return adapter.convertToDTO(registeredUser);
        } catch (ConvertFromDTOException e) {
            ErrorMessage error = messageService.sayError(MessageId.READ_DTO_USER_DATA_ERROR);
            throw new HttpStatusOperationException(
                    ErrorBuilder.singletonMessage(false, error),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ConvertToDTOException e) {
            ErrorMessage error = messageService.sayError(MessageId.WRITE_DTO_USER_DATA_ERROR);
            throw new HttpStatusOperationException(
                    ErrorBuilder.singletonMessage(true, error),
                    HttpStatus.CREATED
            );
        } catch (ValidationException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (SaveException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
