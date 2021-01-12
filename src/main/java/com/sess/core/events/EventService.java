package com.sess.core.events;

import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.SaveException;

import java.util.List;

public interface EventService {

    Event create(Event event) throws SaveException;

    void update(Event event) throws SaveException;

    void remove(long eventId) throws DeleteException;

    List<Event> getActualEvents(long groupId);

    List<Event> getAllEvents(long groupId);

}
