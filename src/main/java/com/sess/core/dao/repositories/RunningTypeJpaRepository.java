package com.sess.core.dao.repositories;

import com.sess.core.running.RunningType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RunningTypeJpaRepository extends JpaRepository<RunningType, Long> {

    List<RunningType> findAllByGroupIdAndDeletedFalse(long groupId);

    @Query("select (count(rt) > 0) from RunningType rt where " +
            "rt.group.id = ?1 and rt.caption = ?2 and rt.deleted = false")
    boolean exists(long groupId, String caption);

    @Query("select (count(rt) > 0) from RunningType rt where " +
            "rt.group.id = ?1 and rt.caption = ?2 and rt.id <> ?3 and rt.deleted = false")
    boolean exists(long groupId, String caption, long exceptId);

}
