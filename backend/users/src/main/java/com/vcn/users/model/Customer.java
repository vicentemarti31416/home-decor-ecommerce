package com.vcn.users.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    private String name;
    private String lastName;
    private String address;
    private String city;
    private String postalCode;
    private String email;
    private String phone;
    private String photo;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "customer_favorites",
            joinColumns = @JoinColumn(name = "customer_id")
    )
    @Column(name = "product_id")
    private Set<Long> favoriteIds;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @EqualsAndHashCode.Include
    private User user;
}
