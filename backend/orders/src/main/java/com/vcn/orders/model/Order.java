package com.vcn.orders.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Item> items;
    @Column(scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @CreationTimestamp
    @EqualsAndHashCode.Include
    private Instant createdDate;
    @UpdateTimestamp
    private Instant updatedDate;
    @Column(nullable = false, updatable = false)
    private String username;
}
