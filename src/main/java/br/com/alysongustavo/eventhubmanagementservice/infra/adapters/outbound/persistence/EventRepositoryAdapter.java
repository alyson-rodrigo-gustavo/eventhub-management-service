package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper.EventJpaMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventJpaRepository eventJpaRepository;
    private final EventJpaMapper eventJpaMapper;

    @Override
    public Event save(Event eventhub) {
        EventEntity eventEntity = eventJpaRepository.save(eventJpaMapper.toEntity(eventhub));
        return eventJpaMapper.toDomain(eventEntity);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventJpaRepository.findById(id)
                .map(eventJpaMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventJpaRepository.findAll()
                .stream()
                .map(eventJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Event> findByName(String name) {
        return eventJpaRepository.findByName(name)
                .map(eventJpaMapper::toDomain);
    }

    @Override
    public Event update(Event eventhub, Long id) {
        EventEntity current = eventJpaRepository.findById(id)
                .orElseThrow(() -> new EventByIdNotFoundException(id));

        BeanUtils.copyProperties(eventhub, current, "id");
        EventEntity saved = eventJpaRepository.save(current);
        return eventJpaMapper.toDomain(saved);
    }

    @Override
    public void delete(Long id) {
        EventEntity entity = eventJpaRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id %d not found", id)));

        eventJpaRepository.delete(entity);
    }

    @Override
    public boolean existsByName(String name) {
        return eventJpaRepository.findByName(name).isPresent();
    }
}
