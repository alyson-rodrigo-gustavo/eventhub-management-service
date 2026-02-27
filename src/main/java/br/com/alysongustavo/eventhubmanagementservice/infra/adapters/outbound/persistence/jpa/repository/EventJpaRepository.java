package br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findByName(String name);
}