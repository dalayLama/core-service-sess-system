package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.DTOOnlyDataEvent;
import com.sess.core.dto.adapters.DTOOnlyDataEventAdapter;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.events.Event;
import com.sess.core.events.EventService;
import com.sess.core.users.exceptions.UserNotFoundException;
import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.ErrorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventsRestApiHandlerImpl implements EventsRestApiHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventsRestApiHandlerImpl.class);

    private final EventService eventService;

    private final DTOOnlyDataEventAdapter onlyDataAdapter;

    private final MessageService messageService;

    public EventsRestApiHandlerImpl(EventService eventService, DTOOnlyDataEventAdapter onlyDataAdapter, MessageService messageService) {
        this.onlyDataAdapter = onlyDataAdapter;
        this.eventService = eventService;
        this.messageService = messageService;
    }

    @Override
    public Collection<? extends DTOOnlyDataEvent> getUserEvents(long userId) throws HttpStatusOperationException {
        try {
            List<Event> userEvents = eventService.getUserEvents(userId);
            return userEvents.stream().map(onlyDataAdapter::convertToDTO).collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new HttpStatusOperationException(e.getError(), HttpStatus.NOT_FOUND);
        } catch (ConvertToDTOException e) {
            LOG.error(e.getMessage(), e);
            Error error = ErrorBuilder
                    .singletonMessage(false, messageService.sayError(MessageId.WRITE_DTO_EVENT_DATA_ERROR));
            throw new HttpStatusOperationException(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
