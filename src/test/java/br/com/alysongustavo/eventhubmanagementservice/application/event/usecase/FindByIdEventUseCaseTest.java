package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindByIdEventUseCaseTest {

    private static final Long POLICY_ID = 1L;

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @InjectMocks
    private FindByIdEventUseCase findByIdEventUseCase;

    @Test
    @DisplayName("Should throw EventByIdNotFoundException when event is not found")
    public void shouldThrowEventByIdNotFoundExceptionWhenEventIsNotFound() {

        when(eventRepositoryPort.findById(POLICY_ID)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                EventByIdNotFoundException.class,
                () -> findByIdEventUseCase.execute(POLICY_ID)
        );

        verify(eventRepositoryPort, times(1)).findById(POLICY_ID);
    }

    @Test
    @DisplayName("Should return event when event exists")
    public void shouldReturnEventWhenEventExists() {
        Long id = 1L;
        var event = new Event(
                id, "Event test", LocalDate.now(), "Test location", 100
        );

        when(eventRepositoryPort.findById(id)).thenReturn(Optional.of(event));

        var result = findByIdEventUseCase.execute(id);
        assertNotNull(result);
        assertEquals(event, result);
        verify(eventRepositoryPort, times(1)).findById(id);

    }
}

