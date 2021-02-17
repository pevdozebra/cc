package co.sptnk.service.cofig;

import co.sptnk.lib.common.AMQPKeys;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue ordersQueue() {
        return QueueBuilder.durable(AMQPKeys.QUEUE_EVENTLOG).build();
    }

    @Bean
    TopicExchange ordersExchange() {
        return ExchangeBuilder.topicExchange(AMQPKeys.TOPIC_EVENTLOG).build();
    }

    @Bean
    Binding binding(Queue ordersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(AMQPKeys.QUEUE_EVENTLOG);
    }
}
