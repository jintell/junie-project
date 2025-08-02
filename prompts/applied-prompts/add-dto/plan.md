# Implementation Plan for Adding DTOs to the Beer API

## Overview
This plan outlines the steps needed to refactor the Beer API to use Data Transfer Objects (DTOs) instead of directly exposing JPA entities. This change will improve the separation of concerns between the web and persistence layers, allow for better validation, and make the API more maintainable.

## Current State Analysis
The current implementation directly exposes the Beer JPA entity in the REST API, which tightly couples the API contract to the database schema. Key observations:

1. The Beer class is a JPA entity implemented as a Java record
2. The BeerController directly uses the Beer entity in request/response bodies
3. The BeerService interface and implementation operate directly on Beer entities
4. The BeerRepository provides reactive data access using R2DBC
5. Tests directly use the Beer entity for assertions and request bodies

## Implementation Steps

### 1. Create DTO Package and BeerDto Class
- Create a new package `com.jade.platform.junieproject.dtos`
- Create a new record `BeerDto` with the following fields:
  - Integer id
  - Integer version
  - String beerName
  - String beerStyle
  - String upc
  - Integer quantityOnHand
  - BigDecimal unitPrice
  - Instant createdOn
  - Instant updatedOn
- Add validation annotations to the BeerDto fields:
  - @NotBlank for beerName, beerStyle, upc
  - @NotNull for quantityOnHand, unitPrice
  - @Positive for quantityOnHand
  - @PositiveOrZero for unitPrice
- Add Lombok annotations:
  - @Builder to enable the builder pattern
  - @Data is not needed since we're using a record

### 2. Create Mapper Package and BeerMapper Interface
- Create a new package `com.jade.platform.junieproject.mappers`
- Create a new interface `BeerMapper` using MapStruct:
  - Add method to convert from Beer to BeerDto
  - Add method to convert from BeerDto to Beer
  - Configure the mapper to ignore id, createdOn, and updatedOn when mapping from DTO to entity
- Add the necessary MapStruct dependencies to the build.gradle file

### 3. Update Service Layer
- Update the BeerService interface to use DTOs:
  - Change method signatures to accept and return BeerDto instead of Beer
- Update the BeerServiceImpl to use the mapper:
  - Inject the BeerMapper
  - Use the mapper to convert between DTOs and entities
  - Update the implementation to handle the conversions

### 4. Update Controller Layer
- Update the BeerController to use DTOs:
  - Change method signatures to accept and return BeerDto instead of Beer
  - Add validation annotations to request methods (@Valid)
  - Update the implementation to handle the conversions

### 5. Update Tests
- Update BeerControllerTest to use DTOs:
  - Create test DTOs instead of test entities
  - Update assertions to check DTO properties
  - Update request bodies to use DTOs

### 6. Verify Tests
- Run all tests to ensure they still pass
- Fix any issues that arise

## Dependencies
- Add the following dependencies to build.gradle:
  - Jakarta Validation API: `jakarta.validation:jakarta.validation-api:3.0.2`
  - Hibernate Validator: `org.hibernate.validator:hibernate-validator:8.0.1.Final`

## Validation Strategy
- Use Jakarta Bean Validation annotations on the DTO fields
- Add @Valid annotation to controller methods that accept DTOs
- Implement proper error handling for validation failures

## Testing Strategy
- Update existing tests to use DTOs instead of entities
- Add tests for validation rules
- Ensure all CRUD operations work correctly with DTOs

## Benefits of This Approach
1. **Separation of Concerns**: Decouples the API contract from the database schema
2. **Validation**: Enables input validation at the API boundary
3. **Security**: Prevents exposing sensitive or internal entity fields
4. **Flexibility**: Allows the API and database schema to evolve independently
5. **Documentation**: Makes the API contract more explicit and self-documenting

## Potential Challenges
1. **Mapping Complexity**: Need to ensure all fields are correctly mapped
2. **Performance**: Additional mapping operations could impact performance
3. **Maintenance**: Need to keep DTOs and entities in sync as requirements change

## Conclusion
This implementation plan provides a structured approach to refactoring the Beer API to use DTOs. By following these steps, we'll improve the separation of concerns, enable better validation, and make the API more maintainable.