package br.com.alysongustavo.eventhubmanagementservice.application.user.port.out;

public interface EventPublisherPort {
    void publish(String eventType, Object event);
}
