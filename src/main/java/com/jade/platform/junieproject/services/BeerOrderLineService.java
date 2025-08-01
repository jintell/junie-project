package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for BeerOrderLine operations.
 * Provides methods for CRUD operations on BeerOrderLine entities.
 */
public interface BeerOrderLineService {
    
    /**
     * Get all beer order lines.
     * 
     * @return a Flux of all beer order lines
     */
    Flux<BeerOrderLineDto> getAllOrderLines();
    
    /**
     * Get all beer order lines for a specific beer order.
     * 
     * @param orderId the ID of the beer order
     * @return a Flux of beer order lines for the specified beer order
     */
    Flux<BeerOrderLineDto> getOrderLinesByOrderId(Integer orderId);
    
    /**
     * Get a beer order line by ID.
     * 
     * @param id the ID of the beer order line to retrieve
     * @return a Mono containing the beer order line if found, or an empty Mono if not found
     */
    Mono<BeerOrderLineDto> getOrderLineById(Integer id);
    
    /**
     * Create a new beer order line.
     * 
     * @param beerOrderLineDto the beer order line data to create
     * @return a Mono containing the created beer order line
     */
    Mono<BeerOrderLineDto> createOrderLine(BeerOrderLineDto beerOrderLineDto);
    
    /**
     * Update an existing beer order line.
     * 
     * @param id the ID of the beer order line to update
     * @param beerOrderLineDto the updated beer order line data
     * @return a Mono containing the updated beer order line if found, or an empty Mono if not found
     */
    Mono<BeerOrderLineDto> updateOrderLine(Integer id, BeerOrderLineDto beerOrderLineDto);
    
    /**
     * Delete a beer order line by ID.
     * 
     * @param id the ID of the beer order line to delete
     * @return a Mono that completes when the beer order line is deleted
     */
    Mono<Void> deleteOrderLine(Integer id);
}