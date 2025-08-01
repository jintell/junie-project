package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

/**
 * Customer entity as a JPA record.
 * This implementation uses a record with Lombok's Builder pattern.
 * Represents a customer who can place beer orders.
 */
@Entity
@Table(name = "customer")
public record Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    String name,
    
    String email,
    
    String phone,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn,
    
    @OneToMany(mappedBy = "customer")
    Set<BeerOrder> orders
) {
    /**
     * Static builder method to create Customer instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static Customer createCustomer(
            Integer id,
            Integer version,
            String name,
            String email,
            String phone,
            Instant createdOn,
            Instant updatedOn,
            Set<BeerOrder> orders) {

        return new Customer(
            id,
            version,
            name,
            email,
            phone,
            createdOn,
            updatedOn,
            orders
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     * This constructor provides the minimum needed for JPA to work.
     */
    public Customer() {
        this(null, null, null, null, null, null, null, null);
    }
}