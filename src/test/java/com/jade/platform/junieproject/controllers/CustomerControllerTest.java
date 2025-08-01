package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.CustomerDto;
import com.jade.platform.junieproject.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests for CustomerController.
 */
@WebFluxTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() {
        // Create test data
        CustomerDto customer1 = CustomerDto.createCustomerDto(
                1, 1, "Customer 1", "customer1@example.com", "123-456-7890", 
                Instant.now(), Instant.now());
        CustomerDto customer2 = CustomerDto.createCustomerDto(
                2, 1, "Customer 2", "customer2@example.com", "098-765-4321", 
                Instant.now(), Instant.now());

        // Mock service response
        when(customerService.getAllCustomers()).thenReturn(Flux.just(customer1, customer2));

        // Perform test
        webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerDto.class)
                .hasSize(2);
    }

    @Test
    void testGetCustomerById() {
        // Create test data
        CustomerDto customer = CustomerDto.createCustomerDto(
                1, 1, "Customer 1", "customer1@example.com", "123-456-7890", 
                Instant.now(), Instant.now());

        // Mock service response
        when(customerService.getCustomerById(1)).thenReturn(Mono.just(customer));

        // Perform test
        webTestClient.get()
                .uri("/api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDto.class)
                .isEqualTo(customer);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        // Mock service response
        when(customerService.getCustomerById(anyInt())).thenReturn(Mono.empty());

        // Perform test
        webTestClient.get()
                .uri("/api/v1/customers/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateCustomer() {
        // Create test data
        CustomerDto customerToCreate = CustomerDto.createCustomerDto(
                null, null, "New Customer", "newcustomer@example.com", "555-555-5555", 
                null, null);
        CustomerDto createdCustomer = CustomerDto.createCustomerDto(
                1, 1, "New Customer", "newcustomer@example.com", "555-555-5555", 
                Instant.now(), Instant.now());

        // Mock service response
        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(Mono.just(createdCustomer));

        // Perform test
        webTestClient.post()
                .uri("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerDto.class)
                .isEqualTo(createdCustomer);
    }

    @Test
    void testUpdateCustomer() {
        // Create test data
        CustomerDto customerToUpdate = CustomerDto.createCustomerDto(
                null, null, "Updated Customer", "updated@example.com", "555-555-5555", 
                null, null);
        CustomerDto updatedCustomer = CustomerDto.createCustomerDto(
                1, 2, "Updated Customer", "updated@example.com", "555-555-5555", 
                Instant.now(), Instant.now());

        // Mock service response
        when(customerService.updateCustomer(anyInt(), any(CustomerDto.class))).thenReturn(Mono.just(updatedCustomer));

        // Perform test
        webTestClient.put()
                .uri("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDto.class)
                .isEqualTo(updatedCustomer);
    }

    @Test
    void testDeleteCustomer() {
        // Mock service response
        when(customerService.deleteCustomer(anyInt())).thenReturn(Mono.empty());

        // Perform test
        webTestClient.delete()
                .uri("/api/v1/customers/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}