package com.jade.platform.junieproject.controllers;

import com.jade.platform.junieproject.model.Beer;
import com.jade.platform.junieproject.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive REST controller for Beer operations.
 * Provides endpoints for CRUD operations on Beer entities.
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
    public Flux<Beer> getAllBeers() {
        return beerService.getAllBeers();
    }

    /**
     * Get a beer by its ID.
     * 
     * @param id the beer ID
     * @return a Mono containing the beer if found, or a 404 response if not found
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Beer>> getBeerById(@PathVariable Integer id) {
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
    public Mono<ResponseEntity<Beer>> getBeerByName(@PathVariable String beerName) {
        return beerService.getBeerByName(beerName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Create a new beer.
     * 
     * @param beer the beer to create
     * @return a Mono containing the created beer with a 201 status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Beer> createBeer(@RequestBody Beer beer) {
        return beerService.createBeer(beer);
    }

    /**
     * Update an existing beer.
     * 
     * @param id the ID of the beer to update
     * @param beer the updated beer data
     * @return a Mono containing the updated beer, or a 404 response if not found
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Beer>> updateBeer(@PathVariable Integer id, @RequestBody Beer beer) {
        return beerService.updateBeer(id, beer)
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