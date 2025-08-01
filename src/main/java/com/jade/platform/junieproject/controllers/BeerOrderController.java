package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.BeerOrderDto;
import com.jade.platform.junieproject.services.BeerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for BeerOrder operations.
 * Provides endpoints for CRUD operations on BeerOrder entities.
 */
@RestController
@RequestMapping("/api/v1")
public class BeerOrderController {
    
    private final BeerOrderService beerOrderService;
    
    /**
     * Constructor for BeerOrderController.
     * 
     * @param beerOrderService the service for BeerOrder operations
     */
    public BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }
    
    /**
     * Get all beer orders.
     * 
     * @return a Flux of all beer orders
     */
    @GetMapping("/orders")
    public Flux<BeerOrderDto> getAllOrders() {
        return beerOrderService.getAllOrders();
    }
    
    /**
     * Get all beer orders for a specific customer.
     * 
     * @param customerId the ID of the customer
     * @return a Flux of beer orders for the specified customer
     */
    @GetMapping("/customers/{customerId}/orders")
    public Flux<BeerOrderDto> getOrdersByCustomerId(@PathVariable Integer customerId) {
        return beerOrderService.getOrdersByCustomerId(customerId);
    }
    
    /**
     * Get a beer order by ID.
     * 
     * @param id the ID of the beer order to retrieve
     * @return a Mono containing the beer order if found, or a 404 response if not found
     */
    @GetMapping("/orders/{id}")
    public Mono<ResponseEntity<BeerOrderDto>> getOrderById(@PathVariable Integer id) {
        return beerOrderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Create a new beer order.
     * 
     * @param beerOrderDto the beer order data to create
     * @return a Mono containing the created beer order with a 201 status code
     */
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BeerOrderDto> createOrder(@Valid @RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.createOrder(beerOrderDto);
    }
    
    /**
     * Update an existing beer order.
     * 
     * @param id the ID of the beer order to update
     * @param beerOrderDto the updated beer order data
     * @return a Mono containing the updated beer order if found, or a 404 response if not found
     */
    @PutMapping("/orders/{id}")
    public Mono<ResponseEntity<BeerOrderDto>> updateOrder(
            @PathVariable Integer id,
            @Valid @RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.updateOrder(id, beerOrderDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete a beer order by ID.
     * 
     * @param id the ID of the beer order to delete
     * @return a Mono that completes with a 204 status code when the beer order is deleted
     */
    @DeleteMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteOrder(@PathVariable Integer id) {
        return beerOrderService.deleteOrder(id);
    }
}