package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.event.mapper.EventMapper;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;
    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EditEventUseCase editEventUseCase;

    private CreateEventCommand command;

    private final Long id = 1L;

    @BeforeEach
    public void setup(){

        this.command = new CreateEventCommand(
                "Event Test",
                LocalDate.now(),
                "Test Location",
                100
        );
    }

    @Test
    @DisplayName("Should throw EventNotFoundException when eventhub does not exist")
    public void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist(){
        // Simulando que o event não foi encontrado
        when(eventRepositoryPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(EventByIdNotFoundException.class,
                () -> editEventUseCase.execute(command, id));

        verify(eventRepositoryPort).findById(id);
        verifyNoMoreInteractions(eventRepositoryPort);
    }


    @Test
    @DisplayName("Should update event when all validations pass")
    public void shouldUpdateEventWhenAllValidationsPass() {

        Event existing = new Event(
                id,
                "Old Name",
                LocalDate.now().minusDays(1),
                "Old Location",
                50
        );

        when(eventRepositoryPort.findById(id)).thenReturn(Optional.of(existing));

        Event mappedEvent = new Event(
                id,
                command.name(),
                command.date(),
                command.location(),
                100
        );


        when(eventMapper.toDomain(any())).thenReturn(mappedEvent);
        when(eventRepositoryPort.update(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));

        editEventUseCase.execute(command, id);

        verify(eventRepositoryPort).update(any(), eq(id));
    }
}
