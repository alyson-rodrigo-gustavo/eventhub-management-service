package br.com.alysongustavo.eventhubmanagementservice.infra.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PolicyTypeCacheWarmupTest {

    @Mock
    private EventCacheService cacheService;

    @Mock
    private ApplicationArguments applicationArguments;

    @InjectMocks
    private EventCacheWarmup eventCacheWarmup;

    @Test
    @DisplayName("Should call cache service methods to warmup cache on application startup")
    void shouldWarmupCacheOnStartup() {

        eventCacheWarmup.run(applicationArguments);

        verify(cacheService, times(1)).getAll();
        verify(cacheService, times(1)).getAllByName();
    }
}