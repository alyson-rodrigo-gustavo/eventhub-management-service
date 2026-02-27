package br.com.alysongustavo.eventhubmanagementservice.infra.config;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.service.EventCapacityCheckerService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DomainBeansConfig {

    @Bean
    public EventCapacityCheckerService validateEventCapacityCheckerService() {
        return new EventCapacityCheckerService();
    }

}
