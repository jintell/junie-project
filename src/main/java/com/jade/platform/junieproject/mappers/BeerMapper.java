package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.BeerDto;
import com.jade.platform.junieproject.model.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Beer entity and BeerDto.
 */
@Mapper(componentModel = "spring")
public interface BeerMapper {

    /**
     * Convert from Beer entity to BeerDto.
     * 
     * @param beer the Beer entity
     * @return the BeerDto
     */
    BeerDto beerToBeerDto(Beer beer);

    /**
     * Convert from BeerDto to Beer entity.
     * Ignores id, createdOn, and updatedOn when mapping from DTO to entity.
     * 
     * @param beerDto the BeerDto
     * @return the Beer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}
