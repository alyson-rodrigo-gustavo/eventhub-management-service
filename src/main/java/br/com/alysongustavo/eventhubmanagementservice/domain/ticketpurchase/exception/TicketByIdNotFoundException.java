package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class TicketByIdNotFoundException extends BusinessException {

    public TicketByIdNotFoundException(Long id)
    {
        super("TICKET_NOT_FOUND_BY_ID", id);
    }
}
