package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.BeerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for Beer operations.
 * Provides reactive methods for CRUD operations on Beer DTOs.
 */
public interface BeerService {

    /**
     * Get all beers.
     * 
     * @return a Flux of all beers
     */
    Flux<BeerDto> getAllBeers();

    /**
     * Get a beer by its ID.
     * 
     * @param id the beer ID
     * @return a Mono containing the beer if found, or empty if not found
     */
    Mono<BeerDto> getBeerById(Integer id);

    /**
     * Get a beer by its name.
     * 
     * @param beerName the beer name
     * @return a Mono containing the beer if found, or empty if not found
     */
    Mono<BeerDto> getBeerByName(String beerName);

    /**
     * Create a new beer.
     * 
     * @param beerDto the beer DTO to create
     * @return a Mono containing the created beer DTO
     */
    Mono<BeerDto> createBeer(BeerDto beerDto);

    /**
     * Update an existing beer.
     * 
     * @param id the ID of the beer to update
     * @param beerDto the updated beer DTO data
     * @return a Mono containing the updated beer DTO, or empty if not found
     */
    Mono<BeerDto> updateBeer(Integer id, BeerDto beerDto);

    /**
     * Delete a beer by its ID.
     * 
     * @param id the ID of the beer to delete
     * @return a Mono that completes when the beer is deleted
     */
    Mono<Void> deleteBeer(Integer id);
}
