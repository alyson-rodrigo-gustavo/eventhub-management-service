package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.event.TicketPurchaseRequestedEvent;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.PurchaseTicketRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class TicketPurchaseController {

    private final EventPublisherPort eventPublisherPort;

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> purchaseTicket(
            @Valid @RequestBody PurchaseTicketRequest request,
            Authentication authentication
    ) {

        String buyerEmail = authentication.getName();

        TicketPurchaseRequestedEvent event =
                new TicketPurchaseRequestedEvent(
                        request.getEventId(),
                        buyerEmail
                );

        eventPublisherPort.publish("ticket.purchase", event);

        return ResponseEntity.accepted().build();
    }
}