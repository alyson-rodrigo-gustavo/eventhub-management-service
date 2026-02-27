package br.com.alysongustavo.eventhubmanagementservice.infra.listener;

import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.event.TicketPurchaseRequestedEvent;
import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.ConfirmTicketPurchaseUseCase;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserByEmailNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TicketPurchaseEventListenerTest {

    @Mock
    private ConfirmTicketPurchaseUseCase confirmTicketPurchaseUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TicketPurchaseEventListener listener;

    @Test
    @DisplayName("Should parse message and call use case")
    void shouldCallUseCaseWhenMessageIsValid() throws Exception {
        String message = "{\"eventId\":10,\"buyerEmail\":\"buyer@test.com\"}";
        TicketPurchaseRequestedEvent event = new TicketPurchaseRequestedEvent(10L, "buyer@test.com");

        when(objectMapper.readValue(message, TicketPurchaseRequestedEvent.class)).thenReturn(event);

        listener.handleTicketPurchase(message);

        verify(objectMapper).readValue(message, TicketPurchaseRequestedEvent.class);
        verify(confirmTicketPurchaseUseCase).execute(10L, "buyer@test.com");
        verifyNoMoreInteractions(objectMapper, confirmTicketPurchaseUseCase);
    }

    @Test
    @DisplayName("Should discard message (no retry) on business exception: EventSoldOutException")
    void shouldDiscardOnEventSoldOut() throws Exception {
        String message = "{\"eventId\":10,\"buyerEmail\":\"buyer@test.com\"}";
        TicketPurchaseRequestedEvent event = new TicketPurchaseRequestedEvent(10L, "buyer@test.com");

        when(objectMapper.readValue(message, TicketPurchaseRequestedEvent.class)).thenReturn(event);
        doThrow(new EventSoldOutException("Event Test"))
                .when(confirmTicketPurchaseUseCase).execute(10L, "buyer@test.com");

        assertDoesNotThrow(() -> listener.handleTicketPurchase(message));

        verify(objectMapper).readValue(message, TicketPurchaseRequestedEvent.class);
        verify(confirmTicketPurchaseUseCase).execute(10L, "buyer@test.com");
        verifyNoMoreInteractions(objectMapper, confirmTicketPurchaseUseCase);
    }

    @Test
    @DisplayName("Should discard message (no retry) on business exception: EventByIdNotFoundException")
    void shouldDiscardOnEventNotFound() throws Exception {
        String message = "{\"eventId\":99,\"buyerEmail\":\"buyer@test.com\"}";
        TicketPurchaseRequestedEvent event = new TicketPurchaseRequestedEvent(99L, "buyer@test.com");

        when(objectMapper.readValue(message, TicketPurchaseRequestedEvent.class)).thenReturn(event);
        doThrow(new EventByIdNotFoundException(99L))
                .when(confirmTicketPurchaseUseCase).execute(99L, "buyer@test.com");

        assertDoesNotThrow(() -> listener.handleTicketPurchase(message));

        verify(objectMapper).readValue(message, TicketPurchaseRequestedEvent.class);
        verify(confirmTicketPurchaseUseCase).execute(99L, "buyer@test.com");
        verifyNoMoreInteractions(objectMapper, confirmTicketPurchaseUseCase);
    }

    @Test
    @DisplayName("Should discard message (no retry) on business exception: UserByEmailNotFoundException")
    void shouldDiscardOnUserNotFound() throws Exception {
        String message = "{\"eventId\":10,\"buyerEmail\":\"missing@test.com\"}";
        TicketPurchaseRequestedEvent event = new TicketPurchaseRequestedEvent(10L, "missing@test.com");

        when(objectMapper.readValue(message, TicketPurchaseRequestedEvent.class)).thenReturn(event);
        doThrow(new UserByEmailNotFoundException("missing@test.com"))
                .when(confirmTicketPurchaseUseCase).execute(10L, "missing@test.com");

        assertDoesNotThrow(() -> listener.handleTicketPurchase(message));

        verify(objectMapper).readValue(message, TicketPurchaseRequestedEvent.class);
        verify(confirmTicketPurchaseUseCase).execute(10L, "missing@test.com");
        verifyNoMoreInteractions(objectMapper, confirmTicketPurchaseUseCase);
    }

    @Test
    @DisplayName("Should rethrow exception (retry) when JSON parsing fails")
    void shouldRethrowWhenJsonParsingFails() throws Exception {
        String message = "invalid-json";

        when(objectMapper.readValue(message, TicketPurchaseRequestedEvent.class))
                .thenThrow(new RuntimeException("JSON parse error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> listener.handleTicketPurchase(message));

        assertEquals("JSON parse error", ex.getMessage());

        verify(objectMapper).readValue(message, TicketPurchaseRequestedEvent.class);
        verifyNoInteractions(confirmTicketPurchaseUseCase);
    }

    @Test
    @DisplayName("Should rethrow exception (retry) on unexpected error from use case")
    void shouldRethrowOnUnexpectedError() throws Exception {
        String message = "{\"eventId\":10,\"buyerEmail\":\"buyer@test.com\"}";
        TicketPurchaseRequestedEvent event = new TicketPurchaseRequestedEvent(10L, "buyer@test.com");

        when(objectMapper.readValue(message, TicketPurchaseRequestedEvent.class)).thenReturn(event);
        doThrow(new RuntimeException("DB timeout"))
                .when(confirmTicketPurchaseUseCase).execute(10L, "buyer@test.com");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> listener.handleTicketPurchase(message));

        assertEquals("DB timeout", ex.getMessage());

        verify(objectMapper).readValue(message, TicketPurchaseRequestedEvent.class);
        verify(confirmTicketPurchaseUseCase).execute(10L, "buyer@test.com");
    }
}
