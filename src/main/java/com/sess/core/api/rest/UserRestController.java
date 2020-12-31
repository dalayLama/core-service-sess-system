package com.sess.core.api.rest;

import com.sess.core.api.rest.handlers.EventsRestApiHandler;
import com.sess.core.api.rest.handlers.UserRestApiHandler;
import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.DTOUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRestApiHandler userApiHandler;

    private final EventsRestApiHandler eventsApiHandler;

    public UserRestController(UserRestApiHandler userApiHandler, EventsRestApiHandler eventsApiHandler) {
        this.userApiHandler = userApiHandler;
        this.eventsApiHandler = eventsApiHandler;
    }

    @PostMapping()
    public ResponseEntity<Object> registerNewUser(DTOUser DTOUser) {
        try {
            DTOUser registeredUser = userApiHandler.register(DTOUser);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (HttpStatusOperationException e) {
            return new ResponseEntity<>(e.getError(), e.getStatus());
        }
    }

    @GetMapping(value = "/{idUser}/events")
    public ResponseEntity<Object> getUserEvents(@PathVariable(name = "idUser") Long idUser) {
        try {
            return new ResponseEntity<>(eventsApiHandler.getUserEvents(idUser), HttpStatus.OK);
        } catch (HttpStatusOperationException e) {
            return new ResponseEntity<>(e.getError(), e.getStatus());
        }
    }

}
