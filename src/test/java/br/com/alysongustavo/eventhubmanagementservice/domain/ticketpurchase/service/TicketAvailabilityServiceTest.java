package br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.service;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TicketAvailabilityServiceTest {

    private final TicketAvailabilityService service = new TicketAvailabilityService();

    @Test
    @DisplayName("Should return true when soldTickets is equal to capacity")
    void shouldBeSoldOutWhenSoldEqualsCapacity() {
        Event event = new Event(1L, "Event", LocalDate.now(), "Loc", 10);
        assertTrue(service.isSoldOut(event, 10));
    }

    @Test
    @DisplayName("Should return true when soldTickets is greater than capacity")
    void shouldBeSoldOutWhenSoldGreaterThanCapacity() {
        Event event = new Event(1L, "Event", LocalDate.now(), "Loc", 10);
        assertTrue(service.isSoldOut(event, 11));
    }

    @Test
    @DisplayName("Should return false when soldTickets is less than capacity")
    void shouldNotBeSoldOutWhenSoldLessThanCapacity() {
        Event event = new Event(1L, "Event", LocalDate.now(), "Loc", 10);
        assertFalse(service.isSoldOut(event, 9));
    }

    @Test
    @DisplayName("Should throw EventSoldOutException when soldTickets is >= capacity")
    void shouldThrowWhenSoldOut() {
        Event event = new Event(1L, "Event", LocalDate.now(), "Loc", 10);

        assertThrows(EventSoldOutException.class,
                () -> service.validateAvailability(event, 10));
    }

    @Test
    @DisplayName("Should not throw when soldTickets is < capacity")
    void shouldNotThrowWhenHasCapacity() {
        Event event = new Event(1L, "Event", LocalDate.now(), "Loc", 10);

        assertDoesNotThrow(
                () -> service.validateAvailability(event, 9));
    }
}
