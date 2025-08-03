package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.CustomerDto;
import com.jade.platform.junieproject.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper interface for converting between Customer entity and CustomerDto.
 * Uses MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    
    /**
     * Convert a Customer entity to a CustomerDto.
     * 
     * @param customer the Customer entity to convert
     * @return the corresponding CustomerDto
     */
    CustomerDto customerToCustomerDto(Customer customer);
    
    /**
     * Convert a CustomerDto to a Customer entity.
     * Ignores id, createdOn, and updatedOn fields which are managed by the database.
     * 
     * @param customerDto the CustomerDto to convert
     * @return the corresponding Customer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}