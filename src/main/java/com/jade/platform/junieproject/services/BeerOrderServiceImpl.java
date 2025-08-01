package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.BeerOrderDto;
import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import com.jade.platform.junieproject.mappers.BeerOrderLineMapper;
import com.jade.platform.junieproject.mappers.BeerOrderMapper;
import com.jade.platform.junieproject.model.BeerOrder;
import com.jade.platform.junieproject.model.BeerOrderLine;
import com.jade.platform.junieproject.repository.BeerOrderLineRepository;
import com.jade.platform.junieproject.repository.BeerOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of BeerOrderService interface.
 * Provides reactive CRUD operations for BeerOrder entities.
 */
@Service
public class BeerOrderServiceImpl implements BeerOrderService {
    
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderLineRepository beerOrderLineRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final BeerOrderLineMapper beerOrderLineMapper;
    
    /**
     * Constructor for BeerOrderServiceImpl.
     * 
     * @param beerOrderRepository the repository for BeerOrder entities
     * @param beerOrderLineRepository the repository for BeerOrderLine entities
     * @param beerOrderMapper the mapper for converting between BeerOrder and BeerOrderDto
     * @param beerOrderLineMapper the mapper for converting between BeerOrderLine and BeerOrderLineDto
     */
    public BeerOrderServiceImpl(
            BeerOrderRepository beerOrderRepository,
            BeerOrderLineRepository beerOrderLineRepository,
            BeerOrderMapper beerOrderMapper,
            BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.beerOrderLineRepository = beerOrderLineRepository;
        this.beerOrderMapper = beerOrderMapper;
        this.beerOrderLineMapper = beerOrderLineMapper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Flux<BeerOrderDto> getAllOrders() {
        return beerOrderRepository.findAll()
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Flux<BeerOrderDto> getOrdersByCustomerId(Integer customerId) {
        return beerOrderRepository.findAllByCustomerId(customerId)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Mono<BeerOrderDto> getOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<BeerOrderDto> createOrder(BeerOrderDto beerOrderDto) {
        return Mono.just(beerOrderDto)
                .map(beerOrderMapper::beerOrderDtoToBeerOrder)
                .flatMap(beerOrderRepository::save)
                .flatMap(savedOrder -> {
                    List<BeerOrderLineDto> orderLineDtos = beerOrderDto.orderLines();
                    
                    return Flux.fromIterable(orderLineDtos)
                            .map(lineDto -> {
                                // Set the order ID for each line
                                return BeerOrderLineDto.createBeerOrderLineDto(
                                        lineDto.id(),
                                        lineDto.version(),
                                        savedOrder.id(),
                                        lineDto.beerId(),
                                        lineDto.orderQuantity(),
                                        lineDto.createdOn(),
                                        lineDto.updatedOn()
                                );
                            })
                            .map(beerOrderLineMapper::beerOrderLineDtoToBeerOrderLine)
                            .flatMap(beerOrderLineRepository::save)
                            .then(Mono.just(savedOrder));
                })
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<BeerOrderDto> updateOrder(Integer id, BeerOrderDto beerOrderDto) {
        return beerOrderRepository.findById(id)
                .flatMap(existingOrder -> {
                    BeerOrder updatedOrder = BeerOrder.createBeerOrder(
                            existingOrder.id(),
                            existingOrder.version(),
                            beerOrderDto.orderStatus(),
                            existingOrder.customer(),
                            existingOrder.orderLines(),
                            existingOrder.createdOn(),
                            existingOrder.updatedOn()
                    );
                    return beerOrderRepository.save(updatedOrder);
                })
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<Void> deleteOrder(Integer id) {
        // First delete all order lines for this order
        return beerOrderLineRepository.findAllByBeerOrderId(id)
                .flatMap(beerOrderLineRepository::delete)
                .then(beerOrderRepository.deleteById(id));
    }
}