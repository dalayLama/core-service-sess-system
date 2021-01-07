package com.sess.core.events;

import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.components.validator.Validator;
import com.sess.core.dao.repositories.EventJpaRepository;
import com.sess.core.events.exceptions.EventNotFound;
import com.sess.core.events.exceptions.NotChangeableEventException;
import com.sess.core.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class JpaEventService implements EventService {

    private static final Logger LOG = LoggerFactory.getLogger(JpaEventService.class);

    private final EventJpaRepository eventRepository;

    private final Validator validator;

    private final MessageService messageService;

    public JpaEventService(EventJpaRepository eventRepository, Validator validator, MessageService messageService) {
        this.eventRepository = eventRepository;
        this.validator = validator;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public Event create(Event event) throws SaveException {
        if (Objects.nonNull(event.getId())) {
            ErrorMessage errMsg = messageService.sayError(MessageId.NOT_NULLABLE_EVENT_ID);
            throw new NotNullableId(errMsg);
        }
        validate(event);
        return eventRepository.saveAndFlush(event);
    }

    @Override
    @Transactional
    public void update(Event event) throws SaveException {
        eventRepository.findById(event.getId()).ifPresentOrElse(
                e -> updateDataEvent(e, event),
                () -> {
                    ErrorMessage errMsg = messageService.sayError(MessageId.EVENT_NOT_FOUND, event.getId());
                    EventNotFound exc = new EventNotFound(errMsg);
                    throw new SaveException(exc, exc.getError());
                }
        );
    }

    @Override
    @Transactional
    public void remove(long eventId) throws DeleteException {
        eventRepository.findById(eventId).ifPresentOrElse(
                e -> {
                    canChange(e);
                    eventRepository.delete(e);
                },
                () -> LOG.warn("event with id {} wasn't found", eventId)
        );
    }

    @Override
    public List<Event> getActualEvents(long groupId) {
        return eventRepository.findAllByGroupIdAndFactualDtStartIsNull(groupId);
    }

    @Override
    public List<Event> getAllEvents(long groupId) {
        return eventRepository.findAllByGroupId(groupId);
    }

    private void updateDataEvent(Event target, Event source) {
        canChange(target);

        target.setEventName(source.getEventName());
        target.setDescription(source.getDescription());
        target.setDistance(source.getDistance());
        target.setPlaceStart(source.getPlaceStart());
        target.setPlaceEnd(source.getPlaceEnd());
        target.setPlannedDtStart(source.getPlannedDtStart());
        target.setPlannedDtEnd(source.getPlannedDtEnd());
        validate(target);
        eventRepository.saveAndFlush(target);
    }

    private void validate(Event event) {
        Set<String> result = validator.validate(event);
        if ((Objects.nonNull(event.getFactualDtStart()) || Objects.nonNull(event.getFactualDtEnd()))) {
            String errMsg = messageService.say(MessageId.DURING_CREATE_FACTUAL_DATES_MUST_BE_NULL);
            result.add(errMsg);
        }
        if (!result.isEmpty()) {
            throw new ValidationException(result);
        }
    }

    private void canChange(Event event) throws NotChangeableEventException {
        if (Objects.nonNull(event.getFactualDtStart())) {
            ErrorMessage errMsg = messageService.sayError(MessageId.EVENT_CAN_NOT_BE_CHANGED, event.getId());
            throw new NotChangeableEventException(errMsg);
        }
    }

}
