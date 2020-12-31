package com.sess.core.dto.adapters;

import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;

public interface DTOAdapter<DTO, Entity> {

    Entity convertFromDTO(DTO dto) throws ConvertFromDTOException;

    DTO convertToDTO(Entity entity) throws ConvertToDTOException;

}
