package br.com.alysongustavo.eventhubmanagementservice.infra.config;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.IamPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.RegisterUserUseCase;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.SyncKeycloakUserUseCase;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.service.EventCapacityCheckerService;
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
            EventPublisherPort eventPublisher) {
        return new RegisterUserUseCase(userRepository, eventPublisher);
    }

    @Bean
    public SyncKeycloakUserUseCase syncKeycloakUserUseCase(
            IamPort iamPort,
            UserRepositoryPort userRepository) {
        return new SyncKeycloakUserUseCase(iamPort, userRepository);
    }

}
