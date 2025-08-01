package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import com.jade.platform.junieproject.model.Beer;
import com.jade.platform.junieproject.model.BeerOrder;
import com.jade.platform.junieproject.model.BeerOrderLine;
import com.jade.platform.junieproject.repository.BeerOrderRepository;
import com.jade.platform.junieproject.repository.BeerRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper interface for converting between BeerOrderLine entity and BeerOrderLineDto.
 * Uses MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BeerOrderLineMapper {
    
    @Autowired
    private BeerRepository beerRepository;
    
    @Autowired
    private BeerOrderRepository beerOrderRepository;
    
    /**
     * Convert a BeerOrderLine entity to a BeerOrderLineDto.
     * Maps the beer.id to beerId and beerOrder.id to orderId.
     * 
     * @param beerOrderLine the BeerOrderLine entity to convert
     * @return the corresponding BeerOrderLineDto
     */
    @Mapping(target = "beerId", source = "beer.id")
    @Mapping(target = "orderId", source = "beerOrder.id")
    public abstract BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);
    
    /**
     * Convert a BeerOrderLineDto to a BeerOrderLine entity.
     * Ignores id, createdOn, and updatedOn fields which are managed by the database.
     * 
     * @param beerOrderLineDto the BeerOrderLineDto to convert
     * @return the corresponding BeerOrderLine entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "beer", source = "beerId")
    @Mapping(target = "beerOrder", source = "orderId")
    public abstract BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
    
    /**
     * Maps a beer ID to a Beer entity.
     * 
     * @param id the beer ID
     * @return the corresponding Beer entity
     */
    Beer mapBeerIdToBeer(Integer id) {
        if (id == null) {
            return null;
        }
        return Beer.createBeer(id, null, null, null, null, null, null, null, null, null);
    }
    
    /**
     * Maps a beer order ID to a BeerOrder entity.
     * 
     * @param id the beer order ID
     * @return the corresponding BeerOrder entity
     */
    BeerOrder mapOrderIdToBeerOrder(Integer id) {
        if (id == null) {
            return null;
        }
        return BeerOrder.createBeerOrder(id, null, null, null, null, null, null);
    }
}