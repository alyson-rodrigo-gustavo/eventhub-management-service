package br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.input;

import java.time.LocalDate;

public record CreateTicketCommand(String name,
                                  LocalDate date,
                                  String location,
                                  Integer capacity)
{}
