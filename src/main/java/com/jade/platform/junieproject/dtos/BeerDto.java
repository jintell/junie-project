package com.jade.platform.junieproject.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Base64;

/**
 * Data Transfer Object for Beer entity.
 * This record is used for transferring beer data between the web layer and the service layer.
 */
@Builder
public record BeerDto (
    // read only
    Integer id,
    Integer version,
    
    @NotBlank(message = "Beer name is required")
    String beerName,

    // style of the beer, ALE, STOUT, PALE, PIA, etc
    @NotBlank(message = "Beer style is required")
    String beerStyle,

    // Universal Product Code, a 13-digits assigned to each unique beer product by the Federal Bar Association
    @NotBlank(message = "UPC is required")
    String upc,
    
    @NotNull(message = "Quantity on hand is required")
    @Positive(message = "Quantity on hand must be positive")
    Integer quantityOnHand,
    
    @NotNull(message = "Unit price is required")
    @PositiveOrZero(message = "Unit price must be positive or zero")
    BigDecimal unitPrice,

    // read only created date
    Instant createdOn,
    // read only updated date
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerDto instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static BeerDto createBeerDto(
            Integer id, 
            Integer version, 
            String beerName, 
            String beerStyle, 
            String upc, 
            Integer quantityOnHand, 
            BigDecimal unitPrice, 
            Instant createdOn, 
            Instant updatedOn) {

        return new BeerDto(
            id, 
            version, 
            beerName, 
            beerStyle, 
            upc, 
            quantityOnHand, 
            unitPrice, 
            createdOn, 
            updatedOn
        );
    }
}