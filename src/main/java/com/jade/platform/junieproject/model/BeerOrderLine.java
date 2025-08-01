package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * BeerOrderLine entity as a JPA record.
 * This implementation uses a record with Lombok's Builder pattern.
 * Represents a line item in a beer order, specifying a beer and its quantity.
 */
@Entity
@Table(name = "beer_order_line")
public record BeerOrderLine(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,
    
    @ManyToOne
    @JoinColumn(name = "beer_order_id")
    BeerOrder beerOrder,
    
    @ManyToOne
    @JoinColumn(name = "beer_id")
    Beer beer,
    
    Integer orderQuantity,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrderLine instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static BeerOrderLine createBeerOrderLine(
            Integer id,
            Integer version,
            BeerOrder beerOrder,
            Beer beer,
            Integer orderQuantity,
            Instant createdOn,
            Instant updatedOn) {

        return new BeerOrderLine(
            id,
            version,
            beerOrder,
            beer,
            orderQuantity,
            createdOn,
            updatedOn
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     * This constructor provides the minimum needed for JPA to work.
     */
    public BeerOrderLine() {
        this(null, null, null, null, null, null, null);
    }
}