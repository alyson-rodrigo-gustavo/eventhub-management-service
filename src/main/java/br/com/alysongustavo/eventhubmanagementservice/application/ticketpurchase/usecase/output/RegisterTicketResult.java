package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.output;

public record RegisterTicketResult(
        Long id,
        Long eventId,
        String eventName,
        Long participantId,
        String participantName
) {}