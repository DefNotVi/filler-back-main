package cl.back.filler.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "app.direct.exchange";
    public static final String QUEUE_NAME = "orders.queue";
    public static final String ROUTING_KEY = "orders.key";

    public static final String DLQ_EXCHANGE = "app.dlq.exchange";
    public static final String DLQ_QUEUE = "orders.dlq.queue";
    public static final String DLQ_ROUTING_KEY = "orders.dlq.key";

    // Exchange Principal
    @Bean
    public DirectExchange appExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    // Exchange para Mensajes Muertos (DLQ)
    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE, true, false);
    }

    // Cola DLQ
    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    // Binding para la DLQ
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue()).to(dlqExchange()).with(DLQ_ROUTING_KEY);
    }

    // Cola Principal con redireccionamiento automático a DLQ en caso de rechazo
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    // Binding Principal
    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(mainQueue()).to(appExchange()).with(ROUTING_KEY);
    }
}
