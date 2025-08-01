package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.BeerOrderDto;
import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import com.jade.platform.junieproject.services.BeerOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests for BeerOrderController.
 */
@WebFluxTest(BeerOrderController.class)
public class BeerOrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BeerOrderService beerOrderService;

    @Test
    void testGetAllOrders() {
        // Create test data
        BeerOrderDto order1 = createTestOrder(1, 1, "NEW", 1);
        BeerOrderDto order2 = createTestOrder(2, 1, "PROCESSING", 2);

        // Mock service response
        when(beerOrderService.getAllOrders()).thenReturn(Flux.just(order1, order2));

        // Perform test
        webTestClient.get()
                .uri("/api/v1/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerOrderDto.class)
                .hasSize(2);
    }

    @Test
    void testGetOrdersByCustomerId() {
        // Create test data
        BeerOrderDto order1 = createTestOrder(1, 1, "NEW", 1);
        BeerOrderDto order2 = createTestOrder(2, 1, "PROCESSING", 1);

        // Mock service response
        when(beerOrderService.getOrdersByCustomerId(1)).thenReturn(Flux.just(order1, order2));

        // Perform test
        webTestClient.get()
                .uri("/api/v1/customers/1/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerOrderDto.class)
                .hasSize(2);
    }

    @Test
    void testGetOrderById() {
        // Create test data
        BeerOrderDto order = createTestOrder(1, 1, "NEW", 1);

        // Mock service response
        when(beerOrderService.getOrderById(1)).thenReturn(Mono.just(order));

        // Perform test
        webTestClient.get()
                .uri("/api/v1/orders/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerOrderDto.class)
                .isEqualTo(order);
    }

    @Test
    void testGetOrderByIdNotFound() {
        // Mock service response
        when(beerOrderService.getOrderById(anyInt())).thenReturn(Mono.empty());

        // Perform test
        webTestClient.get()
                .uri("/api/v1/orders/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateOrder() {
        // Create test data
        BeerOrderDto orderToCreate = createTestOrder(null, null, "NEW", 1);
        BeerOrderDto createdOrder = createTestOrder(1, 1, "NEW", 1);

        // Mock service response
        when(beerOrderService.createOrder(any(BeerOrderDto.class))).thenReturn(Mono.just(createdOrder));

        // Perform test
        webTestClient.post()
                .uri("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BeerOrderDto.class)
                .isEqualTo(createdOrder);
    }

    @Test
    void testUpdateOrder() {
        // Create test data
        BeerOrderDto orderToUpdate = createTestOrder(null, null, "PROCESSING", 1);
        BeerOrderDto updatedOrder = createTestOrder(1, 2, "PROCESSING", 1);

        // Mock service response
        when(beerOrderService.updateOrder(anyInt(), any(BeerOrderDto.class))).thenReturn(Mono.just(updatedOrder));

        // Perform test
        webTestClient.put()
                .uri("/api/v1/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerOrderDto.class)
                .isEqualTo(updatedOrder);
    }

    @Test
    void testDeleteOrder() {
        // Mock service response
        when(beerOrderService.deleteOrder(anyInt())).thenReturn(Mono.empty());

        // Perform test
        webTestClient.delete()
                .uri("/api/v1/orders/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    /**
     * Helper method to create a test BeerOrderDto.
     */
    private BeerOrderDto createTestOrder(Integer id, Integer version, String status, Integer customerId) {
        BeerOrderLineDto line1 = BeerOrderLineDto.createBeerOrderLineDto(
                1, 1, id, 1, 10, Instant.now(), Instant.now());
        BeerOrderLineDto line2 = BeerOrderLineDto.createBeerOrderLineDto(
                2, 1, id, 2, 20, Instant.now(), Instant.now());

        return BeerOrderDto.createBeerOrderDto(
                id, 
                version, 
                status, 
                customerId, 
                List.of(line1, line2), 
                Instant.now(), 
                Instant.now());
    }
}