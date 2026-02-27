package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.event.mapper.EventMapper;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventExistException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterEventUseCaseTest {


    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private RegisterEventUseCase registerEventUseCase;

    private CreateEventCommand command;

    @BeforeEach
    public void setup(){

        this.command = new CreateEventCommand(
                "Event test",
                LocalDate.now().plusMonths(1),
                "Location test",
                300
        );


    }

    @Test
    @DisplayName("Should throw EventExistException when name already exists")
    public void shouldThrowEventExistExceptionWhenEventAlreadyExists() {
        when(eventRepositoryPort.existsByName(command.name())).thenReturn(true);

        assertThrows(EventExistException.class, () -> registerEventUseCase.execute(command));

        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should register event successfully when all validations pass")
    public void shouldRegisterEventSuccessfullyWhenAllValidationsPass() {
        Event mappedEvent = new Event(
                null,
                command.name(),
                command.date(),
                command.location(),
                command.capacity()
        );

        when(eventRepositoryPort.existsByName(command.name())).thenReturn(false);
        when(eventMapper.toDomain(command)).thenReturn(mappedEvent);
        when(eventRepositoryPort.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        registerEventUseCase.execute(command);

        verify(eventRepositoryPort).save(mappedEvent);
        verify(eventMapper).toRegisterEventResult(mappedEvent);
    }
}
