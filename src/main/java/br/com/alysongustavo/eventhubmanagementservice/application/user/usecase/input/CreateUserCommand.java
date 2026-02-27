package br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.input;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;

public record CreateUserCommand(String name,
                                String email,
                                UserType userType,
                                String role)
{}
