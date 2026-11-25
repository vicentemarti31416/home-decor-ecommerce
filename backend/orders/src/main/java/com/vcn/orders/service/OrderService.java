package com.vcn.orders.service;

import com.vcn.orders.model.*;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    List<Order> findAllByUsername(String username);
    Order findById(String id);
    Order updateOrder(Order order, Jwt jwt);
    String paymentIntentHelper(Order order, Jwt jwt);
    Order updateOrderStatus(StatusRequest statusRequest, Jwt jwt);
    Page<OrderResponse> findAllByUsernameAndIds(Jwt jwt, Pageable pageable, LocalDate start, LocalDate end);
    Order findByStatusAndUsername(OrderStatus status, String username);
    Order save(Order order);
    void deleteById(String id);
}
