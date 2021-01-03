package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dto.DTOOnlyDataEvent;
import com.sess.core.dto.adapters.DTOOnlyDataEventAdapter;
import com.sess.core.dto.adapters.DTOOnlyDataEventAdapterImpl;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.events.CapEventService;
import com.sess.core.events.Event;
import com.sess.core.events.exceptions.UserNotFoundException;
import com.sess.core.exceptions.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EventsRestApiHandlerImplTest {

    @Test
    public void shouldExecuteExpectedMethods() {
        final long userId = 1L;
        CapEventService eventService = spy(getCapEventService());
        DTOOnlyDataEventAdapter adapter = spy(new DTOOnlyDataEventAdapterImpl());
        MessageService messageService = mock(MessageService.class);
        when(eventService.getUserEvents(userId)).thenCallRealMethod();

        EventsRestApiHandlerImpl handler = new EventsRestApiHandlerImpl(eventService, adapter, messageService);
        Collection<? extends DTOOnlyDataEvent> events = handler.getUserEvents(userId);
        assertThat(events.size()).isEqualTo(CapEventService.eventsCount);
        verify(eventService).getUserEvents(userId);
        verify(adapter, times(events.size())).convertToDTO(any(Event.class));
    }

    @Test
    public void shouldUserNotFoundException() {
        final long userId = 1L;
        List<ErrorMessage> errorMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        CapEventService eventService = spy(getCapEventService());
        DTOOnlyDataEventAdapter adapter = spy(new DTOOnlyDataEventAdapterImpl());
        MessageService messageService = mock(MessageService.class);
        when(eventService.getUserEvents(userId))
                .thenThrow(new UserNotFoundException(errorMessages.get(0)));

        EventsRestApiHandlerImpl handler = new EventsRestApiHandlerImpl(eventService, adapter, messageService);

        HttpStatusOperationException thrown = assertThrows(
                HttpStatusOperationException.class,
                () -> handler.getUserEvents(userId)
        );

        assertThat(thrown.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(errorMessages);
        verify(adapter, never()).convertToDTO(any());
    }

    @Test
    public void shouldThrownConvertToDTOException() {
        final Long userId = 1L;
        List<ErrorMessage> errorMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        CapEventService eventService = spy(getCapEventService());
        DTOOnlyDataEventAdapter adapter = spy(new DTOOnlyDataEventAdapterImpl());
        MessageService messageService = mock(MessageService.class);
        when(eventService.getUserEvents(userId)).thenCallRealMethod();
        when(adapter.convertToDTO(any(Event.class))).thenThrow(new ConvertToDTOException("test exception"));
        when(messageService.sayError(MessageId.WRITE_DTO_EVENT_DATA_ERROR))
                .thenReturn(errorMessages.get(0));

        EventsRestApiHandlerImpl handler = new EventsRestApiHandlerImpl(eventService, adapter, messageService);

        HttpStatusOperationException thrown = assertThrows(
                HttpStatusOperationException.class,
                () -> handler.getUserEvents(userId)
        );

        assertThat(thrown.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(errorMessages);
        verify(eventService).getUserEvents(userId);
    }
    
    private CapEventService getCapEventService() {
        CapEventService cap = new CapEventService();
        cap.init();
        return cap;
    }

}