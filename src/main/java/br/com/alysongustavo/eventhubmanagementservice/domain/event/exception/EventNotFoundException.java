package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class EventNotFoundException extends BusinessException {

    public EventNotFoundException(String name)
    {
        super("EVENT_NOT_FOUND_BY_NAME", name);
    }
}
