package com.jade.platform.junieproject.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.Instant;

/**
 * Data Transfer Object for BeerOrderLine entity.
 * Contains validation annotations for required fields.
 */
public record BeerOrderLineDto(
    // read only
    Integer id,
    // read only
    Integer version,

    // This is the 11-alphanumeric order identifier
    Integer orderId,
    
    @NotNull(message = "Beer ID is required")
    Integer beerId,
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be positive")
    Integer orderQuantity,

    // read only created date
    Instant createdOn,
    // read only updated date
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrderLineDto instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static BeerOrderLineDto createBeerOrderLineDto(
            Integer id,
            Integer version,
            Integer orderId,
            Integer beerId,
            Integer orderQuantity,
            Instant createdOn,
            Instant updatedOn) {

        return new BeerOrderLineDto(
            id,
            version,
            orderId,
            beerId,
            orderQuantity,
            createdOn,
            updatedOn
        );
    }
}