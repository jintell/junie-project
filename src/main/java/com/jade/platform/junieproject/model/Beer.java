package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * Beer entity as a JPA record.
 * This implementation uses a record with Lombok's Builder pattern.
 */
@Entity
@Table(name = "beer")
public record Beer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    String beerName,
    String beerStyle,
    String upc,
    Integer quantityOnHand,
    BigDecimal unitPrice,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn,
    
    @OneToMany(mappedBy = "beer")
    Set<BeerOrderLine> orderLines
) {
    /**
     * Static builder method to create Beer instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static Beer createBeer(
            Integer id, 
            Integer version, 
            String beerName, 
            String beerStyle, 
            String upc, 
            Integer quantityOnHand, 
            BigDecimal unitPrice, 
            Instant createdOn, 
            Instant updatedOn,
            Set<BeerOrderLine> orderLines) {

        return new Beer(
            id, 
            version, 
            beerName, 
            beerStyle, 
            upc, 
            quantityOnHand, 
            unitPrice, 
            createdOn, 
            updatedOn,
            orderLines
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     * This constructor provides the minimum needed for JPA to work.
     */
    public Beer() {
        this(null, null, null, null, null, null, null, null, null, null);
    }
}
