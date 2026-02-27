package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.ListParticipantTicketsUseCase;
import br.com.alysongustavo.eventhubmanagementservice.application.user.port.in.RegisterUserUseCasePort;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.input.CreateUserCommand;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterUserRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.TicketResponse;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.UserResponse;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper.UserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCasePort registerUserUseCase;
    private final ListParticipantTicketsUseCase listParticipantTicketsUseCase;
    private final UserRestMapper userRestMapper;

    @PostMapping
    public ResponseEntity<UserResponse> registerClientAdmin(@Valid @RequestBody RegisterUserRequest request) {

        CreateUserCommand createUserCommand  = new CreateUserCommand(
                request.getName(),
                request.getEmail(),
                UserType.PARTICIPANT,
                "USER"
        );

        var response = userRestMapper.toUserResponse(registerUserUseCase.registerUser(createUserCommand));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets")
    @PreAuthorize("hasRole('USER')")
    public List<TicketResponse> listMyTickets(Authentication authentication) {

        String email = authentication.getName();

        return listParticipantTicketsUseCase.listTicketsByUser(email)
                .stream()
                .map(ticket -> new TicketResponse(
                        ticket.getId(),
                        ticket.getEventId(),
                        ticket.getParticipantId()
                ))
                .toList();

    }
}
