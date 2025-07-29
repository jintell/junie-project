package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.model.Beer;
import com.jade.platform.junieproject.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BeerControllerTest {

    private WebTestClient webTestClient;
    private BeerService beerService;
    private BeerController beerController;

    private Beer testBeer;

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
                .baseUrl("")  // Empty base URL since controller already has @RequestMapping("/api/v1/beers")
                .build();

        // Create test beer
        testBeer = Beer.createBeer(
                1,
                1,
                "Test Beer",
                "IPA",
                "12345",
                100,
                new BigDecimal("12.99"),
                Instant.now(),
                Instant.now()
        );

        // Reset the mock before each test
        Mockito.reset(beerService);
    }

    @Test
    void getAllBeers() {
        when(beerService.getAllBeers()).thenReturn(Flux.just(testBeer));

        webTestClient.get()
                .uri("/api/v1/beers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Beer.class)
                .hasSize(1)
                .contains(testBeer);

        verify(beerService).getAllBeers();
    }

    @Test
    void getBeerById_Found() {
        when(beerService.getBeerById(1)).thenReturn(Mono.just(testBeer));

        webTestClient.get()
                .uri("/api/v1/beers/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Beer.class)
                .isEqualTo(testBeer);

        verify(beerService).getBeerById(1);
    }

    @Test
    void getBeerById_NotFound() {
        when(beerService.getBeerById(999)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/beers/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        verify(beerService).getBeerById(999);
    }

    @Test
    void getBeerByName_Found() {
        when(beerService.getBeerByName("Test Beer")).thenReturn(Mono.just(testBeer));

        webTestClient.get()
                .uri("/api/v1/beers/name/Test Beer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Beer.class)
                .isEqualTo(testBeer);

        verify(beerService).getBeerByName("Test Beer");
    }

    @Test
    void getBeerByName_NotFound() {
        when(beerService.getBeerByName("Non-existent Beer")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/beers/name/Non-existent Beer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        verify(beerService).getBeerByName("Non-existent Beer");
    }

    @Test
    void createBeer() {
        Beer newBeer = Beer.createBeer(
                null,
                null,
                "New Beer",
                "Stout",
                "67890",
                50,
                new BigDecimal("9.99"),
                null,
                null
        );

        Beer savedBeer = Beer.createBeer(
                2,
                1,
                "New Beer",
                "Stout",
                "67890",
                50,
                new BigDecimal("9.99"),
                Instant.now(),
                Instant.now()
        );

        when(beerService.createBeer(any(Beer.class))).thenReturn(Mono.just(savedBeer));

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBeer)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Beer.class)
                .isEqualTo(savedBeer);

        verify(beerService).createBeer(any(Beer.class));
    }

    @Test
    void updateBeer_Found() {
        Beer updatedBeer = Beer.createBeer(
                1,
                2,
                "Updated Beer",
                "Lager",
                "12345",
                75,
                new BigDecimal("14.99"),
                testBeer.createdOn(),
                Instant.now()
        );

        when(beerService.updateBeer(anyInt(), any(Beer.class))).thenReturn(Mono.just(updatedBeer));

        webTestClient.put()
                .uri("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBeer)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Beer.class)
                .isEqualTo(updatedBeer);

        verify(beerService).updateBeer(anyInt(), any(Beer.class));
    }

    @Test
    void updateBeer_NotFound() {
        Beer updatedBeer = Beer.createBeer(
                999,
                1,
                "Updated Beer",
                "Lager",
                "12345",
                75,
                new BigDecimal("14.99"),
                Instant.now(),
                Instant.now()
        );

        when(beerService.updateBeer(999, updatedBeer)).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBeer)
                .exchange()
                .expectStatus().isNotFound();

        verify(beerService).updateBeer(999, updatedBeer);
    }

    @Test
    void deleteBeer() {
        when(beerService.deleteBeer(1)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/beers/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(beerService).deleteBeer(1);
    }
}
