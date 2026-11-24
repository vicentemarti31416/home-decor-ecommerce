package com.vcn.orders.publisher;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;

@Component
@EnableRabbit
public class Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public Publisher(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void send(Object message) {
        rabbitTemplate.convertAndSend(queue.getName(), message);
    }

}
