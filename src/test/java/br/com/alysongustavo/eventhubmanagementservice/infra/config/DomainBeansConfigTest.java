package br.com.alysongustavo.eventhubmanagementservice.infra.config;

import br.com.alysongustavo.eventhubmanagementservice.domain.event.service.EventCapacityCheckerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = DomainBeansConfig.class)
public class DomainBeansConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EventCapacityCheckerService validateEventCapacityCheckerService;

    @Test
    @DisplayName("Should load application context and register all domain beans")
    void shouldLoadContextAndRegisterAllDomainBeans() {
        assertThat(validateEventCapacityCheckerService).isNotNull();
        assertThat(context.containsBean("validateEventCapacityCheckerService")).isTrue();
    }

}
