package com.vcn.orders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class PublisherConfig {

    @Value("${rabbitmq.queue.orders.order_confirmation}")
    private String message;

    @Bean
    public Queue queue() {
        return new Queue(message, true);
    }

}
