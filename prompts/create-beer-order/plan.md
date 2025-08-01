# Beer Order System Implementation Plan

## Project Overview

This plan outlines the implementation of a beer ordering system based on the existing project structure. The database schema is already set up with migration files for beer, customer, beer_order, and beer_order_line tables, but the corresponding Java entity models, DTOs, repositories, services, and controllers for Customer, BeerOrder, and BeerOrderLine need to be implemented.

## Current State Analysis

The project currently has:
- Database migration scripts for all required tables (beer, customer, beer_order, beer_order_line)
- Implementation of Beer entity, DTO, repository, service, and controller
- Reactive programming model using Spring WebFlux, R2DBC, and Project Reactor

## Implementation Plan

### 1. Entity Models Implementation

Create the following entity models as Java records with JPA annotations:

#### 1.1 Customer Entity
- Implement as a record with fields matching the database schema
- Include relationship to BeerOrder (OneToMany)
- Add Lombok @Builder annotation with static builder method
- Add no-args constructor for JPA

#### 1.2 BeerOrder Entity
- Implement as a record with fields matching the database schema
- Include relationship to Customer (ManyToOne)
- Include relationship to BeerOrderLine (OneToMany)
- Add Lombok @Builder annotation with static builder method
- Add no-args constructor for JPA

#### 1.3 BeerOrderLine Entity
- Implement as a record with fields matching the database schema
- Include relationship to BeerOrder (ManyToOne)
- Include relationship to Beer (ManyToOne)
- Add Lombok @Builder annotation with static builder method
- Add no-args constructor for JPA

#### 1.4 Update Beer Entity
- Add relationship to BeerOrderLine (OneToMany)

### 2. Data Transfer Objects (DTOs) Implementation

Create the following DTOs as Java records with validation annotations:

#### 2.1 CustomerDto
- Implement as a record with fields matching the Customer entity (excluding relationships)
- Add validation annotations for required fields
- Add Lombok @Builder annotation with static builder method

#### 2.2 BeerOrderDto
- Implement as a record with fields matching the BeerOrder entity
- Use customerId instead of Customer entity
- Include a list of BeerOrderLineDto
- Add validation annotations for required fields
- Add Lombok @Builder annotation with static builder method

#### 2.3 BeerOrderLineDto
- Implement as a record with fields matching the BeerOrderLine entity
- Use orderId and beerId instead of entity references
- Add validation annotations for required fields
- Add Lombok @Builder annotation with static builder method

### 3. Repository Implementation

Create the following reactive repositories:

#### 3.1 CustomerRepository
- Extend R2dbcRepository
- Add method to find by email

#### 3.2 BeerOrderRepository
- Extend R2dbcRepository
- Add method to find all by customer

#### 3.3 BeerOrderLineRepository
- Extend R2dbcRepository
- Add method to find all by beer order

### 4. Mapper Implementation

Create the following MapStruct mappers:

#### 4.1 CustomerMapper
- Add methods to map between Customer and CustomerDto
- Ignore id, createdOn, and updatedOn when mapping from DTO to entity

#### 4.2 BeerOrderMapper
- Add methods to map between BeerOrder and BeerOrderDto
- Handle the relationship between BeerOrder and Customer
- Map customerId to Customer entity and vice versa
- Ignore id, createdOn, and updatedOn when mapping from DTO to entity

#### 4.3 BeerOrderLineMapper
- Add methods to map between BeerOrderLine and BeerOrderLineDto
- Handle the relationships between BeerOrderLine, BeerOrder, and Beer
- Map orderId to BeerOrder entity and beerId to Beer entity
- Ignore id, createdOn, and updatedOn when mapping from DTO to entity

### 5. Service Implementation

Create the following service interfaces and implementations:

#### 5.1 CustomerService Interface and Implementation
- Define methods for CRUD operations
- Implement using reactive programming model (Flux/Mono)

#### 5.2 BeerOrderService Interface and Implementation
- Define methods for CRUD operations
- Implement using reactive programming model (Flux/Mono)
- Include method to create an order with multiple order lines

#### 5.3 BeerOrderLineService Interface and Implementation
- Define methods for CRUD operations
- Implement using reactive programming model (Flux/Mono)

### 6. Controller Implementation

Create the following REST controllers:

#### 6.1 CustomerController
- Implement REST endpoints for CRUD operations
- Follow RESTful principles with proper HTTP status codes
- Use validation for request bodies

#### 6.2 BeerOrderController
- Implement REST endpoints for CRUD operations
- Follow RESTful principles with proper HTTP status codes
- Use validation for request bodies
- Include endpoint to create an order with multiple order lines

### 7. Testing

#### 7.1 Unit Tests
- Create unit tests for all services using WebFluxTest
- Mock repositories and mappers

#### 7.2 Integration Tests
- Create integration tests for repositories using TestContainers
- Create integration tests for controllers using WebTestClient

### 8. Documentation

- Add Javadoc comments to all classes and methods
- Update README.md with information about the new functionality

## Implementation Approach

The implementation will follow these principles:

1. **Reactive Programming**: Use reactive programming model with Flux and Mono throughout the application.
2. **Immutability**: Use Java records for entities and DTOs to ensure immutability.
3. **Validation**: Use Jakarta Validation annotations to validate input data.
4. **Separation of Concerns**: Keep clear separation between entities, DTOs, repositories, services, and controllers.
5. **Testing**: Write comprehensive tests for all components.

## Dependencies

The implementation will use the following dependencies:
- Spring Boot
- Spring WebFlux
- Spring Data R2DBC
- Project Reactor
- MapStruct
- Lombok
- Jakarta Validation API
- TestContainers (for testing)

## Timeline

The implementation is estimated to take approximately 2-3 weeks, with the following breakdown:
- Entity Models and DTOs: 2-3 days
- Repositories and Mappers: 2-3 days
- Services: 3-4 days
- Controllers: 2-3 days
- Testing: 3-5 days
- Documentation and Cleanup: 1-2 days