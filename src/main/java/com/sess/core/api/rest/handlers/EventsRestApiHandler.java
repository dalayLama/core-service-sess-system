package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.DTOOnlyDataEvent;

import java.util.Collection;

public interface EventsRestApiHandler {

    Collection<? extends DTOOnlyDataEvent> getUserEvents(long userId) throws HttpStatusOperationException;

}
