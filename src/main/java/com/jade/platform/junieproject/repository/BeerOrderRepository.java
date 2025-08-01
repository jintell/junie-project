package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.BeerOrder;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for BeerOrder entity.
 * Extends R2dbcRepository for reactive database operations.
 */
public interface BeerOrderRepository extends R2dbcRepository<BeerOrder, Integer> {
    
    /**
     * Find all beer orders for a specific customer.
     * 
     * @param customerId the ID of the customer
     * @return a Flux of beer orders for the specified customer
     */
    Flux<BeerOrder> findAllByCustomerId(Integer customerId);
}