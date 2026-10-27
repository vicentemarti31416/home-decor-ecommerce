package com.vcn.orders.service;

import com.vcn.orders.model.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface OrderService {

    List<Order> findAllByUsername(String username);
    Order findById(String id);
    Order updateOrder(Order order, Jwt jwt);
    String paymentIntentHelper(Order order, Jwt jwt);
    Order updateOrderStatus(StatusRequest statusRequest, Jwt jwt);
    List<Item> checkStockAndPrice(Order order, Jwt jwt);
    ProductRecord findProductById(Long id, Jwt jwt);
    List<OrderResponse> findAllByUsernameAndIds(Jwt jwt);
    List<OrderResponse> toOrderResponseList(List<Order> orders, List<ProductResponse> products);
    List<ProductResponse> findProductsById(List<Long> ids, Jwt jwt);
    void updateStock(Order order, Jwt jwt);
    String intentPayment(IntentRequest intentRequest, Jwt jwt);
    Order findByStatusAndUsername(OrderStatus status, String username);
    Order save(Order order);
    void deleteById(String id);
    void notifyPayment(Order order, Jwt jwt);
}
