package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence;

import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.TicketEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.UserEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.TicketJpaRepository;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TicketRepositoryAdapterTest {

    @Mock
    private TicketJpaRepository ticketJpaRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private EventJpaRepository eventJpaRepository;

    @InjectMocks
    private TicketRepositoryAdapter adapter;

    @Test
    @DisplayName("countByEventId should delegate to TicketJpaRepository")
    void countByEventId_shouldDelegate() {
        Long eventId = 10L;
        when(ticketJpaRepository.countByEventId(eventId)).thenReturn(7L);

        long count = adapter.countByEventId(eventId);

        assertEquals(7L, count);
        verify(ticketJpaRepository).countByEventId(eventId);
        verifyNoMoreInteractions(ticketJpaRepository, userJpaRepository, eventJpaRepository);
    }

    @Test
    @DisplayName("save should set event and user references and persist ticket")
    void save_shouldSetEventAndUserReferences() {
        Long eventId = 10L;
        Long participantId = 20L;

        Ticket input = new Ticket(null, eventId, participantId);

        EventEntity eventRef = new EventEntity();
        eventRef.setId(eventId);

        UserEntity userRef = new UserEntity();
        userRef.setId(participantId);

        when(eventJpaRepository.getReferenceById(eventId)).thenReturn(eventRef);
        when(userJpaRepository.getReferenceById(participantId)).thenReturn(userRef);

        ArgumentCaptor<TicketEntity> captor = ArgumentCaptor.forClass(TicketEntity.class);

        TicketEntity savedEntity = new TicketEntity();
        savedEntity.setId(999L);
        savedEntity.setEvent(eventRef);
        savedEntity.setUser(userRef);

        when(ticketJpaRepository.save(any(TicketEntity.class))).thenReturn(savedEntity);

        Ticket result = adapter.save(input);

        verify(eventJpaRepository).getReferenceById(eventId);
        verify(userJpaRepository).getReferenceById(participantId);
        verify(ticketJpaRepository).save(captor.capture());

        TicketEntity entitySentToSave = captor.getValue();
        assertNull(entitySentToSave.getId(), "TicketEntity id should be null before persist");
        assertSame(eventRef, entitySentToSave.getEvent());
        assertSame(userRef, entitySentToSave.getUser());

        assertNotNull(result);
        assertEquals(999L, result.getId());
        assertEquals(participantId, result.getParticipantId());

        verifyNoMoreInteractions(ticketJpaRepository, userJpaRepository, eventJpaRepository);
    }

    @Test
    @DisplayName("findByUserId should map TicketEntity list to domain Ticket list")
    void findByUserId_shouldMapEntitiesToDomain() {
        Long userId = 20L;

        EventEntity e1 = new EventEntity(); e1.setId(10L);
        UserEntity u = new UserEntity(); u.setId(userId);

        TicketEntity t1 = new TicketEntity();
        t1.setId(1L);
        t1.setEvent(e1);
        t1.setUser(u);

        EventEntity e2 = new EventEntity(); e2.setId(11L);
        TicketEntity t2 = new TicketEntity();
        t2.setId(2L);
        t2.setEvent(e2);
        t2.setUser(u);

        when(ticketJpaRepository.findByUserId(userId)).thenReturn(List.of(t1, t2));

        List<Ticket> result = adapter.findByUserId(userId);

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals(10L, result.get(0).getEventId());
        assertEquals(userId, result.get(0).getParticipantId());

        assertEquals(2L, result.get(1).getId());
        assertEquals(11L, result.get(1).getEventId());
        assertEquals(userId, result.get(1).getParticipantId());

        verify(ticketJpaRepository).findByUserId(userId);
        verifyNoMoreInteractions(ticketJpaRepository, userJpaRepository, eventJpaRepository);
    }
}