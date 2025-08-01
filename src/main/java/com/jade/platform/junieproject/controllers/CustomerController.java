package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.CustomerDto;
import com.jade.platform.junieproject.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for Customer operations.
 * Provides endpoints for CRUD operations on Customer entities.
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    
    private final CustomerService customerService;
    
    /**
     * Constructor for CustomerController.
     * 
     * @param customerService the service for Customer operations
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    /**
     * Get all customers.
     * 
     * @return a Flux of all customers
     */
    @GetMapping
    public Flux<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    
    /**
     * Get a customer by ID.
     * 
     * @param id the ID of the customer to retrieve
     * @return a Mono containing the customer if found, or a 404 response if not found
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Get a customer by email.
     * 
     * @param email the email of the customer to retrieve
     * @return a Mono containing the customer if found, or a 404 response if not found
     */
    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<CustomerDto>> getCustomerByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Create a new customer.
     * 
     * @param customerDto the customer data to create
     * @return a Mono containing the created customer with a 201 status code
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }
    
    /**
     * Update an existing customer.
     * 
     * @param id the ID of the customer to update
     * @param customerDto the updated customer data
     * @return a Mono containing the updated customer if found, or a 404 response if not found
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete a customer by ID.
     * 
     * @param id the ID of the customer to delete
     * @return a Mono that completes with a 204 status code when the customer is deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }
}