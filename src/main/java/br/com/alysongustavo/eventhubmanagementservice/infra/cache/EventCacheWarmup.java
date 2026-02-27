package br.com.alysongustavo.eventhubmanagementservice.infra.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventCacheWarmup implements ApplicationRunner {

    private final EventCacheService cacheService;

    @Override
    public void run(ApplicationArguments args) {
        cacheService.getAll();
        cacheService.getAllByName();
    }
}
