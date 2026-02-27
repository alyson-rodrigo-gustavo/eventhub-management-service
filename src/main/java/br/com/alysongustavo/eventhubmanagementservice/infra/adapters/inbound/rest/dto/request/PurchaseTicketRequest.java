package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseTicketRequest {

    @NotNull(message = "eventId is required")
    private Long eventId;
}
