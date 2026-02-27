package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketPurchaseRequestedEvent {

    private Long eventId;
    private String buyerEmail;
}
