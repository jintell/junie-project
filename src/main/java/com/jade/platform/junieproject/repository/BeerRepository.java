package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.Beer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Beer entity.
 * Provides reactive CRUD operations for the Beer entity using R2DBC.
 */
@Repository
public interface BeerRepository extends R2dbcRepository<Beer, Integer> {

    /**
     * Find beers by beer style.
     * 
     * @param beerStyle the beer style to search for
     * @return a Flux of beers with the given style
     */
    Flux<Beer> findByBeerStyle(String beerStyle);

    /**
     * Find a beer by its name.
     * 
     * @param beerName the beer name to search for
     * @return a Mono of the beer with the given name
     */
    Mono<Beer> findByBeerName(String beerName);
}
