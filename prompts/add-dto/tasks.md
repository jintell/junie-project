# Task List for Adding DTOs to the Beer API

## 1. Create DTO Package and BeerDto Class
- [ ] Create a new package `com.jade.platform.junieproject.dtos`
- [ ] Create a new record `BeerDto` with the following fields:
  - [ ] Integer id
  - [ ] Integer version
  - [ ] String beerName
  - [ ] String beerStyle
  - [ ] String upc
  - [ ] Integer quantityOnHand
  - [ ] BigDecimal unitPrice
  - [ ] Instant createdOn
  - [ ] Instant updatedOn
- [ ] Add validation annotations to the BeerDto fields:
  - [ ] @NotBlank for beerName, beerStyle, upc
  - [ ] @NotNull for quantityOnHand, unitPrice
  - [ ] @Positive for quantityOnHand
  - [ ] @PositiveOrZero for unitPrice
- [ ] Add Lombok annotations:
  - [ ] @Builder to enable the builder pattern

## 2. Create Mapper Package and BeerMapper Interface
- [ ] Create a new package `com.jade.platform.junieproject.mappers`
- [ ] Create a new interface `BeerMapper` using MapStruct:
  - [ ] Add method to convert from Beer to BeerDto
  - [ ] Add method to convert from BeerDto to Beer
  - [ ] Configure the mapper to ignore id, createdOn, and updatedOn when mapping from DTO to entity
- [ ] Add the necessary MapStruct dependencies to the build.gradle file:
  - [ ] Add MapStruct annotation processor
  - [ ] Add MapStruct core library

## 3. Update Service Layer
- [ ] Update the BeerService interface to use DTOs:
  - [ ] Change method signatures to accept and return BeerDto instead of Beer
- [ ] Update the BeerServiceImpl to use the mapper:
  - [ ] Inject the BeerMapper
  - [ ] Use the mapper to convert between DTOs and entities
  - [ ] Update the implementation to handle the conversions

## 4. Update Controller Layer
- [ ] Update the BeerController to use DTOs:
  - [ ] Change method signatures to accept and return BeerDto instead of Beer
  - [ ] Add validation annotations to request methods (@Valid)
  - [ ] Update the implementation to handle the conversions

## 5. Update Tests
- [ ] Update BeerControllerTest to use DTOs:
  - [ ] Create test DTOs instead of test entities
  - [ ] Update assertions to check DTO properties
  - [ ] Update request bodies to use DTOs

## 6. Add Dependencies
- [ ] Add the following dependencies to build.gradle:
  - [ ] Jakarta Validation API: `jakarta.validation:jakarta.validation-api:3.0.2`
  - [ ] Hibernate Validator: `org.hibernate.validator:hibernate-validator:8.0.1.Final`

## 7. Implement Validation Strategy
- [ ] Use Jakarta Bean Validation annotations on the DTO fields
- [ ] Add @Valid annotation to controller methods that accept DTOs
- [ ] Implement proper error handling for validation failures

## 8. Verify and Test
- [ ] Run all tests to ensure they still pass
- [ ] Fix any issues that arise
- [ ] Add tests for validation rules
- [ ] Ensure all CRUD operations work correctly with DTOs