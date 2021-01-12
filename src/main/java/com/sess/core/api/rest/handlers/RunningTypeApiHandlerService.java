package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.RunningTypeDTO;
import com.sess.core.dto.adapters.RunningTypeDTOAdapter;
import com.sess.core.exceptions.*;
import com.sess.core.groups.Group;
import com.sess.core.groups.GroupService;
import com.sess.core.groups.exceptions.GroupNotFoundException;
import com.sess.core.running.RunningType;
import com.sess.core.running.RunningTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RunningTypeApiHandlerService implements RunningTypeApiHandler {

    private final RunningTypeService service;

    private final RunningTypeDTOAdapter runningTypeAdapter;

    private final GroupService groupService;

    private final MessageService messageService;

    public RunningTypeApiHandlerService(RunningTypeService service, RunningTypeDTOAdapter runningTypeAdapter, GroupService groupService, MessageService messageService) {
        this.service = service;
        this.runningTypeAdapter = runningTypeAdapter;
        this.groupService = groupService;
        this.messageService = messageService;
    }

    @Override
    public RunningTypeDTO create(long groupId, RunningTypeDTO dto) {
        try {
            Group group = groupService.findById(groupId).orElseThrow(() -> {
                ErrorMessage errorMessage = messageService.sayError(MessageId.GROUP_NOT_FOUND, groupId);
                throw new GroupNotFoundException(errorMessage);
            });
            RunningType rt = runningTypeAdapter.convertFromDTO(dto);
            rt.setGroup(group);
            RunningType savedRt = service.create(rt);
            return runningTypeAdapter.convertToDTO(savedRt);
        } catch (GroupNotFoundException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.NOT_FOUND);
        } catch (ValidationException | NotNullableId e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (OperationAppException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void update(RunningTypeDTO dto) {
        try {
            RunningType rt = runningTypeAdapter.convertFromDTO(dto);
            service.update(rt);
        } catch (ValidationException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.BAD_REQUEST);
        } catch (OperationAppException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void remove(long id) {
        try {
            service.remove(id);
        } catch (OperationAppException e) {
            throw new HttpStatusOperationException(e.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RunningTypeDTO> getAll(long groupId) {
        return service.getAll(groupId).stream().map(runningTypeAdapter::convertToDTO).collect(Collectors.toList());
    }
}
