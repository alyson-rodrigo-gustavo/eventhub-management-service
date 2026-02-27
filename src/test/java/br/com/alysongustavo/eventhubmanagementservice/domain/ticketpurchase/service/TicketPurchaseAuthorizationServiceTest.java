package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.exception.UserNotAllowedToPurchaseTicketException;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketPurchaseAuthorizationServiceTest {

    private final TicketPurchaseAuthorizationService service = new TicketPurchaseAuthorizationService();

    @Test
    @DisplayName("Should allow purchase when user is PARTICIPANT")
    void shouldAllowWhenParticipant() {
        User user = new User(1L, "Buyer", "buyer@test.com", null, "USER", UserType.PARTICIPANT);

        assertTrue(service.canPurchase(user));
        assertDoesNotThrow(() -> service.validateBuyer(user));
    }

    @Test
    @DisplayName("Should deny purchase when user is not PARTICIPANT")
    void shouldDenyWhenNotParticipant() {
        User user = new User(2L, "Admin", "admin@test.com", null, "ADMIN", UserType.ADMIN);

        assertFalse(service.canPurchase(user));
        assertThrows(UserNotAllowedToPurchaseTicketException.class,
                () -> service.validateBuyer(user));
    }
}