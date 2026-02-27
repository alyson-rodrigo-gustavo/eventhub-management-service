package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase;

import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound.TicketRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListParticipantTicketsUseCase {

    private final TicketRepositoryPort ticketRepositoryPort;

    public List<Ticket> listTicketsByUser(String userEmail) {

        List<Ticket> tickets = ticketRepositoryPort.findByUserEmail(userEmail);
        return tickets;
    }
}
