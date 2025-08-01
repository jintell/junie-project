package com.jade.platform.junieproject.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object for BeerOrder entity.
 * Contains validation annotations for required fields.
 */
public record BeerOrderDto(
    Integer id,
    Integer version,
    
    @NotBlank(message = "Order status is required")
    String orderStatus,
    
    @NotNull(message = "Customer ID is required")
    Integer customerId,
    
    @NotEmpty(message = "Order must contain at least one line item")
    @Valid
    List<BeerOrderLineDto> orderLines,
    
    Instant createdOn,
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrderDto instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static BeerOrderDto createBeerOrderDto(
            Integer id,
            Integer version,
            String orderStatus,
            Integer customerId,
            List<BeerOrderLineDto> orderLines,
            Instant createdOn,
            Instant updatedOn) {

        return new BeerOrderDto(
            id,
            version,
            orderStatus,
            customerId,
            orderLines,
            createdOn,
            updatedOn
        );
    }
}