package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.DTOEvent;

import java.util.List;

public interface EventsApiHandler {

    DTOEvent create(DTOEvent dto) throws HttpStatusOperationException;

    void update(DTOEvent dto) throws HttpStatusOperationException;

    void delete(long eventId) throws HttpStatusOperationException;

    List<DTOEvent> getActualEvent(long groupId);

    List<DTOEvent> getAllEvent(long groupId);

}
