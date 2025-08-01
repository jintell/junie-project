package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.BeerOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for BeerOrder operations.
 * Provides methods for CRUD operations on BeerOrder entities.
 */
public interface BeerOrderService {
    
    /**
     * Get all beer orders.
     * 
     * @return a Flux of all beer orders
     */
    Flux<BeerOrderDto> getAllOrders();
    
    /**
     * Get all beer orders for a specific customer.
     * 
     * @param customerId the ID of the customer
     * @return a Flux of beer orders for the specified customer
     */
    Flux<BeerOrderDto> getOrdersByCustomerId(Integer customerId);
    
    /**
     * Get a beer order by ID.
     * 
     * @param id the ID of the beer order to retrieve
     * @return a Mono containing the beer order if found, or an empty Mono if not found
     */
    Mono<BeerOrderDto> getOrderById(Integer id);
    
    /**
     * Create a new beer order.
     * 
     * @param beerOrderDto the beer order data to create
     * @return a Mono containing the created beer order
     */
    Mono<BeerOrderDto> createOrder(BeerOrderDto beerOrderDto);
    
    /**
     * Update an existing beer order.
     * 
     * @param id the ID of the beer order to update
     * @param beerOrderDto the updated beer order data
     * @return a Mono containing the updated beer order if found, or an empty Mono if not found
     */
    Mono<BeerOrderDto> updateOrder(Integer id, BeerOrderDto beerOrderDto);
    
    /**
     * Delete a beer order by ID.
     * 
     * @param id the ID of the beer order to delete
     * @return a Mono that completes when the beer order is deleted
     */
    Mono<Void> deleteOrder(Integer id);
}