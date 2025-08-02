### Beer Order System Requirements

#### Overview
This document outlines the requirements for implementing a beer ordering system as part of the Junie project. The system will allow customers to place orders for various beers, with each order containing multiple line items.

#### Entity Structure
The system will consist of the following entities:
1. Beer (existing)
2. Customer
3. BeerOrder
4. BeerOrderLine

#### Technical Requirements

### 1. Entity Implementation

All entities should be implemented as Java records with the following characteristics:
- Use JPA annotations for persistence
- Implement Lombok's Builder pattern via static builder methods
- Include proper validation constraints
- Follow reactive programming principles
- Maintain immutability through the record structure

#### Beer Entity (Already Implemented)
The Beer entity is already implemented with the following fields:
- id (Integer)
- version (Integer)
- beerName (String)
- beerStyle (String)
- upc (String)
- quantityOnHand (Integer)
- unitPrice (BigDecimal)
- createdOn (Instant)
- updatedOn (Instant)

#### Customer Entity
Implement a Customer entity with the following fields:
- id (Integer)
- version (Integer)
- name (String, required)
- email (String, required, unique)
- phone (String, optional)
- createdOn (Instant)
- updatedOn (Instant)
- orders (relationship to BeerOrder)

#### BeerOrder Entity
Implement a BeerOrder entity with the following fields:
- id (Integer)
- version (Integer)
- orderStatus (String, required)
- customer (relationship to Customer)
- orderLines (relationship to BeerOrderLine)
- createdOn (Instant)
- updatedOn (Instant)

#### BeerOrderLine Entity
Implement a BeerOrderLine entity with the following fields:
- id (Integer)
- version (Integer)
- beerOrder (relationship to BeerOrder)
- beer (relationship to Beer)
- orderQuantity (Integer, required, positive)
- createdOn (Instant)
- updatedOn (Instant)

### 2. DTO Implementation

Create corresponding DTOs for each entity in the `com.jade.platform.junieproject.dtos` package:

#### CustomerDto
- id (Integer)
- version (Integer)
- name (String, required)
- email (String, required, valid email format)
- phone (String)
- createdOn (Instant)
- updatedOn (Instant)

#### BeerOrderDto
- id (Integer)
- version (Integer)
- orderStatus (String, required)
- customerId (Integer, required)
- orderLines (List of BeerOrderLineDto)
- createdOn (Instant)
- updatedOn (Instant)

#### BeerOrderLineDto
- id (Integer)
- version (Integer)
- orderId (Integer)
- beerId (Integer, required)
- orderQuantity (Integer, required, positive)
- createdOn (Instant)
- updatedOn (Instant)

### 3. Repository Implementation

Create reactive repositories for each entity using Spring Data R2DBC:

#### CustomerRepository
```java
public interface CustomerRepository extends R2dbcRepository<Customer, Integer> {
    Mono<Customer> findByEmail(String email);
}
```

#### BeerOrderRepository
```java
public interface BeerOrderRepository extends R2dbcRepository<BeerOrder, Integer> {
    Flux<BeerOrder> findAllByCustomerId(Integer customerId);
}
```

#### BeerOrderLineRepository
```java
public interface BeerOrderLineRepository extends R2dbcRepository<BeerOrderLine, Integer> {
    Flux<BeerOrderLine> findAllByBeerOrderId(Integer beerOrderId);
}
```

### 4. Mapper Implementation

Create MapStruct mappers for each entity to convert between entities and DTOs:

#### CustomerMapper
- Convert Customer to CustomerDto
- Convert CustomerDto to Customer (ignoring id, createdOn, updatedOn, and orders)

#### BeerOrderMapper
- Convert BeerOrder to BeerOrderDto (mapping customer.id to customerId)
- Convert BeerOrderDto to BeerOrder (resolving customer from customerId)

#### BeerOrderLineMapper
- Convert BeerOrderLine to BeerOrderLineDto (mapping beer.id to beerId and beerOrder.id to orderId)
- Convert BeerOrderLineDto to BeerOrderLine (resolving beer and beerOrder from their IDs)

### 5. Service Implementation

Create service interfaces and implementations for each entity:

#### CustomerService
- getAllCustomers()
- getCustomerById(Integer id)
- getCustomerByEmail(String email)
- createCustomer(CustomerDto customerDto)
- updateCustomer(Integer id, CustomerDto customerDto)
- deleteCustomer(Integer id)

#### BeerOrderService
- getAllOrders()
- getOrdersByCustomerId(Integer customerId)
- getOrderById(Integer id)
- createOrder(BeerOrderDto beerOrderDto)
- updateOrder(Integer id, BeerOrderDto beerOrderDto)
- deleteOrder(Integer id)

### 6. Controller Implementation

Create REST controllers for each entity:

#### CustomerController
- GET /api/v1/customers
- GET /api/v1/customers/{id}
- GET /api/v1/customers/email/{email}
- POST /api/v1/customers
- PUT /api/v1/customers/{id}
- DELETE /api/v1/customers/{id}

#### BeerOrderController
- GET /api/v1/orders
- GET /api/v1/customers/{customerId}/orders
- GET /api/v1/orders/{id}
- POST /api/v1/orders
- PUT /api/v1/orders/{id}
- DELETE /api/v1/orders/{id}

### 7. Validation Requirements

- All required fields should be validated using Jakarta Validation annotations
- Email fields should be validated for proper format
- Quantity fields should be validated to ensure positive values
- Appropriate error messages should be provided for validation failures

### 8. Reactive Programming Guidelines

- Use Mono and Flux for asynchronous operations
- Avoid blocking operations
- Handle relationships properly in a reactive context
- Use proper error handling with reactive types
- Implement proper transaction management with @Transactional

### 9. Testing Requirements

- Write unit tests for all service implementations
- Write integration tests for repositories using TestContainers
- Write controller tests using WebTestClient
- Use StepVerifier for testing reactive code
- Ensure test coverage for both success and error scenarios

### 10. Documentation Requirements

- Add comprehensive JavaDoc comments to all classes, interfaces, and methods
- Document API endpoints using OpenAPI annotations
- Include example requests and responses in the API documentation
- Document validation constraints and error responses

By implementing these requirements, the beer ordering system will provide a complete, reactive solution for managing beer orders with proper entity relationships, validation, and API endpoints.