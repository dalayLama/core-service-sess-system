package com.sess.core.dto.adapters;

import com.sess.core.dto.DTOEvent;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.events.Event;
import com.sess.core.running.RunningType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EventDTOAdapterImpl implements EventDTOAdapter {

    private final DTOAdapterGroup groupAdapter;

    private final RunningTypeDTOAdapter runningTypeAdapter;

    public EventDTOAdapterImpl(
            DTOAdapterGroup groupAdapter,
            RunningTypeDTOAdapter runningTypeAdapter) {
        this.groupAdapter = groupAdapter;
        this.runningTypeAdapter = runningTypeAdapter;
    }

    @Override
    public Event convertFromDTO(DTOEvent dtoEvent) throws ConvertFromDTOException {
        if (Objects.isNull(dtoEvent)) {
            return null;
        }

        RunningType rt = runningTypeAdapter.convertFromDTO(dtoEvent.getRunningType());

        Event event = new Event();
        event.setId(dtoEvent.getId());
        event.setRunningType(rt);
        event.setEventName(dtoEvent.getEventName());
        event.setPlaceStart(dtoEvent.getPlaceStart());
        event.setPlaceEnd(dtoEvent.getPlaceEnd());
        event.setDistance(dtoEvent.getDistance());
        event.setPlannedDtStart(dtoEvent.getPlannedDtStart());
        event.setPlannedDtEnd(dtoEvent.getPlannedDtEnd());
        event.setDescription(dtoEvent.getDescription());
        return event;
    }

    @Override
    public DTOEvent convertToDTO(Event event) throws ConvertToDTOException {
        if (Objects.isNull(event)) {
            return null;
        }

        return new DTOEvent(
                event.getId(),
                groupAdapter.convertToDTO(event.getGroup()),
                runningTypeAdapter.convertToDTO(event.getRunningType()),
                event.getEventName(),
                event.getDistance(),
                event.getPlaceStart(),
                event.getPlaceEnd(),
                event.getPlannedDtStart(),
                event.getPlannedDtEnd(),
                event.getDescription(),
                event.getFactualDtStart(),
                event.getFactualDtEnd()
        );
    }
}
