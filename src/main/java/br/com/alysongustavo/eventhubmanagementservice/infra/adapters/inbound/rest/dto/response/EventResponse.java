package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.inbound.rest.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventResponse {

    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private Integer capacity;
}
