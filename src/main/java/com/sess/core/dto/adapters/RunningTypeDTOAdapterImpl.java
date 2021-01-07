package com.sess.core.dto.adapters;

import com.sess.core.dto.RunningTypeDTO;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.groups.Group;
import com.sess.core.running.RunningType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RunningTypeDTOAdapterImpl implements RunningTypeDTOAdapter {

    private final DTOAdapterGroup groupAdapter;

    public RunningTypeDTOAdapterImpl(DTOAdapterGroup groupAdapter) {
        this.groupAdapter = groupAdapter;
    }

    @Override
    public RunningType convertFromDTO(RunningTypeDTO runningTypeDTO) throws ConvertFromDTOException {
        if (Objects.isNull(runningTypeDTO)) {
            return null;
        }
        Group group = groupAdapter.convertFromDTO(runningTypeDTO.getGroup());

        RunningType rt = new RunningType();
        rt.setId(runningTypeDTO.getId());
        rt.setGroup(group);
        rt.setCaption(runningTypeDTO.getCaption());
        return rt;
    }

    @Override
    public RunningTypeDTO convertToDTO(RunningType runningType) throws ConvertToDTOException {
        return new RunningTypeDTO(
                runningType.getId(),
                groupAdapter.convertToDTO(runningType.getGroup()),
                runningType.getCaption()
        );
    }
}
