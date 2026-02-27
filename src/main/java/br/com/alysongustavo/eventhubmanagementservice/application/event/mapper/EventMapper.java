package br.com.alysongustavo.eventhubmanagementservice.application.event.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toDomain(CreateEventCommand command);

    RegisterEventResult toRegisterEventResult(Event eventhub);

}
