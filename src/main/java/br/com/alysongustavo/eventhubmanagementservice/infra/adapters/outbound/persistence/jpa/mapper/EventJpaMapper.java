package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventJpaMapper {

    EventEntity toEntity(Event event);
    Event toDomain(EventEntity entity);
}
