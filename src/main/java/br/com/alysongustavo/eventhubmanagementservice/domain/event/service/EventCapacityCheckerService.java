package br.com.alysongustavo.eventhubmanagementservice.domain.event.service;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;

public class EventCapacityCheckerService {

    public void ensureCapacityAvailable(Event event, int alreadyCommitted, int requested) {
        int remaining = event.getCapacity() - alreadyCommitted;
        if (requested > remaining) {
            throw new EventSoldOutException(event.getName());
        }
    }
}
