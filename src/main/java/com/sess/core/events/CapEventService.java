package com.sess.core.events;

import com.sess.core.groups.exceptions.GroupNotFoundException;
import com.sess.core.users.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CapEventService implements EventService {

    public static final int eventsCount = 23;

    private final List<EventRun> eventsContainer = new ArrayList<>();

    @Override
    public List<Event> getUserEvents(long userId) throws UserNotFoundException {
        return new ArrayList<>(eventsContainer);
    }

    @Override
    public List<Event> getGroupEvents(long groupId) throws GroupNotFoundException {
        return new ArrayList<>(eventsContainer);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 23; i++) {
            EventRun event = new EventRun();
            event.setId((long) i + 1);
            event.setName(String.format("Event of %d day", i+1));
            event.setDestination((float) i + 1);
            event.setStartDTime(LocalDateTime.now().minusDays(eventsCount - i));
            event.setEndDTime(LocalDateTime.now().minusDays(eventsCount - i).plusHours(2L));
            eventsContainer.add(event);
        }
    }

}
