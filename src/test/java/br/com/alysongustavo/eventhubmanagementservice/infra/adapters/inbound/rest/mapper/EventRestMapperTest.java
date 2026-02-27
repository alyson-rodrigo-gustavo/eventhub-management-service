package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterEventRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.EventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EventRestMapperTest.TestConfig.class)
public class EventRestMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = EventRestMapper.class)
    static class TestConfig {}

    @Autowired
    private EventRestMapper eventRestMapper;

    @Test
    @DisplayName("Should map Event domain model to EventResponse DTO")
    void shouldMapDomainParaResponse() {
        Event event =
                new Event(1L, "Event test",
                        LocalDate.now().plusMonths(1), "Test location", 300);


        EventResponse response = eventRestMapper.toEventResponse(event);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(event.getId());
        assertThat(response.getName()).isEqualTo(event.getName());
        assertThat(response.getDate()).isEqualTo(event.getDate());
        assertThat(response.getLocation()).isEqualTo(event.getLocation());
        assertThat(response.getCapacity()).isEqualTo(event.getCapacity());
    }

    @Test
    @DisplayName("Should map RegisterEventRequest to CreateEventCommand")
    void shouldMapRequestToCommand() {
        RegisterEventRequest request = new RegisterEventRequest(
                "Event test", LocalDate.now().plusMonths(2), "location ", 100);

        var command = eventRestMapper.toCreateEventCommand(request);

        assertThat(command).isNotNull();
        assertThat(command.name()).isEqualTo(request.getName());
        assertThat(command.date()).isEqualTo(request.getDate());
        assertThat(command.location()).isEqualTo(request.getLocation());
        assertThat(command.capacity()).isEqualTo(request.getCapacity());
    }

    @Test
    @DisplayName("Should map RegisterEventResult to EventResponse DTO")
    void shouldMapResultToResponse() {

        RegisterEventResult result = new RegisterEventResult(
                1L, "Event test", LocalDate.now().plusMonths(1), "Location test", 100
        );

        var response = eventRestMapper.toEventResponse(result);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(result.id());
        assertThat(response.getName()).isEqualTo(result.name());
        assertThat(response.getDate()).isEqualTo(result.date());
        assertThat(response.getLocation()).isEqualTo(result.location());
        assertThat(response.getCapacity()).isEqualTo(result.capacity());
    }

}
