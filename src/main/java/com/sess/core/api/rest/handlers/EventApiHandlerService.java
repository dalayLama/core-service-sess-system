package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.DTOEvent;
import com.sess.core.dto.adapters.EventDTOAdapter;
import com.sess.core.events.Event;
import com.sess.core.events.EventService;
import com.sess.core.events.exceptions.NotChangeableEventException;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.NotNullableId;
import com.sess.core.exceptions.OperationAppException;
import com.sess.core.exceptions.ValidationException;
import com.sess.core.groups.GroupService;
import com.sess.core.groups.exceptions.GroupNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventApiHandlerService implements EventApiHandler {

    private final EventService eventService;

    private final EventDTOAdapter adapter;

    private final GroupService groupService;

    private final MessageService messageService;

    public EventApiHandlerService(EventService eventService, EventDTOAdapter adapter, GroupService groupService, MessageService messageService) {
        this.eventService = eventService;
        this.adapter = adapter;
        this.groupService = groupService;
        this.messageService = messageService;
    }

    @Override
    public DTOEvent create(long groupId, DTOEvent dto) throws HttpStatusOperationException {
        try {
            groupService.findById(groupId).orElseThrow(() -> {
                ErrorMessage errMsg = messageService.sayError(MessageId.GROUP_NOT_FOUND, groupId);
                throw new GroupNotFoundException(errMsg);
            });
            Event event = adapter.convertFromDTO(dto);
            Event savedEvent = eventService.create(event);
            return adapter.convertToDTO(savedEvent);
        } catch (GroupNotFoundException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.NOT_FOUND);
        } catch (NotNullableId | ValidationException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (OperationAppException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void update(DTOEvent dto) throws HttpStatusOperationException {
        try {
            Event event = adapter.convertFromDTO(dto);
            eventService.update(event);
        } catch (ValidationException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (NotChangeableEventException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.LOCKED);
        } catch (OperationAppException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(long eventId) throws HttpStatusOperationException {
        try {
            eventService.remove(eventId);
        } catch (NotChangeableEventException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.LOCKED);
        } catch (OperationAppException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<DTOEvent> getActualEvent(long groupId) {
        return eventService.getActualEvents(groupId).stream().map(adapter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DTOEvent> getAllEvent(long groupId) {
        return eventService.getAllEvents(groupId).stream().map(adapter::convertToDTO).collect(Collectors.toList());
    }



}
