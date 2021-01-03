package com.sess.core.dao.repositories;

import com.sess.core.groups.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaGroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByDeletedFalse();

    Optional<Group> findByTitleAndDeletedFalse(String title);

    @Query("select g from Group g where g.id <> ?1 and g.title = ?2 and g.deleted = false")
    Optional<Group> findByTitleAndDeletedFalse(long exceptGroupId, String title);

}
