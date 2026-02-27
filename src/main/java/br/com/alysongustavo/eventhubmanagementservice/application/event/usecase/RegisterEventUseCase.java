package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.event.mapper.EventMapper;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventExistException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegisterEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    private final EventMapper eventMapper;

    public RegisterEventResult execute(CreateEventCommand command) {

        if (eventRepositoryPort.existsByName(command.name())) {
            throw new EventExistException(command.name());
        }

        Event event = eventMapper.toDomain(command);
        Event saved = eventRepositoryPort.save(event);
        return eventMapper.toRegisterEventResult(saved);
    }


}
