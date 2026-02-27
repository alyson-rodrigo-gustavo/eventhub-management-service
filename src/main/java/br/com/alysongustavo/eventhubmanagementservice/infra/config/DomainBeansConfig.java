package br.com.alysongustavo.eventhubmanagementservice.infra.config;

import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.ListParticipantByIdTicketsUseCase;
import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.ListParticipantTicketsUseCase;
import br.com.alysongustavo.eventhubmanagementservice.application.user.mapper.UserMapper;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.IamPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.RegisterUserUseCase;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.SyncKeycloakUserUseCase;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.service.EventCapacityCheckerService;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound.TicketRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service.TicketAvailabilityService;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service.TicketPurchaseAuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DomainBeansConfig {

    @Bean
    public EventCapacityCheckerService validateEventCapacityCheckerService() {
        return new EventCapacityCheckerService();
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepositoryPort userRepository,
            EventPublisherPort eventPublisher,
            UserMapper userMapper) {
        return new RegisterUserUseCase(userRepository, eventPublisher, userMapper);
    }

    @Bean
    public SyncKeycloakUserUseCase syncKeycloakUserUseCase(
            IamPort iamPort,
            UserRepositoryPort userRepository) {
        return new SyncKeycloakUserUseCase(iamPort, userRepository);
    }

    @Bean
    public ListParticipantTicketsUseCase listParticipantTicketsUseCase(TicketRepositoryPort ticketRepositoryPort) {
        return new ListParticipantTicketsUseCase(ticketRepositoryPort);
    }

    @Bean
    public ListParticipantByIdTicketsUseCase listParticipantByIdTicketsUseCase(TicketRepositoryPort ticketRepositoryPort) {
        return new ListParticipantByIdTicketsUseCase(ticketRepositoryPort);
    }

    @Bean
    public TicketAvailabilityService eventCapacityCheckerService() {
        return new TicketAvailabilityService();
    }

    @Bean
    public TicketPurchaseAuthorizationService ticketPurchaseAuthorizationService() {
        return new TicketPurchaseAuthorizationService();
    }



}
