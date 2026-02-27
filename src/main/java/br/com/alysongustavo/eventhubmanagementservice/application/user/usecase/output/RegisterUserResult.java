package br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.output;

import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.UserType;

public record RegisterUserResult(
        Long id,
        String name,
        String email,
        UserType userType,
        String role
) {}
