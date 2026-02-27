package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.mapper;

import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.input.CreateEventCommand;
import br.com.alysongustavo.eventhubmanagementservice.application.event.usecase.output.RegisterEventResult;
import br.com.alysongustavo.eventhubmanagementservice.application.ticketpurchase.usecase.output.RegisterTicketResult;
import br.com.alysongustavo.eventhubmanagementservice.domain.ticketpurchase.model.Ticket;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.request.PurchaseTicketRequest;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response.TicketResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketRestMapper {

    TicketResponse toTicketResponse(Ticket ticket);

    CreateEventCommand toCreateTicketCommand(PurchaseTicketRequest request);

    TicketResponse toTicketResponse(RegisterTicketResult result);

}
