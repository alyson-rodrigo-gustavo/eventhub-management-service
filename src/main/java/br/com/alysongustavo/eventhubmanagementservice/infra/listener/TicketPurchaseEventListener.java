package br.com.alysongustavo.eventhubmanagementservice.infra.listener;

import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.event.TicketPurchaseRequestedEvent;
import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.ConfirmTicketPurchaseUseCase;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserByEmailNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketPurchaseEventListener {

    private final ConfirmTicketPurchaseUseCase confirmTicketPurchaseUseCase;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "ticket.purchase.queue")
    public void handleTicketPurchase(String message) throws JsonProcessingException {
        try {
            TicketPurchaseRequestedEvent event = objectMapper.readValue(message, TicketPurchaseRequestedEvent.class);

            confirmTicketPurchaseUseCase.execute(event.getEventId(), event.getBuyerEmail());

            log.info("Ticket purchase processed: eventId={} buyer={}", event.getEventId(), event.getBuyerEmail());

        } catch (EventSoldOutException | EventByIdNotFoundException | UserByEmailNotFoundException e) {
            log.warn("Discarding message (business rule): {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing message (will retry): {}", e.getMessage(), e);
            throw e;
        }
    }
}
