package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.UserRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventByIdNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.port.outbound.EventRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.port.outbound.TicketRepositoryPort;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service.TicketAvailabilityService;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service.TicketPurchaseAuthorizationService;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserByEmailNotFoundException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserNotAllowedToPurchaseTicketException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ConfirmTicketPurchaseUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;
    @Mock
    private UserRepositoryPort userRepository;
    @Mock
    private TicketRepositoryPort ticketRepository;

    // Services (vamos mockar para testar orquestração do use case)
    @Mock
    private TicketAvailabilityService ticketAvailabilityService;
    @Mock
    private TicketPurchaseAuthorizationService ticketPurchaseAuthorizationService;

    @InjectMocks
    private ConfirmTicketPurchaseUseCase useCase;

    private final Long eventId = 10L;
    private final String buyerEmail = "buyer@test.com";

    private Event event;
    private User participantUser;

    @BeforeEach
    void setup() {
        event = new Event(eventId, "Event Test", LocalDate.now(), "Test Location", 100);
        participantUser = new User(1L, "Buyer", buyerEmail, null, "USER", UserType.PARTICIPANT);
    }

    @Test
    @DisplayName("Should throw EventByIdNotFoundException when event does not exist")
    void shouldThrowEventNotFoundWhenEventDoesNotExist() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventByIdNotFoundException.class,
                () -> useCase.execute(eventId, buyerEmail));

        verify(eventRepository).findById(eventId);
        verifyNoInteractions(userRepository, ticketRepository, ticketAvailabilityService, ticketPurchaseAuthorizationService);
    }

    @Test
    @DisplayName("Should throw UserByEmailNotFoundException when user does not exist")
    void shouldThrowUserNotFoundWhenUserDoesNotExist() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail(buyerEmail)).thenReturn(Optional.empty());

        assertThrows(UserByEmailNotFoundException.class,
                () -> useCase.execute(eventId, buyerEmail));

        verify(eventRepository).findById(eventId);
        verify(userRepository).findByEmail(buyerEmail);
        verifyNoInteractions(ticketRepository, ticketAvailabilityService, ticketPurchaseAuthorizationService);
    }

    @Test
    @DisplayName("Should throw UserNotAllowedToPurchaseTicketException when user is not PARTICIPANT")
    void shouldThrowWhenUserNotParticipant() {
        User adminUser = new User(2L, "Admin", buyerEmail, null, "ADMIN", UserType.ADMIN);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail(buyerEmail)).thenReturn(Optional.of(adminUser));

        doThrow(new UserNotAllowedToPurchaseTicketException(adminUser.getName()))
                .when(ticketPurchaseAuthorizationService).validateBuyer(adminUser);

        assertThrows(UserNotAllowedToPurchaseTicketException.class,
                () -> useCase.execute(eventId, buyerEmail));

        verify(eventRepository).findById(eventId);
        verify(userRepository).findByEmail(buyerEmail);
        verify(ticketPurchaseAuthorizationService).validateBuyer(adminUser);

        verifyNoMoreInteractions(ticketPurchaseAuthorizationService);
        verifyNoInteractions(ticketAvailabilityService);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw EventSoldOutException when event is sold out")
    void shouldThrowSoldOutWhenNoCapacity() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail(buyerEmail)).thenReturn(Optional.of(participantUser));
        when(ticketRepository.countByEventId(eventId)).thenReturn(100L);

        doNothing().when(ticketPurchaseAuthorizationService).validateBuyer(participantUser);
        doThrow(new EventSoldOutException(event.getName()))
                .when(ticketAvailabilityService).validateAvailability(event, 100L);

        assertThrows(EventSoldOutException.class,
                () -> useCase.execute(eventId, buyerEmail));

        verify(eventRepository).findById(eventId);
        verify(userRepository).findByEmail(buyerEmail);
        verify(ticketPurchaseAuthorizationService).validateBuyer(participantUser);
        verify(ticketRepository).countByEventId(eventId);
        verify(ticketAvailabilityService).validateAvailability(event, 100L);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save ticket when validations pass")
    void shouldSaveTicketWhenValid() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail(buyerEmail)).thenReturn(Optional.of(participantUser));
        when(ticketRepository.countByEventId(eventId)).thenReturn(10L);

        doNothing().when(ticketPurchaseAuthorizationService).validateBuyer(participantUser);
        doNothing().when(ticketAvailabilityService).validateAvailability(event, 10L);

        useCase.execute(eventId, buyerEmail);

        verify(eventRepository).findById(eventId);
        verify(userRepository).findByEmail(buyerEmail);
        verify(ticketPurchaseAuthorizationService).validateBuyer(participantUser);
        verify(ticketRepository).countByEventId(eventId);
        verify(ticketAvailabilityService).validateAvailability(event, 10L);

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketCaptor.capture());

        Ticket savedTicket = ticketCaptor.getValue();
        assertNull(savedTicket.getId());
        assertEquals(eventId, savedTicket.getEventId());
        assertEquals(participantUser.getId(), savedTicket.getParticipantId());
    }
}
