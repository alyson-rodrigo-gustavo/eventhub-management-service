package br.com.alysongustavo.eventhubmanagementservice.application.user.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.user.mapper.UserMapper;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.RegisterUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.input.CreateUserCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.output.RegisterUserResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.event.UserRegisteredEvent;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RegisterUserUseCase implements RegisterUserUseCasePort {

    private final UserRepositoryPort userRepository;
    private final EventPublisherPort eventPublisher;
    private final UserMapper userMapper;

    @Override
    public RegisterUserResult registerUser(CreateUserCommand createUserCommand) {
        User user = User.builder()
                .name(createUserCommand.name())
                .email(createUserCommand.email())
                .userType(createUserCommand.userType())
                .role(createUserCommand.role())
                .build();

        User savedUser = userRepository.save(user);

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .type(savedUser.getUserType())
                .role(savedUser.getRole())
                .build();

        eventPublisher.publish("user.registered", event);

        return userMapper.toRegisterUserResult(savedUser);
    }
}
