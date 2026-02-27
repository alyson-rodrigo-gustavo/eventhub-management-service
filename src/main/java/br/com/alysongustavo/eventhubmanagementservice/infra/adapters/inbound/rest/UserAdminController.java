package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.RegisterUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final RegisterUserUseCasePort registerUserUseCase;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerUserAdmin(@Valid @RequestBody RegisterUserRequest request) {
        User registeredUser = registerUserUseCase.registerUser(request.getName(), request.getEmail(), UserType.ADMIN, "ADMIN");
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerUserManager(@Valid @RequestBody RegisterUserRequest request) {
        User registeredUser = registerUserUseCase.registerUser(request.getName(), request.getEmail(), UserType.MANAGER, "MANAGER");
        return ResponseEntity.ok(registeredUser);
    }
}
