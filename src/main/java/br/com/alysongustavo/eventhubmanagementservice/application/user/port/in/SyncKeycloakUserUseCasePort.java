package br.com.alysongustavo.eventhubmanagementservice.application.user.port.in;


import br.com.alysongustavo.eventhubmanagementservice.domain.user.event.UserRegisteredEvent;

public interface SyncKeycloakUserUseCasePort {
    void syncUserWithKeycloak(UserRegisteredEvent event);
}
