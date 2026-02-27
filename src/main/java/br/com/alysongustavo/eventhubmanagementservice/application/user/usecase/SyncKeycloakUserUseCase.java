package br.com.alysongustavo.eventhubmanagementservice.application.user.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.SyncKeycloakUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.IamPort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.event.UserRegisteredEvent;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SyncKeycloakUserUseCase implements SyncKeycloakUserUseCasePort {

    private final IamPort iamPort;
    private final UserRepositoryPort userRepository;

    @Override
    public void syncUserWithKeycloak(UserRegisteredEvent event) {
        User user = User.builder()
                .id(event.getUserId())
                .email(event.getEmail())
                .name(event.getName())
                .role(event.getRole())
                .userType(event.getType())
                .build();

        String keycloakId = iamPort.createUser(user, event.getRole());
        user.setKeycloakId(keycloakId);

        userRepository.save(user);
    }
}
