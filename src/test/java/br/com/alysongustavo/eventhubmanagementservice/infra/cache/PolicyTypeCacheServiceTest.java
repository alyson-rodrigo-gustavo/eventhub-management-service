package br.com.alysongustavo.eventhubmanagementservice.infra.cache;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolicyTypeCacheServiceTest {

    @Mock
    private EventJpaRepository repository;

    @InjectMocks
    private EventCacheService eventCacheService;

    private List<EventEntity> mockEntities;

    @BeforeEach
    void setUp() {
        mockEntities = List.of(
                new EventEntity(1L, "Event test 1", LocalDate.now().plusMonths(1), "Location 1", 100),
                new EventEntity(2L, "Event test 2", LocalDate.now().plusMonths(2), "Location 2", 200),
                new EventEntity(3L, "Event test 3", LocalDate.now().plusMonths(3), "Location 3", 300)
        );
    }

    @Test
    @DisplayName("Should return all EventEntities directly from repository")
    void shouldReturnAllEventEntities() {
        when(repository.findAll()).thenReturn(mockEntities);

        List<EventEntity> result = eventCacheService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(mockEntities);

        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should map EventEntities by name correctly")
    void shouldMapEventEntitiesByName() {
        when(repository.findAll()).thenReturn(mockEntities);

        Map<String, EventEntity> resultMap = eventCacheService.getAllByName();

        assertThat(resultMap).isNotNull();
        assertThat(resultMap).hasSize(3);

        assertThat(resultMap.get("Event test 1").getId()).isEqualTo(1L);
        assertThat(resultMap.get("Event test 2").getId()).isEqualTo(2L);
        assertThat(resultMap.get("Event test 3").getId()).isEqualTo(3L);

        verify(repository).findAll();
    }
}