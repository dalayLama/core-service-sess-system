package com.sess.core.dao.repositories;

import com.sess.core.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {


}
