package com.vcn.orders.producer;

import com.vcn.orders.model.Item;
import com.vcn.orders.model.Order;
import com.vcn.orders.model.UpdateStockEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateStockProducer {

    private final KafkaTemplate<String, UpdateStockEvent> kafkaTemplate;

    public UpdateStockProducer(KafkaTemplate<String, UpdateStockEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUpdateStock(Order order) {
        Map<Long, Long> products = order
                .getItems()
                .stream()
                .collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
        kafkaTemplate.send(
                "update-stock",
                order.getId(),
                new UpdateStockEvent(order.getId(), products));
    }
}
