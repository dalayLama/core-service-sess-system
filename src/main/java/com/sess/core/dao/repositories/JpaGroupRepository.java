package com.sess.core.dao.repositories;

import com.sess.core.groups.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGroupRepository extends JpaRepository<Group, Long> {

}
