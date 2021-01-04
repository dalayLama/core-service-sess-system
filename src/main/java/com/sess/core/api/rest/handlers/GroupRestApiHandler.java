package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.GroupDTO;

import java.util.List;

public interface GroupRestApiHandler {

    GroupDTO create(GroupDTO group) throws HttpStatusOperationException;

    void update(GroupDTO group) throws HttpStatusOperationException;

    void deleteGroup(long groupId) throws HttpStatusOperationException;

    List<GroupDTO> getGroupsByCity(long cityId);

    List<DTOUser> getUsersByGroup(long groupId);

}
