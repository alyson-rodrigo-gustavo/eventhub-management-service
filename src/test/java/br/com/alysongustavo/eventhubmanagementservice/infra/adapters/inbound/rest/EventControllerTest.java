package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.*;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterEventRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.EventResponse;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper.EventRestMapper;
import br.com.alysongustavo.eventhubmanagementservice.infra.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private ListEventUseCase listEventUseCase;
    @MockitoBean private RegisterEventUseCase registerEventUseCase;
    @MockitoBean private FindByIdEventUseCase findByIdEventUseCase;
    @MockitoBean private DeleteEventUseCase deleteEventUseCase;
    @MockitoBean private EditEventUseCase editEventUseCase;
    @MockitoBean private EventRestMapper eventRestMapper;


    @Test
    @DisplayName("GET /event/{id} - Should return 200 OK when user has ADMIN role")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200WhenAdminFetchesEventById() throws Exception {
        Long id = 1L;
        Event domain = new Event(id, "Event test", LocalDate.now(), "Test location", 200);

        EventResponse response = new EventResponse();
        response.setId(id);
        response.setName("Event test");
        response.setDate(LocalDate.now());
        response.setLocation("Test location");
        response.setCapacity(200);

        when(findByIdEventUseCase.execute(id)).thenReturn(domain);
        when(eventRestMapper.toEventResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/events/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event test"))
                .andExpect(jsonPath("$.location").value("Test location"));
    }

    @Test
    @DisplayName("GET /event/{id} - Should return 403 Forbidden when user does not have ADMIN role")
    @WithMockUser(roles = "USER")
    void shouldReturn403WhenUserWithoutManagerRoleFetchesEventById() throws Exception {
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /event - Should return a list of event")
    @WithMockUser(roles = "ADMIN")
    void shouldReturnEventsList() throws Exception {
        Event event1 = new Event(1L, "Event test 1", LocalDate.now().plusMonths(1), "Test location 1", 100);
        Event event2 = new Event(2L, "Event test 2", LocalDate.now().plusMonths(2), "Test location 2", 200);

        EventResponse resp1 = new EventResponse();
        resp1.setId(1L);
        resp1.setName("Event test 1");
        resp1.setDate(LocalDate.now().plusMonths(1));
        resp1.setLocation("Test location 1");
        resp1.setCapacity(100);

        EventResponse resp2 = new EventResponse();
        resp2.setId(2L);
        resp2.setName("Event test 2");
        resp2.setDate(LocalDate.now().plusMonths(2));
        resp2.setLocation("Test location 2");
        resp2.setCapacity(200);

        when(listEventUseCase.execute()).thenReturn(List.of(event1, event2));
        when(eventRestMapper.toEventResponse(event1)).thenReturn(resp1);
        when(eventRestMapper.toEventResponse(event2)).thenReturn(resp2);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Event test 1"));
    }

    @Test
    @DisplayName("POST /event - Should create an event when user has ADMIN role")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateEventWhenAdmin() throws Exception {

        RegisterEventRequest request = new RegisterEventRequest(
                "Event test 1", LocalDate.now(), "Test location", 100
        );

        var command = eventRestMapper.toCreateEventCommand(request);

        RegisterEventResult resultMock = new RegisterEventResult(
                1L,
                "Event test 1",
                LocalDate.now().plusMonths(1),
                "Test location 1",
                100
        );

        EventResponse response = new EventResponse();
        response.setId(1L);
        response.setName("Event test 1");
        response.setDate(LocalDate.now().plusMonths(1));
        response.setLocation("Test location 1");


        when(eventRestMapper.toCreateEventCommand(any(RegisterEventRequest.class)))
                .thenReturn(mock(CreateEventCommand.class));

        when(registerEventUseCase.execute(any())).thenReturn(resultMock);

        when(eventRestMapper.toEventResponse(resultMock))
                .thenReturn(response);

        mockMvc.perform(post("/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Event test 1"));
    }

    @Test
    @DisplayName("POST /event - Should return 403 Forbidden when USER tries to create an eventhub")
    @WithMockUser(roles = "USER")
    void shouldReturn403WhenUserTriesToCreateEvent() throws Exception {
        RegisterEventRequest request = new RegisterEventRequest("Event test 1", LocalDate.now().plusMonths(1),
                "Test location 1", 100);

        mockMvc.perform(post("/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /event - Should return 400 Bad Request when request body is invalid")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        String emptyJson = "{}";

        mockMvc.perform(post("/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /event/{id} - Should delete eventhub and return 204 No Content when user has ADMIN role")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteEventAndReturn204WhenAdmin() throws Exception {
        Long id = 1L;

        doNothing().when(deleteEventUseCase).execute(id);

        mockMvc.perform(delete("/events/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent()); // 204

        verify(deleteEventUseCase).execute(id);
    }
}
