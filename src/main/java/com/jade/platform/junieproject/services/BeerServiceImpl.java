package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.model.Beer;
import com.jade.platform.junieproject.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the BeerService interface.
 * Uses BeerRepository for data access operations.
 */
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    @Override
    public Flux<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    @Override
    public Mono<Beer> getBeerById(Integer id) {
        return beerRepository.findById(id);
    }

    @Override
    public Mono<Beer> getBeerByName(String beerName) {
        return beerRepository.findByBeerName(beerName);
    }

    @Override
    public Mono<Beer> createBeer(Beer beer) {
        // Ensure we're creating a new beer, not updating an existing one
        return Mono.just(beer)
                .filter(b -> b.id() == null)
                .flatMap(beerRepository::save)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Beer ID must be null for creation")));
    }

    @Override
    public Mono<Beer> updateBeer(Integer id, Beer beer) {
        return beerRepository.findById(id)
                .flatMap(existingBeer -> {
                    // Create a new beer with the updated fields but keep the same ID
                    Beer updatedBeer = Beer.createBeer(
                            id,
                            beer.version(),
                            beer.beerName(),
                            beer.beerStyle(),
                            beer.upc(),
                            beer.quantityOnHand(),
                            beer.unitPrice(),
                            existingBeer.createdOn(),
                            null // Let the @UpdateTimestamp handle this
                    );
                    return beerRepository.save(updatedBeer);
                });
    }

    @Override
    public Mono<Void> deleteBeer(Integer id) {
        return beerRepository.deleteById(id);
    }
}