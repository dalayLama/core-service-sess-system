package com.sess.core.users.registration;

import com.sess.core.users.User;
import com.sess.core.users.registration.exceptions.RegistrationException;

public interface UserRegistrationService {

    User register(User user) throws RegistrationException;

}
