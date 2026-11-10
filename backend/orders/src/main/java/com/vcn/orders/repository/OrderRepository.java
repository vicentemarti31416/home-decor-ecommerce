package com.vcn.orders.repository;

import com.vcn.orders.model.Order;
import com.vcn.orders.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByStatusAndUsername(OrderStatus status, String username);
    Optional<List<Order>> findAllByUsername(String username);
    Page<Order> findByUsernameAndUpdatedDateBetweenAndStatusNot(
            String username,
            Instant start,
            Instant end,
            OrderStatus status,
            Pageable pageable);
}
