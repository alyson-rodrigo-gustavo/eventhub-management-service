package br.com.alysongustavo.eventhubmanagementservice.domain.event.service;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.exception.EventSoldOutException;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.service.EventCapacityCheckerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventCapacityCheckerServiceTest {

    private final EventCapacityCheckerService service = new EventCapacityCheckerService();

    @ParameterizedTest(name = "capacity={0}, committed={1}, requested={2} -> shouldThrow={3}")
    @CsvSource({
            // remaining = capacity - committed
            "100, 100, 1, true",   // remaining=0, requested=1 -> sold out
            "100, 90, 11, true",   // remaining=10, requested=11 -> sold out
            "50,  49, 2, true",    // remaining=1, requested=2 -> sold out
            "10,  0,  11, true"    // remaining=10, requested=11 -> sold out
    })
    @DisplayName("Should throw EventSoldOutException when requested tickets exceed remaining capacity")
    void shouldThrowWhenRequestedExceedsRemaining(int capacity, int alreadyCommitted, int requested, boolean shouldThrow) {

        Event event = new Event();
        event.setName("Tech Conference");
        event.setCapacity(capacity);

        if (shouldThrow) {
            assertThrows(EventSoldOutException.class,
                    () -> service.ensureCapacityAvailable(event, alreadyCommitted, requested));
        }
    }

    @ParameterizedTest(name = "capacity={0}, committed={1}, requested={2} -> shouldPass={3}")
    @CsvSource({
            "100, 0,  1,  true",   // remaining=100, requested=1 -> ok
            "100, 90, 10, true",   // remaining=10, requested=10 -> ok (boundary)
            "50,  49, 1,  true",   // remaining=1, requested=1 -> ok (boundary)
            "10,  5,  0,  true"    // remaining=5, requested=0 -> ok
    })
    @DisplayName("Should not throw when requested tickets are within remaining capacity")
    void shouldNotThrowWhenRequestedWithinCapacity(int capacity, int alreadyCommitted, int requested, boolean shouldPass) {

        Event event = new Event();
        event.setName("Tech Conference");
        event.setCapacity(capacity);

        if (shouldPass) {
            assertDoesNotThrow(() -> service.ensureCapacityAvailable(event, alreadyCommitted, requested));
        }
    }
}