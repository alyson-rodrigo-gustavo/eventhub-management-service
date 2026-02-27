package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class EventExistException extends BusinessException {

    public EventExistException(String name)
    {
        super("EVENT_ALREADY_EXISTS", name);
    }
}