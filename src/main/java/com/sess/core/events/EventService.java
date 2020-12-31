package com.sess.core.events;

import com.sess.core.events.exceptions.GroupNotFoundException;
import com.sess.core.events.exceptions.UserNotFoundException;

import java.util.List;

public interface EventService {

    List<Event> getUserEvents(long userId) throws UserNotFoundException;

    List<Event> getGroupEvents(long groupId) throws GroupNotFoundException;

}
