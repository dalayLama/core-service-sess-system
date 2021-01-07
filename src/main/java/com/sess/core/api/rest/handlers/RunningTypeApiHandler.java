package com.sess.core.api.rest.handlers;

import com.sess.core.dto.RunningTypeDTO;
import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.SaveException;

import java.util.List;

public interface RunningTypeApiHandler {

    RunningTypeDTO create(RunningTypeDTO dto) throws SaveException;

    void update(RunningTypeDTO dto) throws SaveException;

    void remove(long id) throws DeleteException;

    List<RunningTypeDTO> getAll(long groupId);

}
