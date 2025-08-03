package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.BeerOrderDto;
import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import com.jade.platform.junieproject.model.BeerOrder;
import com.jade.platform.junieproject.model.Customer;
import com.jade.platform.junieproject.repository.CustomerRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between BeerOrder entity and BeerOrderDto.
 * Uses MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
        uses = {BeerOrderLineMapper.class})
public abstract class BeerOrderMapper {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private BeerOrderLineMapper beerOrderLineMapper;
    
    /**
     * Convert a BeerOrder entity to a BeerOrderDto.
     * Maps the customer.id to customerId.
     * 
     * @param beerOrder the BeerOrder entity to convert
     * @return the corresponding BeerOrderDto
     */
    @Mapping(target = "customerId", source = "customer.id")
    public abstract BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);
    
    /**
     * Convert a BeerOrderDto to a BeerOrder entity.
     * Ignores id, createdOn, and updatedOn fields which are managed by the database.
     * 
     * @param beerOrderDto the BeerOrderDto to convert
     * @return the corresponding BeerOrder entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "customer", source = "customerId")
    @Mapping(target = "orderLines", ignore = true)
    public abstract BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
    
    /**
     * Maps a customer ID to a Customer entity.
     * 
     * @param id the customer ID
     * @return the corresponding Customer entity
     */
    Customer mapCustomerIdToCustomer(Integer id) {
        if (id == null) {
            return null;
        }
        return Customer.createCustomer(id, null, null, null, null, null, null, null);
    }
    
    /**
     * Custom implementation to handle the order lines relationship properly.
     * Since we're using records, we need to create a new instance with all fields set correctly.
     * This method is called by MapStruct after the abstract method.
     * 
     * @param beerOrderDto the source BeerOrderDto
     * @return a fully mapped BeerOrder entity
     */
    @AfterMapping
    protected BeerOrder afterBeerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto, @MappingTarget BeerOrder beerOrder) {
        if (beerOrderDto == null) {
            return null;
        }
        
        Customer customer = mapCustomerIdToCustomer(beerOrderDto.customerId());
        
        // Create the BeerOrder without order lines first
        return BeerOrder.createBeerOrder(
            null, // id is ignored for new entities
            beerOrderDto.version(),
            beerOrderDto.orderStatus(),
            customer,
            null, // orderLines will be set later
            null, // createdOn is managed by the database
            null  // updatedOn is managed by the database
        );
    }
}