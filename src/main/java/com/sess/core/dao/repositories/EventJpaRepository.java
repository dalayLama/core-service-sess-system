package com.sess.core.dao.repositories;

import com.sess.core.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventJpaRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByGroupId(long groupId);

    List<Event> findAllByGroupIdAndFactualDtStartIsNull(long groupId);

}
