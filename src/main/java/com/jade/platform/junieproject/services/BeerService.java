package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.model.Beer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for Beer operations.
 * Provides reactive methods for CRUD operations on Beer entities.
 */
public interface BeerService {
    
    /**
     * Get all beers.
     * 
     * @return a Flux of all beers
     */
    Flux<Beer> getAllBeers();
    
    /**
     * Get a beer by its ID.
     * 
     * @param id the beer ID
     * @return a Mono containing the beer if found, or empty if not found
     */
    Mono<Beer> getBeerById(Integer id);
    
    /**
     * Get a beer by its name.
     * 
     * @param beerName the beer name
     * @return a Mono containing the beer if found, or empty if not found
     */
    Mono<Beer> getBeerByName(String beerName);
    
    /**
     * Create a new beer.
     * 
     * @param beer the beer to create
     * @return a Mono containing the created beer
     */
    Mono<Beer> createBeer(Beer beer);
    
    /**
     * Update an existing beer.
     * 
     * @param id the ID of the beer to update
     * @param beer the updated beer data
     * @return a Mono containing the updated beer, or empty if not found
     */
    Mono<Beer> updateBeer(Integer id, Beer beer);
    
    /**
     * Delete a beer by its ID.
     * 
     * @param id the ID of the beer to delete
     * @return a Mono that completes when the beer is deleted
     */
    Mono<Void> deleteBeer(Integer id);
}