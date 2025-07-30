package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.dtos.BeerDto;
import com.jade.platform.junieproject.services.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive REST controller for Beer operations.
 * Provides endpoints for CRUD operations on Beer DTOs.
 */
@RestController
@RequestMapping("/api/v1/beers")
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    /**
     * Get all beers.
     * 
     * @return a Flux of all beers
     */
    @GetMapping
    public Flux<BeerDto> getAllBeers() {
        return beerService.getAllBeers();
    }

    /**
     * Get a beer by its ID.
     * 
     * @param id the beer ID
     * @return a Mono containing the beer if found, or a 404 response if not found
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BeerDto>> getBeerById(@PathVariable Integer id) {
        return beerService.getBeerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Get a beer by its name.
     * 
     * @param beerName the beer name
     * @return a Mono containing the beer if found, or a 404 response if not found
     */
    @GetMapping("/name/{beerName}")
    public Mono<ResponseEntity<BeerDto>> getBeerByName(@PathVariable String beerName) {
        return beerService.getBeerByName(beerName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Create a new beer.
     * 
     * @param beerDto the beer DTO to create
     * @return a Mono containing the created beer DTO with a 201 status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BeerDto> createBeer(@Valid @RequestBody BeerDto beerDto) {
        return beerService.createBeer(beerDto);
    }

    /**
     * Update an existing beer.
     * 
     * @param id the ID of the beer to update
     * @param beerDto the updated beer DTO data
     * @return a Mono containing the updated beer DTO, or a 404 response if not found
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BeerDto>> updateBeer(@PathVariable Integer id, @Valid @RequestBody BeerDto beerDto) {
        return beerService.updateBeer(id, beerDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Delete a beer by its ID.
     * 
     * @param id the ID of the beer to delete
     * @return a Mono that completes with a 204 status when the beer is deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBeer(@PathVariable Integer id) {
        return beerService.deleteBeer(id);
    }
}
