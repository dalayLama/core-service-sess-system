package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.RunningTypeDTO;
import com.sess.core.dto.adapters.RunningTypeDTOAdapter;
import com.sess.core.exceptions.NotNullableId;
import com.sess.core.exceptions.OperationAppException;
import com.sess.core.exceptions.ValidationException;
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

    public RunningTypeApiHandlerService(RunningTypeService service, RunningTypeDTOAdapter runningTypeAdapter) {
        this.service = service;
        this.runningTypeAdapter = runningTypeAdapter;
    }

    @Override
    public RunningTypeDTO create(RunningTypeDTO dto) {
        try {
            RunningType rt = runningTypeAdapter.convertFromDTO(dto);
            RunningType savedRt = service.create(rt);
            return runningTypeAdapter.convertToDTO(savedRt);
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
