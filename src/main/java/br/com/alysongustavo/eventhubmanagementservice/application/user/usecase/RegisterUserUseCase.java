package br.com.alysongustavo.eventhubmanagementservice.application.user.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.RegisterUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.event.UserRegisteredEvent;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RegisterUserUseCase implements RegisterUserUseCasePort {

    private final UserRepositoryPort userRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    public User registerUser(String name, String email, UserType userType, String role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .userType(userType)
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .type(savedUser.getUserType())
                .role(role)
                .build();

        eventPublisher.publish("user.registered", event);

        return savedUser;
    }
}
