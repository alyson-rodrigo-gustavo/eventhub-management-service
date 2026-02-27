package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound.TicketRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service.TicketAvailabilityService;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service.TicketPurchaseAuthorizationService;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserByEmailNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmTicketPurchaseUseCase {

    private final EventRepositoryPort eventRepository;
    private final UserRepositoryPort userRepository;
    private final TicketRepositoryPort ticketRepository;

    private final TicketAvailabilityService ticketAvailabilityService;
    private final TicketPurchaseAuthorizationService ticketPurchaseAuthorizationService;

    @Transactional
    public void execute(Long eventId, String buyerEmail) {

        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventByIdNotFoundException(eventId));

        var user = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new UserByEmailNotFoundException(buyerEmail));

        ticketPurchaseAuthorizationService.validateBuyer(user);

        long sold = ticketRepository.countByEventId(eventId);

        ticketAvailabilityService.validateAvailability(event, sold);

        ticketRepository.save(new Ticket(null, eventId, user.getId()));
    }
}
