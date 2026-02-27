package br.com.alysongustavo.eventhubmanagementservice.domain.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private Integer capacity;
}
