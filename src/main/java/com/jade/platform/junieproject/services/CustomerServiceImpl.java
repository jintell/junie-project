package com.jade.platform.junieproject.services;

import com.jade.platform.junieproject.dtos.CustomerDto;
import com.jade.platform.junieproject.mappers.CustomerMapper;
import com.jade.platform.junieproject.model.Customer;
import com.jade.platform.junieproject.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of CustomerService interface.
 * Provides reactive CRUD operations for Customer entities.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    /**
     * Constructor for CustomerServiceImpl.
     * 
     * @param customerRepository the repository for Customer entities
     * @param customerMapper the mapper for converting between Customer and CustomerDto
     */
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Flux<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Mono<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Mono<CustomerDto> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customerMapper::customerToCustomerDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<CustomerDto> createCustomer(CustomerDto customerDto) {
        return Mono.just(customerDto)
                .map(customerMapper::customerDtoToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .flatMap(existingCustomer -> {
                    Customer updatedCustomer = Customer.createCustomer(
                            existingCustomer.id(),
                            existingCustomer.version(),
                            customerDto.name(),
                            customerDto.email(),
                            customerDto.phone(),
                            existingCustomer.createdOn(),
                            existingCustomer.updatedOn(),
                            existingCustomer.orders()
                    );
                    return customerRepository.save(updatedCustomer);
                })
                .map(customerMapper::customerToCustomerDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Mono<Void> deleteCustomer(Integer id) {
        return customerRepository.deleteById(id);
    }
}