package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for Customer operations.
 * Provides methods for CRUD operations on Customer entities.
 */
public interface CustomerService {
    
    /**
     * Get all customers.
     * 
     * @return a Flux of all customers
     */
    Flux<CustomerDto> getAllCustomers();
    
    /**
     * Get a customer by ID.
     * 
     * @param id the ID of the customer to retrieve
     * @return a Mono containing the customer if found, or an empty Mono if not found
     */
    Mono<CustomerDto> getCustomerById(Integer id);
    
    /**
     * Get a customer by email.
     * 
     * @param email the email of the customer to retrieve
     * @return a Mono containing the customer if found, or an empty Mono if not found
     */
    Mono<CustomerDto> getCustomerByEmail(String email);
    
    /**
     * Create a new customer.
     * 
     * @param customerDto the customer data to create
     * @return a Mono containing the created customer
     */
    Mono<CustomerDto> createCustomer(CustomerDto customerDto);
    
    /**
     * Update an existing customer.
     * 
     * @param id the ID of the customer to update
     * @param customerDto the updated customer data
     * @return a Mono containing the updated customer if found, or an empty Mono if not found
     */
    Mono<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto);
    
    /**
     * Delete a customer by ID.
     * 
     * @param id the ID of the customer to delete
     * @return a Mono that completes when the customer is deleted
     */
    Mono<Void> deleteCustomer(Integer id);
}