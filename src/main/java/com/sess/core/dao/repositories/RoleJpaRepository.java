package com.sess.core.dao.repositories;

import com.sess.core.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {

}
