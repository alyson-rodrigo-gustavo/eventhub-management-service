package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence;

import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound.TicketRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.TicketEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper.EventJpaMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper.TicketMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.TicketJpaRepository;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final TicketJpaRepository ticketJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final EventJpaRepository eventJpaRepository;

    private final TicketMapper ticketMapper;

    @Override
    public long countByEventId(Long eventId) {
        return ticketJpaRepository.countByEventId(eventId);
    }

    @Override
    public Ticket save(Ticket ticket) {
        TicketEntity entity = new TicketEntity();

        entity.setEvent(eventJpaRepository.getReferenceById(ticket.getEventId()));
        entity.setUser(userJpaRepository.getReferenceById(ticket.getParticipantId()));

        TicketEntity saved = ticketJpaRepository.save(entity);
        return new Ticket(saved.getId(), saved.getId(), saved.getUser().getId());
    }

    @Override
    public List<Ticket> findByUserId(Long userId) {
        return ticketJpaRepository.findByUserId(userId).stream()
                .map(t -> new Ticket(
                        t.getId(),
                        t.getEvent().getId(),
                        t.getUser().getId()
                ))
                .toList();
    }

    @Override
    public List<Ticket> findByUserEmail(String email) {
        return ticketJpaRepository.findByUserEmail(email)
                .stream()
                .map(ticketMapper::toDomain)
                .toList();
    }
}
