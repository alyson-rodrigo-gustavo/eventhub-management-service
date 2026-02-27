package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.event.model.Event;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.RegisterEventRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.EventResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventRestMapper {

    EventResponse toEventResponse(Event event);

    CreateEventCommand toCreateEventCommand(RegisterEventRequest request);

    EventResponse toEventResponse(RegisterEventResult result);

}
