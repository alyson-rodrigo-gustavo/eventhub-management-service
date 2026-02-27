package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.*;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterEventRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.EventResponse;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper.EventRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final ListEventUseCase listEventUseCase;
    private final RegisterEventUseCase registerEventUseCase;
    private final FindByIdEventUseCase findByIdEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final EditEventUseCase editEventUseCase;
    private final EventRestMapper eventRestMapper;

    @Operation(summary = "Search event by ID", description = "Returns the details of a specific event. Requires ADMIN permission.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event successfully found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Requires ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse resp = eventRestMapper.toEventResponse(this.findByIdEventUseCase.execute(id));
        return ResponseEntity.ok(resp);
    }

    @Operation(
            summary = "List all event",
            description = "Returns a list containing all registered event. Requires ADMIN permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List successfully returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Requires ADMIN role)")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventResponse>> listEvents() {
        List<EventResponse> resp = this.listEventUseCase.execute()
                .stream().map(eventRestMapper::toEventResponse)
                .toList();

        return ResponseEntity.ok(resp);
    }

    @Operation(
            summary = "Register a new event",
            description = "Creates a new event after validating business rules. Requires ADMIN permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation error in request payload fields (e.g., negative values)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Requires ADMIN role)"),
            @ApiResponse(responseCode = "422", description = "Business rule violation (Event already exists)")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> registerEvent(@RequestBody @Valid RegisterEventRequest registerEventRequest) {
        log.info("Receiving request to register event. Name: {}, Date: {}, Location: {}, Capacity: {}",
                registerEventRequest.getName(),
                registerEventRequest.getDate(),
                registerEventRequest.getLocation(),
                registerEventRequest.getCapacity());

        var result = this.registerEventUseCase.execute(eventRestMapper.toCreateEventCommand(registerEventRequest));

        log.info("Event successfully registered. Generated ID: {}", result.id());
        return ResponseEntity.status(HttpStatus.OK).body(eventRestMapper.toEventResponse(result));
    }

    @Operation(
            summary = "Edit an event",
            description = "Edits an event after validating business rules. Requires ADMIN permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation error in request payload fields"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Requires ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "Event not found for the provided ID"),
            @ApiResponse(responseCode = "422", description = "Business rule violation (Event already exists)")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> editEvent(@RequestBody @Valid RegisterEventRequest registerEventRequest, @PathVariable Long id) {
        var result = this.editEventUseCase.execute(eventRestMapper.toCreateEventCommand(registerEventRequest), id);
        return ResponseEntity.status(HttpStatus.OK).body(eventRestMapper.toEventResponse(result));
    }

    @Operation(
            summary = "Delete an event",
            description = "Removes an event from the system by its ID. Requires ADMIN permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event successfully deleted (No content returned)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (Missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Requires ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        deleteEventUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
