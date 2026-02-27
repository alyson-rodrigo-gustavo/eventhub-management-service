package br.com.alysongustavo.eventhubmanagementservice.infra.config;

import br.com.alysongustavo.eventhubmanagementservice.application.user.port.out.EventPublisherPort;
import br.com.alysongustavo.eventhubmanagementservice.infra.adapters.outbound.messaging.RabbitMQEventPublisherAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQRestConfig {

    public static final String EVENTS_EXCHANGE = "events-exchange";

    public static final String USER_REGISTERED_QUEUE = "user.registered.queue";
    public static final String USER_REGISTERED_ROUTING_KEY = "user.registered";

    public static final String TICKET_PURCHASE_QUEUE = "ticket.purchase.queue";
    public static final String TICKET_PURCHASE_ROUTING_KEY = "ticket.purchase";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(USER_REGISTERED_QUEUE, true);
    }

    @Bean
    public Binding userRegisteredBinding(Queue userRegisteredQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(userRegisteredQueue)
                .to(eventsExchange)
                .with(USER_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Queue ticketPurchaseQueue() {
        return new Queue(TICKET_PURCHASE_QUEUE, true);
    }

    @Bean
    public Binding ticketPurchaseBinding(Queue ticketPurchaseQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(ticketPurchaseQueue)
                .to(eventsExchange)
                .with(TICKET_PURCHASE_ROUTING_KEY);
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public EventPublisherPort eventPublisherPort(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        return new RabbitMQEventPublisherAdapter(rabbitTemplate, objectMapper);
    }
}
