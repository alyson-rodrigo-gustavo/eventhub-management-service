package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteEventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public void execute(Long id) {
        this.eventRepositoryPort.findById(id)
                .orElseThrow(() -> new EventByIdNotFoundException(id));

        this.eventRepositoryPort.delete(id);
    }
}
