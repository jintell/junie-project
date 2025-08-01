# Beer Order System Implementation Tasks

## 1. Entity Models Implementation

### 1.1 Customer Entity
- [*] Create Customer record with fields matching the database schema
- [*] Add relationship to BeerOrder (OneToMany)
- [*] Add Lombok @Builder annotation with static builder method
- [*] Add no-args constructor for JPA

### 1.2 BeerOrder Entity
- [*] Create BeerOrder record with fields matching the database schema
- [*] Add relationship to Customer (ManyToOne)
- [*] Add relationship to BeerOrderLine (OneToMany)
- [*] Add Lombok @Builder annotation with static builder method
- [*] Add no-args constructor for JPA

### 1.3 BeerOrderLine Entity
- [*] Create BeerOrderLine record with fields matching the database schema
- [*] Add relationship to BeerOrder (ManyToOne)
- [*] Add relationship to Beer (ManyToOne)
- [*] Add Lombok @Builder annotation with static builder method
- [*] Add no-args constructor for JPA

### 1.4 Update Beer Entity
- [*] Add relationship to BeerOrderLine (OneToMany)

## 2. Data Transfer Objects (DTOs) Implementation

### 2.1 CustomerDto
- [*] Create CustomerDto record with fields matching the Customer entity (excluding relationships)
- [*] Add validation annotations for required fields
- [*] Add Lombok @Builder annotation with static builder method

### 2.2 BeerOrderDto
- [*] Create BeerOrderDto record with fields matching the BeerOrder entity
- [*] Use customerId instead of Customer entity
- [*] Include a list of BeerOrderLineDto
- [*] Add validation annotations for required fields
- [*] Add Lombok @Builder annotation with static builder method

### 2.3 BeerOrderLineDto
- [*] Create BeerOrderLineDto record with fields matching the BeerOrderLine entity
- [*] Use orderId and beerId instead of entity references
- [*] Add validation annotations for required fields
- [*] Add Lombok @Builder annotation with static builder method

## 3. Repository Implementation

### 3.1 CustomerRepository
- [*] Create CustomerRepository interface extending R2dbcRepository
- [*] Add method to find by email

### 3.2 BeerOrderRepository
- [*] Create BeerOrderRepository interface extending R2dbcRepository
- [*] Add method to find all by customer

### 3.3 BeerOrderLineRepository
- [*] Create BeerOrderLineRepository interface extending R2dbcRepository
- [*] Add method to find all by beer order

## 4. Mapper Implementation

### 4.1 CustomerMapper
- [*] Create CustomerMapper interface with MapStruct annotation
- [*] Add methods to map between Customer and CustomerDto
- [*] Configure to ignore id, createdOn, and updatedOn when mapping from DTO to entity

### 4.2 BeerOrderMapper
- [*] Create BeerOrderMapper interface with MapStruct annotation
- [*] Add methods to map between BeerOrder and BeerOrderDto
- [*] Configure to handle the relationship between BeerOrder and Customer
- [*] Configure to map customerId to Customer entity and vice versa
- [*] Configure to ignore id, createdOn, and updatedOn when mapping from DTO to entity

### 4.3 BeerOrderLineMapper
- [*] Create BeerOrderLineMapper interface with MapStruct annotation
- [*] Add methods to map between BeerOrderLine and BeerOrderLineDto
- [*] Configure to handle the relationships between BeerOrderLine, BeerOrder, and Beer
- [*] Configure to map orderId to BeerOrder entity and beerId to Beer entity
- [*] Configure to ignore id, createdOn, and updatedOn when mapping from DTO to entity

## 5. Service Implementation

### 5.1 CustomerService
- [*] Create CustomerService interface with CRUD operation methods
- [*] Create CustomerServiceImpl class implementing CustomerService
- [*] Implement CRUD operations using reactive programming model (Flux/Mono)
- [*] Add proper error handling and validation

### 5.2 BeerOrderService
- [*] Create BeerOrderService interface with CRUD operation methods
- [*] Create BeerOrderServiceImpl class implementing BeerOrderService
- [*] Implement CRUD operations using reactive programming model (Flux/Mono)
- [*] Implement method to create an order with multiple order lines
- [*] Add proper error handling and validation

### 5.3 BeerOrderLineService
- [*] Create BeerOrderLineService interface with CRUD operation methods
- [*] Create BeerOrderLineServiceImpl class implementing BeerOrderLineService
- [*] Implement CRUD operations using reactive programming model (Flux/Mono)
- [*] Add proper error handling and validation

## 6. Controller Implementation

### 6.1 CustomerController
- [*] Create CustomerController class with REST endpoints for CRUD operations
- [*] Implement proper HTTP status codes for different scenarios
- [*] Add validation for request bodies
- [*] Add error handling for validation failures

### 6.2 BeerOrderController
- [*] Create BeerOrderController class with REST endpoints for CRUD operations
- [*] Implement endpoint to create an order with multiple order lines
- [*] Implement proper HTTP status codes for different scenarios
- [*] Add validation for request bodies
- [*] Add error handling for validation failures

## 7. Testing

### 7.1 Unit Tests
- [*] Create unit tests for CustomerService using WebFluxTest
- [*] Create unit tests for BeerOrderService using WebFluxTest
- [*] Create unit tests for BeerOrderLineService using WebFluxTest
- [*] Mock repositories and mappers in all service tests

### 7.2 Integration Tests
- [*] Create integration tests for CustomerRepository using TestContainers
- [*] Create integration tests for BeerOrderRepository using TestContainers
- [*] Create integration tests for BeerOrderLineRepository using TestContainers
- [*] Create integration tests for CustomerController using WebTestClient
- [*] Create integration tests for BeerOrderController using WebTestClient

## 8. Documentation

- [*] Add Javadoc comments to all entity classes
- [*] Add Javadoc comments to all DTO classes
- [*] Add Javadoc comments to all repository interfaces
- [*] Add Javadoc comments to all mapper interfaces
- [*] Add Javadoc comments to all service interfaces and implementations
- [*] Add Javadoc comments to all controller classes
- [*] Update README.md with information about the new functionality