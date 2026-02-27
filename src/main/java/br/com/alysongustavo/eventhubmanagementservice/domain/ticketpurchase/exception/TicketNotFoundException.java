package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class TicketNotFoundException extends BusinessException {

    public TicketNotFoundException(Long id)
    {
        super("TICKET_NOT_FOUND", id);
    }
}
