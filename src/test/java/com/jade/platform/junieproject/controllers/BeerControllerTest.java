package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.BeerDto;
import com.jade.platform.junieproject.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    private WebTestClient webTestClient;
    
    @Mock
    private BeerService beerService;
    
    private BeerController beerController;
    private BeerDto testBeerDto;

    @BeforeEach
    void setUp() {
        // Create the controller with the mock service
        beerController = new BeerController(beerService);

        // Create WebTestClient bound to the controller
        webTestClient = WebTestClient
                .bindToController(beerController)
                .configureClient()
                .baseUrl("")  // Empty base URL since controller already has @RequestMapping("/api/v1/beers")
                .build();

        // Create test beer DTO
        testBeerDto = BeerDto.createBeerDto(
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
    }

    @Test
    void getAllBeers() {
        Flux<BeerDto> beerFlux = Flux.just(testBeerDto);
        when(beerService.getAllBeers(null, 0, 25)).thenReturn(Mono.just(beerFlux));

        webTestClient.get()
                .uri("/api/v1/beers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerDto.class)
                .hasSize(1)
                .contains(testBeerDto);

        verify(beerService).getAllBeers(null, 0, 25);
    }
    
    @Test
    void getAllBeersWithBeerNameFilter() {
        Flux<BeerDto> beerFlux = Flux.just(testBeerDto);
        when(beerService.getAllBeers("Test", 0, 25)).thenReturn(Mono.just(beerFlux));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/beers")
                        .queryParam("beerName", "Test")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerDto.class)
                .hasSize(1)
                .contains(testBeerDto);

        verify(beerService).getAllBeers("Test", 0, 25);
    }
    
    @Test
    void getAllBeersWithPagination() {
        Flux<BeerDto> beerFlux = Flux.just(testBeerDto);
        when(beerService.getAllBeers(null, 1, 10)).thenReturn(Mono.just(beerFlux));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/beers")
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerDto.class)
                .hasSize(1)
                .contains(testBeerDto);

        verify(beerService).getAllBeers(null, 1, 10);
    }

    @Test
    void getBeerById_Found() {
        when(beerService.getBeerById(1)).thenReturn(Mono.just(testBeerDto));

        webTestClient.get()
                .uri("/api/v1/beers/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .isEqualTo(testBeerDto);

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
        when(beerService.getBeerByName("Test Beer")).thenReturn(Mono.just(testBeerDto));

        webTestClient.get()
                .uri("/api/v1/beers/name/Test Beer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .isEqualTo(testBeerDto);

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
        BeerDto newBeerDto = BeerDto.createBeerDto(
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

        BeerDto savedBeerDto = BeerDto.createBeerDto(
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

        when(beerService.createBeer(any(BeerDto.class))).thenReturn(Mono.just(savedBeerDto));

        webTestClient.post()
                .uri("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBeerDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BeerDto.class)
                .isEqualTo(savedBeerDto);

        verify(beerService).createBeer(any(BeerDto.class));
    }

    @Test
    void updateBeer_Found() {
        BeerDto updatedBeerDto = BeerDto.createBeerDto(
                1,
                2,
                "Updated Beer",
                "Lager",
                "12345",
                75,
                new BigDecimal("14.99"),
                testBeerDto.createdOn(),
                Instant.now()
        );

        when(beerService.updateBeer(anyInt(), any(BeerDto.class))).thenReturn(Mono.just(updatedBeerDto));

        webTestClient.put()
                .uri("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBeerDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .isEqualTo(updatedBeerDto);

        verify(beerService).updateBeer(anyInt(), any(BeerDto.class));
    }

    @Test
    void updateBeer_NotFound() {
        BeerDto updatedBeerDto = BeerDto.createBeerDto(
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

        when(beerService.updateBeer(999, updatedBeerDto)).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBeerDto)
                .exchange()
                .expectStatus().isNotFound();

        verify(beerService).updateBeer(999, updatedBeerDto);
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
