package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class EventByIdNotFoundException extends BusinessException {

    public EventByIdNotFoundException(Long id)
    {
        super("EVENT_NOT_FOUND", id);
    }
}
