package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import com.jade.platform.junieproject.mappers.BeerOrderLineMapper;
import com.jade.platform.junieproject.model.BeerOrderLine;
import com.jade.platform.junieproject.repository.BeerOrderLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of BeerOrderLineService interface.
 * Provides reactive CRUD operations for BeerOrderLine entities.
 */
@Service
public class BeerOrderLineServiceImpl implements BeerOrderLineService {
    
    private final BeerOrderLineRepository beerOrderLineRepository;
    private final BeerOrderLineMapper beerOrderLineMapper;
    
    /**
     * Constructor for BeerOrderLineServiceImpl.
     * 
     * @param beerOrderLineRepository the repository for BeerOrderLine entities
     * @param beerOrderLineMapper the mapper for converting between BeerOrderLine and BeerOrderLineDto
     */
    public BeerOrderLineServiceImpl(
            BeerOrderLineRepository beerOrderLineRepository,
            BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineRepository = beerOrderLineRepository;
        this.beerOrderLineMapper = beerOrderLineMapper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Flux<BeerOrderLineDto> getAllOrderLines() {
        return beerOrderLineRepository.findAll()
                .map(beerOrderLineMapper::beerOrderLineToBeerOrderLineDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Flux<BeerOrderLineDto> getOrderLinesByOrderId(Integer orderId) {
        return beerOrderLineRepository.findAllByBeerOrderId(orderId)
                .map(beerOrderLineMapper::beerOrderLineToBeerOrderLineDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Mono<BeerOrderLineDto> getOrderLineById(Integer id) {
        return beerOrderLineRepository.findById(id)
                .map(beerOrderLineMapper::beerOrderLineToBeerOrderLineDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<BeerOrderLineDto> createOrderLine(BeerOrderLineDto beerOrderLineDto) {
        return Mono.just(beerOrderLineDto)
                .map(beerOrderLineMapper::beerOrderLineDtoToBeerOrderLine)
                .flatMap(beerOrderLineRepository::save)
                .map(beerOrderLineMapper::beerOrderLineToBeerOrderLineDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<BeerOrderLineDto> updateOrderLine(Integer id, BeerOrderLineDto beerOrderLineDto) {
        return beerOrderLineRepository.findById(id)
                .flatMap(existingOrderLine -> {
                    BeerOrderLine updatedOrderLine = BeerOrderLine.createBeerOrderLine(
                            existingOrderLine.id(),
                            existingOrderLine.version(),
                            existingOrderLine.beerOrder(),
                            existingOrderLine.beer(),
                            beerOrderLineDto.orderQuantity(),
                            existingOrderLine.createdOn(),
                            existingOrderLine.updatedOn()
                    );
                    return beerOrderLineRepository.save(updatedOrderLine);
                })
                .map(beerOrderLineMapper::beerOrderLineToBeerOrderLineDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<Void> deleteOrderLine(Integer id) {
        return beerOrderLineRepository.deleteById(id);
    }
}