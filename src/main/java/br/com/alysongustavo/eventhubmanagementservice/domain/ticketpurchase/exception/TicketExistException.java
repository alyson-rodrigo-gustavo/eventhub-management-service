package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class TicketExistException extends BusinessException {

    public TicketExistException(String name)
    {
        super("TICKET_ALREADY_EXISTS", name);
    }
}