package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterEventRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import br.com.alysongustavo.eventhubmanagementservice.infra.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import br.com.alysongustavo.eventhubmanagementservice.TestEventhubManagementServiceApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Import({TestSecurityConfig.class, TestEventhubManagementServiceApplication.class})
public class EventControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventJpaRepository eventJpaRepository;


    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setup() {
        eventJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration: Should create an event and persist it to the database (end-to-end flow)")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateEventAndPersistToDatabaseEndToEnd() throws Exception {

        RegisterEventRequest request = new RegisterEventRequest(
                "Event test", LocalDate.now().plusMonths(1), "Location test", 200
        );

        mockMvc.perform(post("/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Event test"));

        Optional<EventEntity> salvoNoBanco = eventJpaRepository.findByName("Event test");

        assertThat(salvoNoBanco).isPresent();
        assertThat(salvoNoBanco.get().getName()).isEqualTo("Event test");
        assertThat(salvoNoBanco.get().getDate()).isEqualTo(LocalDate.now().plusMonths(1));
        assertThat(salvoNoBanco.get().getCapacity()).isEqualByComparingTo(200);
    }

    @Test
    @DisplayName("Integration: Should reject eventhub creation when CPF validation fails (external service)")
    @WithMockUser(roles = "ADMIN")
    void shouldRejectEventCreationWhenRuleEventCoverageEligibilityValidationFails() throws Exception {
        RegisterEventRequest request = new RegisterEventRequest(
                "Event test", LocalDate.now(), "Location test", 100);

        mockMvc.perform(post("/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableContent());

        assertThat(eventJpaRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Integration: Should retrieve an event from the database")
    @WithMockUser(roles = "ADMIN")
    void shouldRetrieveEventFromDatabase() throws Exception {
        EventEntity entity =
                new EventEntity(null, "Event test", LocalDate.now(), "Location test", 100);

        EventEntity salvo = eventJpaRepository.save(entity);

        mockMvc.perform(get("/events/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Event test"));
    }

}
