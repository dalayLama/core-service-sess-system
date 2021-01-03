package com.sess.core.dao.repositories;

import com.sess.core.groups.UserOfGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOfGroupJpaRepository extends JpaRepository<UserOfGroup, Long> {

    boolean existsByGroupIdAndUserId(long groupId, long userId);

    Optional<UserOfGroup> findByGroupIdAndUserId(long groupId, long userId);

}
