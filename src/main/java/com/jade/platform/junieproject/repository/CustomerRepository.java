package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for Customer entity.
 * Extends R2dbcRepository for reactive database operations.
 */
public interface CustomerRepository extends R2dbcRepository<Customer, Integer> {
    
    /**
     * Find a customer by email address.
     * 
     * @param email the email address to search for
     * @return a Mono containing the customer if found, or an empty Mono if not found
     */
    Mono<Customer> findByEmail(String email);
}