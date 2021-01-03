package com.sess.core.users.registration;

import com.sess.core.users.User;
import com.sess.core.exceptions.SaveException;

import java.util.Optional;

public interface UserService {

    User register(User user) throws SaveException;

    Optional<User> findById(long userId);

}
