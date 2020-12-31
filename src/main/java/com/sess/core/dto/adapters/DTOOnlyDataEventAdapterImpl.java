package com.sess.core.dto.adapters;

import com.sess.core.dto.DTOOnlyDataEvent;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.events.Event;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DTOOnlyDataEventAdapterImpl implements DTOOnlyDataEventAdapter {

    @Override
    public Event convertFromDTO(DTOOnlyDataEvent dtoOnlyDataEvent) throws ConvertFromDTOException {
        throw new ConvertFromDTOException(new UnsupportedOperationException());
    }

    @Override
    public DTOOnlyDataEvent convertToDTO(Event event) throws ConvertToDTOException {
        if (Objects.isNull(event)) {
            return null;
        }
        return new DTOOnlyDataEvent(
                event.getId(),
                event.getName(),
                event.getStartDTime(),
                event.getEndDTime(),
                event.getOwnDetails()
        );
    }
}
