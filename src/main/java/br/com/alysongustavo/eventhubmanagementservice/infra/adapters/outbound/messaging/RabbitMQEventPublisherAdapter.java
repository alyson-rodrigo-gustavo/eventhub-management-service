package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.messaging;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class RabbitMQEventPublisherAdapter implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(String eventType, Object event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend("events-exchange", eventType, message);
        } catch (Exception e) {
            throw new RuntimeException("Error publishing event: " + eventType, e);
        }
    }
}
