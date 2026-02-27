package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase;

import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound.TicketRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListParticipantByIdTicketsUseCase {

    private final TicketRepositoryPort ticketRepositoryPort;

    public List<Ticket> listTicketsByUserId(Long userId) {
        return ticketRepositoryPort.findByUserId(userId);
    }
}
