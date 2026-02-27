package br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {

    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    Event update(Event event, Long id);
    void delete(Long id);
    Optional<Event> findByName(String name);
    boolean existsByName(String name);
}
