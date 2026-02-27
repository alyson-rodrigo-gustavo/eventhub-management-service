
package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper;

import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(target = "event.id", source = "eventId")
    @Mapping(target = "user.id", source = "participantId")
    TicketEntity toEntity(Ticket ticket);

    // Para o Entity -> Domain
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "participantId", source = "user.id")
    Ticket toDomain(TicketEntity ticketEntity);
}
