package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.event.mapper.EventMapper;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EditEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    private final EventMapper eventMapper;

    public RegisterEventResult execute(CreateEventCommand command, Long id) {

        if (eventRepositoryPort.findById(id).isEmpty()) {
            throw new EventByIdNotFoundException(id);
        }

        Event event = eventMapper.toDomain(command);
        Event saved = eventRepositoryPort.update(event, id);
        return eventMapper.toRegisterEventResult(saved);
    }
}
