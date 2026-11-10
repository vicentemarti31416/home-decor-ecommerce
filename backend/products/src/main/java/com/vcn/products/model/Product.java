package com.vcn.products.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false, scale = 2)
    private BigDecimal price;
    private String category;
    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String reference;
    private Long stock;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,  orphanRemoval = true)
    @JsonManagedReference
    private List<Photo> photos;
    private Double rating = 0.0;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Rating> ratings;
    // Genera la tabla product_statistics
    // El primer valor del list almacena el número total de valoraciones
    // El valor del índice 1 del list almacena el número total de valoraciones de una estrella
    // El valor del índice 2 las valoraciones de 2 estrellas, el índice 3 las de tres estrellas...
    // @ElementCollection crea una tabla aparte para guardar los valores
    // @OrderColumn asegura que los valores de esta tabla mantengan el orden (índice 1 stars 1, índice 2 stars 2...)
    @ElementCollection
    @OrderColumn(name = "position")
    private List<Integer> statistics = new ArrayList<>(Collections.nCopies(6, 0));
    @CreationTimestamp
    private Instant createdDate;
}
