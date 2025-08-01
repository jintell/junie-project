package com.jade.platform.junieproject.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

/**
 * Data Transfer Object for Customer entity.
 * Contains validation annotations for required fields.
 */
public record CustomerDto(
    Integer id,
    Integer version,
    
    @NotBlank(message = "Name is required")
    String name,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    String phone,
    
    Instant createdOn,
    Instant updatedOn
) {
    /**
     * Static builder method to create CustomerDto instances.
     * This is necessary because records can't use Lombok's @Builder directly.
     */
    @Builder
    public static CustomerDto createCustomerDto(
            Integer id,
            Integer version,
            String name,
            String email,
            String phone,
            Instant createdOn,
            Instant updatedOn) {

        return new CustomerDto(
            id,
            version,
            name,
            email,
            phone,
            createdOn,
            updatedOn
        );
    }
}