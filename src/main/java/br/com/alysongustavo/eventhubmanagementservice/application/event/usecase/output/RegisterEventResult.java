package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output;

import java.time.LocalDate;

public record RegisterEventResult(
        Long id,
        String name,
        LocalDate date,
        String location,
        Integer capacity
) {}
