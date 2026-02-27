package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.mapper;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EventJpaMapperTest.TestConfig.class)
public class EventJpaMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = EventJpaMapper.class)
    static class TestConfig {}

    @Autowired
    private EventJpaMapper eventJpaMapper;

    @Test
    @DisplayName("Should map Event domain model to EventEntity correctly")
    void shouldMapDomainToEntity() {
        Event event =
                new Event(1L, "Event test 1" , LocalDate.now().plusMonths(1), "Test location 1", 300);


        EventEntity eventEntity = eventJpaMapper.toEntity(event);

        assertThat(eventEntity).isNotNull();
        assertThat(eventEntity.getId()).isEqualTo(event.getId());
        assertThat(eventEntity.getName()).isEqualTo(event.getName());
        assertThat(eventEntity.getDate()).isEqualTo(event.getDate());
        assertThat(eventEntity.getLocation()).isEqualTo(event.getLocation());
        assertThat(eventEntity.getCapacity()).isEqualTo(event.getCapacity());

    }

    @Test
    @DisplayName("Should map EventEntity to Event domain model correctly")
    void shouldMapEntityToDomain() {
        EventEntity eventEntity =
                new EventEntity(1L, "Event test 1", LocalDate.now().plusDays(10), "Teste location 1", 200);


        Event event = eventJpaMapper.toDomain(eventEntity);

        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(eventEntity.getId());
        assertThat(event.getName()).isEqualTo(eventEntity.getName());
        assertThat(event.getDate()).isEqualTo(eventEntity.getDate());
        assertThat(event.getLocation()).isEqualTo(eventEntity.getLocation());
        assertThat(event.getCapacity()).isEqualTo(eventEntity.getCapacity());

    }


}
