package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound;

import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;

import java.util.List;

public interface TicketRepositoryPort {
    long countByEventId(Long eventId);
    Ticket save(Ticket ticket);
    List<Ticket> findByUserId(Long userId);
}
