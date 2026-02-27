package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;

public class TicketAvailabilityService {

    public void validateAvailability(Event event, long soldTickets) {
        if (soldTickets >= event.getCapacity()) {
            throw new EventSoldOutException(event.getName());
        }
    }

    public boolean isSoldOut(Event event, long soldTickets) {
        return soldTickets >= event.getCapacity();
    }
}
