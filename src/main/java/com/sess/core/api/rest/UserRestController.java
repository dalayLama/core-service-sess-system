package com.sess.core.api.rest;

import com.sess.core.api.rest.handlers.UserRestApiHandler;
import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.DTOUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRestApiHandler apiHandler;

    public UserRestController(UserRestApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @PostMapping()
    public ResponseEntity<Object> registerNewUser(DTOUser DTOUser) {
        try {
            DTOUser registeredUser = apiHandler.register(DTOUser);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (HttpStatusOperationException e) {
            return new ResponseEntity<>(e.getError(), e.getStatus());
        }
    }

}
