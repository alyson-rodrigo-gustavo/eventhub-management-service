package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper.EventJpaMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRepositoryAdapterTest {

    private static final Long EVENT_ID = 1L;

    @Mock
    private EventJpaRepository eventJpaRepository;

    @Mock
    private EventJpaMapper eventJpaMapper;

    @InjectMocks
    private EventRepositoryAdapter eventRepositoryAdapter;

    @Test
    @DisplayName("Should save event and return domain model with generated ID")
    void shouldSaveEventAndReturnDomainWithId() {
        Event inputDomain = new Event(
                null, "Event test 1", LocalDate.now().plusMonths(1), "Test location", 300);

        EventEntity entityToSave = new EventEntity(
                null, "Event test 1", LocalDate.now().plusMonths(1), "Test location", 300);

        EventEntity savedEntity = new EventEntity(
                EVENT_ID, "Event test 1", LocalDate.now().plusMonths(1), "Test location", 300);

        Event expectedDomain = new Event(
                EVENT_ID, "Event test 1", LocalDate.now().plusMonths(1), "Test location", 300);

        when(eventJpaMapper.toEntity(inputDomain)).thenReturn(entityToSave);
        when(eventJpaRepository.save(entityToSave)).thenReturn(savedEntity);
        when(eventJpaMapper.toDomain(savedEntity)).thenReturn(expectedDomain);

        Event result = eventRepositoryAdapter.save(inputDomain);

        assertNotNull(result);
        assertEquals(EVENT_ID, result.getId());
        assertEquals("Event test 1", result.getName());

        verify(eventJpaMapper).toEntity(inputDomain);
        verify(eventJpaRepository).save(entityToSave);
        verify(eventJpaMapper).toDomain(savedEntity);
        verifyNoMoreInteractions(eventJpaRepository, eventJpaMapper);
    }

    @Test
    @DisplayName("Should return true when eventhub exists by name")
    void shouldReturnTrueWhenEventExistsByDocument() {
        when(eventJpaRepository.findByName("Event test 1"))
                .thenReturn(java.util.Optional.of(new EventEntity()));

        boolean exists = eventRepositoryAdapter.existsByName("Event test 1");

        assertTrue(exists);
        verify(eventJpaRepository).findByName("Event test 1");
    }

    @Test
    @DisplayName("Should return false when event does not exist by document")
    void shouldReturnFalseWhenEventDoesNotExistByDocument() {
        when(eventJpaRepository.findByName("Event test 1"))
                .thenReturn(java.util.Optional.empty());

        boolean exists = eventRepositoryAdapter.existsByName("Event test 1");

        assertFalse(exists);
        verify(eventJpaRepository).findByName("Event test 1");
    }
}