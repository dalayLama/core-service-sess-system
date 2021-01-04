package com.sess.core.api.rest;

import com.sess.core.api.rest.handlers.GroupRestApiHandler;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.GroupDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupRestController {

    private final GroupRestApiHandler apiHandler;

    public GroupRestController(GroupRestApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO group) {
        return new ResponseEntity<>(apiHandler.create(group), HttpStatus.CREATED);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<Void> updateGroup(@PathVariable(value = "groupId") long groupId, @RequestBody GroupDTO group) {
        apiHandler.update(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(value = "groupId") long groupId) {
        apiHandler.deleteGroup(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<DTOUser>> getUsersByGroup(@PathVariable(value = "groupId") long groupId) {
        return new ResponseEntity<>(apiHandler.getUsersByGroup(groupId), HttpStatus.OK);
    }

}
