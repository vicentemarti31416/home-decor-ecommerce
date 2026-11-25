package com.vcn.products.consumer;

import com.vcn.products.model.UpdateStockEvent;
import com.vcn.products.service.ProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateStockConsumer {

    private final ProductService productService;

    public UpdateStockConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "update-stock", groupId = "products-group")
    public void onUpdateStock(UpdateStockEvent updateStockEvent) {
        productService.updateStock(updateStockEvent.products());
    }
}
