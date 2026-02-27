package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.input.CreateUserCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.output.RegisterUserResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterUserRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    UserResponse toEventResponse(Event event);

    CreateUserCommand toCreateEventCommand(RegisterUserRequest request);

    UserResponse toUserResponse(RegisterUserResult result);

}
