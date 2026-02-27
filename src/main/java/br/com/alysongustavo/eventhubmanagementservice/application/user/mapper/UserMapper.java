package br.com.alysongustavo.eventhubmanagementservice.application.user.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.input.CreateUserCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.user.usecase.output.RegisterUserResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(CreateUserCommand command);

    RegisterUserResult toRegisterUserResult(User user);

}
