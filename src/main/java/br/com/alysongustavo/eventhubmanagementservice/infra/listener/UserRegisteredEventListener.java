package br.com.alysongustavo.eventhubmanagementservice.infra.listener;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.SyncKeycloakUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.event.UserRegisteredEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final SyncKeycloakUserUseCasePort syncKeycloakUserUseCase;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "user.registered.queue")
    public void handleUserRegistered(String message) {
        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
            log.info("Processing UserRegisteredEvent for user: {}", event.getEmail());
            syncKeycloakUserUseCase.syncUserWithKeycloak(event);
        } catch (Exception e) {
            log.error("Error processing UserRegisteredEvent", e);
        }
    }
}
