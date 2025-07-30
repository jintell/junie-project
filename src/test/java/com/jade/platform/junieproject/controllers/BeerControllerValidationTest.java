package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.BeerDto;
import com.jade.platform.junieproject.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BeerControllerValidationTest {

    private WebTestClient webTestClient;
    private BeerService beerService;
    private BeerController beerController;

    @BeforeEach
    void setUp() {
        // Create a mock BeerService
        beerService = Mockito.mock(BeerService.class);

        // Create the controller with the mock service
        beerController = new BeerController(beerService);

        // Create WebTestClient bound to the controller
        webTestClient = WebTestClient
                .bindToController(beerController)
                .configureClient()
                .baseUrl("")
                .build();

        // Setup mock service to return a valid beer for any input
        when(beerService.createBeer(any(BeerDto.class))).thenReturn(
                Mono.just(BeerDto.createBeerDto(1, 1, "Test Beer", "IPA", "12345", 100, new BigDecimal("12.99"), null, null))
        );
    }

    @Test
    void testCreateBeer_EmptyBeerName() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "", // Empty beer name
                "IPA",
                "12345",
                100,
                new BigDecimal("12.99"),
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_EmptyBeerStyle() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "", // Empty beer style
                "12345",
                100,
                new BigDecimal("12.99"),
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_EmptyUpc() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "IPA",
                "", // Empty UPC
                100,
                new BigDecimal("12.99"),
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_NullQuantity() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "IPA",
                "12345",
                null, // Null quantity
                new BigDecimal("12.99"),
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_NegativeQuantity() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "IPA",
                "12345",
                -10, // Negative quantity
                new BigDecimal("12.99"),
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_NullPrice() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "IPA",
                "12345",
                100,
                null, // Null price
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_NegativePrice() {
        BeerDto invalidBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "IPA",
                "12345",
                100,
                new BigDecimal("-12.99"), // Negative price
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBeerDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer_ValidBeer() {
        BeerDto validBeerDto = BeerDto.createBeerDto(
                null,
                null,
                "Test Beer",
                "IPA",
                "12345",
                100,
                new BigDecimal("12.99"),
                null,
                null
        );

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validBeerDto)
                .exchange()
                .expectStatus().isCreated();
    }
}