package com.sess.core.api.rest;

import com.sess.core.api.rest.handlers.CityRestApiHandler;
import com.sess.core.api.rest.handlers.GroupRestApiHandler;
import com.sess.core.dto.DTOCity;
import com.sess.core.dto.GroupDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityRestController {

    private final CityRestApiHandler cityApiHandler;

    private final GroupRestApiHandler groupApiHandler;

    public CityRestController(CityRestApiHandler cityApiHandler, GroupRestApiHandler groupApiHandler) {
        this.cityApiHandler = cityApiHandler;
        this.groupApiHandler = groupApiHandler;
    }

    @GetMapping
    public ResponseEntity<List<DTOCity>> getAllCities() {
        return new ResponseEntity<>(cityApiHandler.getAllCities(), HttpStatus.OK);
    }

    @GetMapping("/{groupId}/groups")
    public ResponseEntity<List<GroupDTO>> getGroupsByCity(@PathVariable(value = "groupId") long groupId) {
        return new ResponseEntity<>(groupApiHandler.getGroupsByCity(groupId), HttpStatus.OK);
    }

}
