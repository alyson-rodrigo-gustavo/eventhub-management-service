package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

    @Id
    @SequenceGenerator(name = "event_seq", sequenceName = "event_seq", allocationSize = 1)
    @GeneratedValue(generator = "event_seq", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private Integer capacity;

}
