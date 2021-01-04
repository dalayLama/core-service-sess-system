package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.GroupDTO;
import com.sess.core.dto.adapters.DTOAdapterGroup;
import com.sess.core.dto.adapters.UserDTOAdapter;
import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.*;
import com.sess.core.groups.Group;
import com.sess.core.groups.GroupService;
import com.sess.core.groups.exceptions.GroupNotFoundException;
import com.sess.core.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupRestApiHandlerService implements GroupRestApiHandler {

    private final GroupService groupService;

    private final DTOAdapterGroup groupAdapter;

    private final UserDTOAdapter userAdapter;

    private final MessageService messageService;

    public GroupRestApiHandlerService(
            GroupService groupService,
            DTOAdapterGroup groupAdapter,
            UserDTOAdapter userAdapter,
            MessageService messageService) {
        this.groupService = groupService;
        this.groupAdapter = groupAdapter;
        this.userAdapter = userAdapter;
        this.messageService = messageService;
    }

    @Override
    public GroupDTO create(GroupDTO dto) throws HttpStatusOperationException {
        try {
            Group group = groupAdapter.convertFromDTO(dto);
            Group savedGroup = groupService.createGroup(group);
            return groupAdapter.convertToDTO(savedGroup);
        } catch (ValidationException | NotNullableId e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (SaveException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void update(GroupDTO dto) throws HttpStatusOperationException {
        try {
            Group group = groupAdapter.convertFromDTO(dto);
            groupService.updateGroup(group);
        } catch (ValidationException | GroupNotFoundException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (SaveException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteGroup(long groupId) throws HttpStatusOperationException {
        try {
            groupService.deleteGroup(groupId);
        } catch (DeleteException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<GroupDTO> getGroupsByCity(long cityId) {
        return groupService.getAllByCity(cityId).stream()
                .map(groupAdapter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DTOUser> getUsersByGroup(long groupId) {
        List<User> users = new ArrayList<>();
        groupService.findById(groupId).ifPresentOrElse(
                group -> users.addAll(group.getUsers()),
                () -> {
                    Error error = ErrorBuilder.singletonMessage(
                            false, messageService.sayError(MessageId.GROUP_NOT_FOUND, groupId));
                    throw new HttpStatusOperationException(error, HttpStatus.NOT_FOUND);
                }
        );
        return users.stream()
                .map(userAdapter::convertToDTO)
                .collect(Collectors.toList());
    }
}
