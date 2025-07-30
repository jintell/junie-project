# DTO Implementation Requirements for Beer API

## Overview
This document outlines the requirements for implementing Data Transfer Objects (DTOs) in the Beer API project. The goal is to separate the web layer from the persistence layer by introducing DTOs, which will improve API design, validation, and maintainability.

## Requirements

### 1. Create BeerDto Record
- Create a new package `com.jade.platform.junieproject.dtos`
- Implement a `BeerDto` as a Java record with the following fields:
  - Integer id
  - Integer version
  - String beerName
  - String beerStyle
  - String upc
  - Integer quantityOnHand
  - BigDecimal unitPrice
  - Instant createdOn
  - Instant updatedOn

### 2. Add Validation to BeerDto
- Add Jakarta Bean Validation annotations to the BeerDto fields:
  - `beerName`: @NotBlank, with a message indicating that beer name is required
  - `beerStyle`: @NotBlank, with a message indicating that beer style is required
  - `upc`: @NotBlank, with a message indicating that UPC is required
  - `unitPrice`: @NotNull, and @Positive with a message indicating that price is required and cannot be negative
  - `quantityOnHand`: @NotNull, @Positive with a message indicating that quantity is required and cannot be negative

### 3. Implement Builder Pattern for BeerDto
- Add Lombok's `@Builder` annotation to the BeerDto record
- Implement a static builder method similar to the one in the Beer entity

### 4. Create MapStruct Mapper
- Create a new package `com.jade.platform.junieproject.mappers`
- Implement a `BeerMapper` interface using MapStruct:
  ```java
  @Mapper
  public interface BeerMapper {
      BeerDto beerToBeerDto(Beer beer);
      Beer beerDtoToBeer(BeerDto beerDto);
  }
  ```
- When mapping from BeerDto to Beer, ignore the following properties:
  - id (when creating a new beer)
  - createdOn
  - updatedOn

### 5. Update Service Layer
- Modify the `BeerService` interface to accept and return DTOs instead of entities:
  ```java
  public interface BeerService {
      Flux<BeerDto> getAllBeers();
      Mono<BeerDto> getBeerById(Integer id);
      Mono<BeerDto> getBeerByName(String beerName);
      Mono<BeerDto> createBeer(BeerDto beerDto);
      Mono<BeerDto> updateBeer(Integer id, BeerDto beerDto);
      Mono<Void> deleteBeer(Integer id);
  }
  ```
- Update `BeerServiceImpl` to use the mapper for converting between DTOs and entities
- Ensure that the service layer handles the conversion between DTOs and entities appropriately

### 6. Update Controller Layer
- Modify `BeerController` to use DTOs in all request and response handling:
  ```java
  @RestController
  @RequestMapping("/api/v1/beers")
  @RequiredArgsConstructor
  public class BeerController {
      private final BeerService beerService;
      
      @GetMapping
      public Flux<BeerDto> getAllBeers() {
          return beerService.getAllBeers();
      }
      
      // Update other methods similarly
  }
  ```
- Add `@Valid` annotation to request body parameters to enable validation:
  ```java
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BeerDto> createBeer(@Valid @RequestBody BeerDto beerDto) {
      return beerService.createBeer(beerDto);
  }
  ```

### 7. Implement Global Exception Handler
- Create a global exception handler to handle validation errors and return appropriate error responses
- Use `@RestControllerAdvice` to create a centralized exception handling component
- Implement handlers for common exceptions like `MethodArgumentNotValidException` for validation errors

### 8. Update Tests
- Update `BeerControllerTest` to use DTOs instead of entities
- Create test fixtures for DTOs
- Ensure all tests pass with the new DTO implementation

## Implementation Notes
- The project uses reactive programming with Spring WebFlux, so ensure all implementations maintain the reactive nature
- The Beer entity is implemented as a Java record, and the BeerDto should follow the same pattern
- Use constructor injection for all dependencies
- Maintain the existing API contract (endpoints, HTTP methods, status codes) to ensure backward compatibility

## Acceptance Criteria
- All controller methods use DTOs for request and response handling
- All service methods accept and return DTOs
- Validation is properly implemented and tested
- All tests pass
- The API maintains its reactive nature
- The web layer is properly separated from the persistence layer