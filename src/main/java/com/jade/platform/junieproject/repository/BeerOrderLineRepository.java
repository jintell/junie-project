package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.BeerOrderLine;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for BeerOrderLine entity.
 * Extends R2dbcRepository for reactive database operations.
 */
public interface BeerOrderLineRepository extends R2dbcRepository<BeerOrderLine, Integer> {
    
    /**
     * Find all beer order lines for a specific beer order.
     * 
     * @param beerOrderId the ID of the beer order
     * @return a Flux of beer order lines for the specified beer order
     */
    Flux<BeerOrderLine> findAllByBeerOrderId(Integer beerOrderId);
}