package com.sess.core.api.rest;

import com.sess.core.api.rest.handlers.EventApiHandler;
import com.sess.core.api.rest.handlers.GroupRestApiHandler;
import com.sess.core.api.rest.handlers.RunningTypeApiHandler;
import com.sess.core.dto.DTOEvent;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.GroupDTO;
import com.sess.core.dto.RunningTypeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/groups")
public class GroupRestController {

    private final GroupRestApiHandler groupApiHandler;

    private final EventApiHandler eventApiHandler;

    private final RunningTypeApiHandler runningTypeApiHandler;

    public GroupRestController(GroupRestApiHandler apiHandler, EventApiHandler eventApiHandler, RunningTypeApiHandler runningTypeApiHandler) {
        this.groupApiHandler = apiHandler;
        this.eventApiHandler = eventApiHandler;
        this.runningTypeApiHandler = runningTypeApiHandler;
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO group) {
        return new ResponseEntity<>(groupApiHandler.create(group), HttpStatus.CREATED);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<Void> updateGroup(@PathVariable(value = "groupId") long groupId, @RequestBody GroupDTO group) {
        groupApiHandler.update(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(value = "groupId") long groupId) {
        groupApiHandler.deleteGroup(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<DTOUser>> getUsersByGroup(@PathVariable(value = "groupId") long groupId) {
        return new ResponseEntity<>(groupApiHandler.getUsersByGroup(groupId), HttpStatus.OK);
    }

    @PostMapping("/{groupId}/users/{userId}/roles")
    public ResponseEntity<Void> addRoles(
            @PathVariable(value = "groupId") long groupId,
            @PathVariable(value = "userId") long userId,
            @RequestBody Set<Long> rolesIds
    ) {
        groupApiHandler.addRoles(groupId, userId, rolesIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/users/{userId}/roles")
    public ResponseEntity<Void> removeRoles(
            @PathVariable(value = "groupId") long groupId,
            @PathVariable(value = "userId") long userId,
            @RequestBody Set<Long> rolesIds
    ) {
        groupApiHandler.removeRoles(groupId, userId, rolesIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{groupId}/events/")
    public ResponseEntity<DTOEvent> createEvent(
            @PathVariable long groupId,
            @RequestBody DTOEvent event
    ) {
        DTOEvent savedEvent = eventApiHandler.create(groupId, event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}/events/{eventId}")
    public ResponseEntity<Void> updateEvent(
            @PathVariable long groupId,
            @PathVariable long eventId,
            @RequestBody DTOEvent event) {
        eventApiHandler.update(event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/events/{eventId}")
    public ResponseEntity<Void> delete(
            @PathVariable long groupId,
            @PathVariable long eventId
    ) {
        eventApiHandler.delete(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{groupId}/running-types")
    public ResponseEntity<RunningTypeDTO> createRunningType(
            @PathVariable long groupId,
            @RequestBody RunningTypeDTO dto) {
        RunningTypeDTO runningTypeDTO = runningTypeApiHandler.create(groupId, dto);
        return new ResponseEntity<>(runningTypeDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/running-types")
    public ResponseEntity<List<RunningTypeDTO>> getRunningTypes(@PathVariable long groupId) {
        return new ResponseEntity<>(runningTypeApiHandler.getAll(groupId), HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/running-types")
    public ResponseEntity<Void> deleteRunningType(@PathVariable long groupId) {
        runningTypeApiHandler.remove(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
