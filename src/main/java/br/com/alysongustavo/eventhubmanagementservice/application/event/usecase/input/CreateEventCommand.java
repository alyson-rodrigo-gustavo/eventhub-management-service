package br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input;

import java.time.LocalDate;

public record CreateEventCommand(String name,
                                 LocalDate date,
                                 String location,
                                 Integer capacity)
{}
