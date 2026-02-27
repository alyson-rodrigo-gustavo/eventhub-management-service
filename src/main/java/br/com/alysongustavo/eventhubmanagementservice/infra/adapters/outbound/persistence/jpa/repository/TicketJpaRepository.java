package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {

    long countByEventId(Long eventId);

    List<TicketEntity> findByUserId(Long userId);
}
