package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long id;
    private Long eventId;
    private Long participantId;
}
