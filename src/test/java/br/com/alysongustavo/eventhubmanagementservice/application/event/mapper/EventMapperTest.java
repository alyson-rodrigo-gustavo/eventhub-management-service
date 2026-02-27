package br.com.alysongustavo.eventhubmanagementservice.application.event.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EventMapperTest.TestConfig.class)
public class EventMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = EventMapper.class)
    static class TestConfig {}

    @Autowired
    private EventMapper eventMapper;

    @Test
    @DisplayName("Should map CreateEventCommand to Event domain model correctly")
    void shouldMapCommandToDomain() {

        CreateEventCommand command = new CreateEventCommand(
                "Event Test",
                LocalDate.now(),
                "Test Location",
                100
        );

        Event domain = eventMapper.toDomain(command);

        assertThat(domain).isNotNull();
        assertThat(domain.getName()).isEqualTo(command.name());
        assertThat(domain.getDate()).isEqualTo(command.date());
        assertThat(domain.getLocation()).isEqualTo(command.location());
        assertThat(domain.getCapacity()).isEqualTo(command.capacity());

    }

    @Test
    @DisplayName("Should map Event domain model to RegisterEventResult correctly")
    void shouldMapDomainToResult() {
        Event event =
                new Event(1L, "Event Test", LocalDate.now(), "Test location", 200);

        RegisterEventResult result = eventMapper.toRegisterEventResult(event);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(event.getId());
        assertThat(result.name()).isEqualTo(event.getName());
        assertThat(result.date()).isEqualTo(event.getDate());
        assertThat(result.location()).isEqualTo(event.getLocation());
        assertThat(result.capacity()).isEqualTo(event.getCapacity());

    }
}
