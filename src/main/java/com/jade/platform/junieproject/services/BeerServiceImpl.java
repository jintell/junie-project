package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.BeerDto;
import com.jade.platform.junieproject.mappers.BeerMapper;
import com.jade.platform.junieproject.model.Beer;
import com.jade.platform.junieproject.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the BeerService interface.
 * Uses BeerRepository for data access operations and BeerMapper for DTO conversions.
 */
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDto> getAllBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> getBeerByName(String beerName) {
        return beerRepository.findByBeerName(beerName)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> createBeer(BeerDto beerDto) {
        // Convert DTO to entity, save it, and convert back to DTO
        return Mono.just(beerDto)
                .map(beerMapper::beerDtoToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> updateBeer(Integer id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .flatMap(existingBeer -> {
                    // Convert DTO to entity, preserving ID and createdOn
                    Beer beer = beerMapper.beerDtoToBeer(beerDto);

                    // Create a new beer with the updated fields but keep the same ID and createdOn
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
                })
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<Void> deleteBeer(Integer id) {
        return beerRepository.deleteById(id);
    }
}
