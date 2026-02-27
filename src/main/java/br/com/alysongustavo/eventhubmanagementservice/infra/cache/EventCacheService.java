package br.com.alysongustavo.eventhubmanagementservice.infra.cache;

import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.entity.EventEntity;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.persistence.jpa.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventCacheService {

    public static final String CACHE_NAME = "event";

    private final EventJpaRepository repository;

    /**
     * Carrega tudo do banco e armazena no Redis.
     * Chave fixa 'all' porque é reference data.
     */
    @Cacheable(cacheNames = CACHE_NAME, key = "'all'")
    public List<EventEntity> getAll() {
        return repository.findAll();
    }

    /**
     * Opcional: mapa por name (facilita validações/lookup).
     * Também cacheado com chave fixa.
     */
    @Cacheable(cacheNames = CACHE_NAME, key = "'byName'")
    public Map<String, EventEntity> getAllByName() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(
                        EventEntity::getName,
                        Function.identity()
                ));
    }

}
