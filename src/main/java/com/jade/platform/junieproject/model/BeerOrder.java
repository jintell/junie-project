package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

/**
 * BeerOrder entity as a JPA record.
 * This implementation uses a record with Lombok's Builder pattern.
 * Represents an order placed by a customer for one or more beers.
 */
@Entity
@Table(name = "beer_order")
public record BeerOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    String orderStatus,
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer,
    
    @OneToMany(mappedBy = "beerOrder")
    Set<BeerOrderLine> orderLines,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrder instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static BeerOrder createBeerOrder(
            Integer id,
            Integer version,
            String orderStatus,
            Customer customer,
            Set<BeerOrderLine> orderLines,
            Instant createdOn,
            Instant updatedOn) {

        return new BeerOrder(
            id,
            version,
            orderStatus,
            customer,
            orderLines,
            createdOn,
            updatedOn
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     * This constructor provides the minimum needed for JPA to work.
     */
    public BeerOrder() {
        this(null, null, null, null, null, null, null);
    }
}