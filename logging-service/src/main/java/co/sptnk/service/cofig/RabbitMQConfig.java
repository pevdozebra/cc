package co.sptnk.service.cofig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_LOG = "logging-queue";
    public static final String EXCHANGE_LOG = "logging-exchange";

    @Bean
    Queue ordersQueue() {
        return QueueBuilder.durable(QUEUE_LOG).build();
    }

    @Bean
    TopicExchange ordersExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_LOG).build();
    }

    @Bean
    Binding binding(Queue ordersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(QUEUE_LOG);
    }
}
