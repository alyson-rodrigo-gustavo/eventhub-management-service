package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.ListParticipantByIdTicketsUseCase;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.RegisterUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.input.CreateUserCommand;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterUserRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.TicketResponse;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.UserResponse;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper.TicketRestMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper.UserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final RegisterUserUseCasePort registerUserUseCase;
    private final ListParticipantByIdTicketsUseCase listParticipantByIdTicketsUseCase;
    private final UserRestMapper userRestMapper;
    private final TicketRestMapper ticketRestMapper;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> registerUserAdmin(@Valid @RequestBody RegisterUserRequest request) {

        CreateUserCommand createUserCommand  = new CreateUserCommand(
                request.getName(),
                request.getEmail(),
                UserType.ADMIN,
                "ADMIN"
        );

        var registeredUser = registerUserUseCase.registerUser(createUserCommand);
        return ResponseEntity.ok(userRestMapper.toUserResponse(registeredUser));
    }

    @PostMapping("/manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> registerUserManager(@Valid @RequestBody RegisterUserRequest request) {

        CreateUserCommand createUserCommand  = new CreateUserCommand(
                request.getName(),
                request.getEmail(),
                UserType.MANAGER,
                "MANAGER"
        );

        var registeredUser = registerUserUseCase.registerUser(createUserCommand);
        return ResponseEntity.ok(userRestMapper.toUserResponse(registeredUser));
    }

    @GetMapping("/{userId}/tickets")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<TicketResponse> listTicketsByUserId(@PathVariable Long userId) {
        return listParticipantByIdTicketsUseCase.listTicketsByUserId(userId)
                .stream()
                .map(ticketRestMapper::toTicketResponse)
                .toList();
    }

}
