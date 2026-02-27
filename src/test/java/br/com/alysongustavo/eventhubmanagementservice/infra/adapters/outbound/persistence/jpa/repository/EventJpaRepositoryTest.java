package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class EventJpaRepositoryTest {

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should return event when searching by existing name")
    void shouldReturnEventWhenSearchingByExistingName() {
        String name = "Event test 1";


        EventEntity entity = criarEventEntity(name);

        entityManager.persist(entity);

        entityManager.flush();
        entityManager.clear();

        Optional<EventEntity> result = eventJpaRepository.findByName(name);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Should return empty when searching by non-existing NAME")
    void shouldReturnEmptyWhenSearchingByNonExistingName() {
        String name = "Event test 1";

        Optional<EventEntity> result = eventJpaRepository.findByName(name);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should persist event and generate id automatically")
    void shouldPersistEventAndGenerateIdAutomatically() {
        EventEntity entity = criarEventEntity("Event test 1");

        EventEntity saved = eventJpaRepository.save(entity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Event test 1");
    }

    private EventEntity criarEventEntity(String name) {
        return new EventEntity(
                null,
                name,
                LocalDate.now().plusMonths(1),
                "Test location",
                300
        );
    }
}
