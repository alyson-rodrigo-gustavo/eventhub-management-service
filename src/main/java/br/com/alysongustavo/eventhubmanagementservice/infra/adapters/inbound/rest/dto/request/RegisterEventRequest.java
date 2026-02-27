package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEventRequest {

    @NotEmpty(message = "Event name cannot be empty")
    @Size(max = 50)
    private String name;

    @NotNull
    @FutureOrPresent(message = "Event date cannot be in the past")
    private LocalDate date;

    @NotEmpty
    private String location;

    @NotNull
    private Integer capacity;

}
