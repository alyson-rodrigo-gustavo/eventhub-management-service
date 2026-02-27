package br.com.alysongustavo.eventhubmanagementservice.domain.event.exception;

import br.com.alysongustavo.eventhubmanagementservice.domain.shared.exception.BusinessException;

public class EventSoldOutException extends BusinessException {

    public EventSoldOutException(String name) {
        super(
                "EVENT_SOLD_OUT",
                name
        );
    }
}