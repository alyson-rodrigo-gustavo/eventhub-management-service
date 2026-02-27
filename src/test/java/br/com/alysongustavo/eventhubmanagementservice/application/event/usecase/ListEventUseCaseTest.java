package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @InjectMocks
    private ListEventUseCase listEventUseCase;

    @Test
    @DisplayName("Should return all event")
    public void shouldReturnAllEvents() {
        List<Event> employeeList = List.of(
                new Event(1L, "Event test 1", LocalDate.now().plusDays(2), "Test location 1", 100),
                new Event(2L, "Event test 2", LocalDate.now().plusDays(4), "Test location 1", 200),
                new Event(3L, "Event test 3", LocalDate.now().plusDays(6), "Test location 1", 300)
        );

        when(eventRepositoryPort.findAll()).thenReturn(employeeList);

        List<Event> events = listEventUseCase.execute();

        assertEquals(employeeList, events);
        assertEquals(3, events.size());
        verify(eventRepositoryPort).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no event exist")
    public void shouldReturnEmptyListWhenNoEventsExist() {
        when(eventRepositoryPort.findAll()).thenReturn(List.of());

        List<Event> events = listEventUseCase.execute();

        assertNotNull(events);
        assertTrue(events.isEmpty());
        verify(eventRepositoryPort).findAll();
    }
}
