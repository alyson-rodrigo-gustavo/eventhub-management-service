package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FindByIdEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public Event execute(Long id) {
        return eventRepositoryPort.findById(id)
                .orElseThrow(() -> new EventByIdNotFoundException(id));
    }
}
