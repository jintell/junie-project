### Implementing Entity Relationships with Java Records, Lombok, and Reactive Programming

Based on the project context and the mention of a Beer ERD, I'll provide detailed instructions for implementing entity relationships using Java records with Lombok in a reactive-compliant project. The implementation will focus on creating a beer ordering system.

#### Entity Structure

From analyzing the existing code and the context of the task, we need to implement the following entities:

1. Beer (already exists)
2. Customer (to be created)
3. BeerOrder (to be created)
4. BeerOrderLine (to be created)

#### Implementation Instructions

### 1. Customer Entity

```java
package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

/**
 * Customer entity as a JPA record.
 */
@Entity
@Table(name = "customer")
public record Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    String name,
    String email,
    String phone,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn,

    @OneToMany(mappedBy = "customer")
    Set<BeerOrder> orders
) {
    /**
     * Static builder method to create Customer instances.
     */
    @Builder
    public static Customer createCustomer(
            Integer id,
            Integer version,
            String name,
            String email,
            String phone,
            Instant createdOn,
            Instant updatedOn,
            Set<BeerOrder> orders) {

        return new Customer(
            id,
            version,
            name,
            email,
            phone,
            createdOn,
            updatedOn,
            orders
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     */
    public Customer() {
        this(null, null, null, null, null, null, null, null);
    }
}
```

### 2. BeerOrder Entity

```java
package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

/**
 * BeerOrder entity as a JPA record.
 */
@Entity
@Table(name = "beer_order")
public record BeerOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    String orderStatus,
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer,

    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    Set<BeerOrderLine> orderLines,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrder instances.
     */
    @Builder
    public static BeerOrder createBeerOrder(
            Integer id,
            Integer version,
            String orderStatus,
            Customer customer,
            Set<BeerOrderLine> orderLines,
            Instant createdOn,
            Instant updatedOn) {

        return new BeerOrder(
            id,
            version,
            orderStatus,
            customer,
            orderLines,
            createdOn,
            updatedOn
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     */
    public BeerOrder() {
        this(null, null, null, null, null, null, null);
    }
}
```

### 3. BeerOrderLine Entity

```java
package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * BeerOrderLine entity as a JPA record.
 */
@Entity
@Table(name = "beer_order_line")
public record BeerOrderLine(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    @ManyToOne
    @JoinColumn(name = "beer_order_id")
    BeerOrder beerOrder,

    @ManyToOne
    @JoinColumn(name = "beer_id")
    Beer beer,

    Integer orderQuantity,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrderLine instances.
     */
    @Builder
    public static BeerOrderLine createBeerOrderLine(
            Integer id,
            Integer version,
            BeerOrder beerOrder,
            Beer beer,
            Integer orderQuantity,
            Instant createdOn,
            Instant updatedOn) {

        return new BeerOrderLine(
            id,
            version,
            beerOrder,
            beer,
            orderQuantity,
            createdOn,
            updatedOn
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     */
    public BeerOrderLine() {
        this(null, null, null, null, null, null, null);
    }
}
```

### 4. Update Beer Entity to Include Relationship

```java
package com.jade.platform.junieproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

/**
 * Beer entity as a JPA record.
 */
@Entity
@Table(name = "beer")
public record Beer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id,

    @Version
    Integer version,

    String beerName,
    String beerStyle,
    String upc,
    Integer quantityOnHand,
    BigDecimal unitPrice,

    @OneToMany(mappedBy = "beer")
    Set<BeerOrderLine> orderLines,

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdOn,

    @UpdateTimestamp
    Instant updatedOn
) {
    /**
     * Static builder method to create Beer instances.
     */
    @Builder
    public static Beer createBeer(
            Integer id, 
            Integer version, 
            String beerName, 
            String beerStyle, 
            String upc, 
            Integer quantityOnHand, 
            BigDecimal unitPrice,
            Set<BeerOrderLine> orderLines,
            Instant createdOn, 
            Instant updatedOn) {

        return new Beer(
            id, 
            version, 
            beerName, 
            beerStyle, 
            upc, 
            quantityOnHand, 
            unitPrice,
            orderLines,
            createdOn, 
            updatedOn
        );
    }

    /**
     * JPA requires a no-args constructor, but records don't allow this directly.
     */
    public Beer() {
        this(null, null, null, null, null, null, null, null, null, null);
    }
}
```

### 5. Create DTOs for New Entities

For each entity, create corresponding DTOs in the `com.jade.platform.junieproject.dtos` package:

#### CustomerDto

```java
package com.jade.platform.junieproject.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

/**
 * Data Transfer Object for Customer entity.
 */
@Builder
public record CustomerDto(
    Integer id,
    
    Integer version,
    
    @NotBlank(message = "Customer name is required")
    String name,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    String phone,
    
    Instant createdOn,
    
    Instant updatedOn
) {
    /**
     * Static builder method to create CustomerDto instances.
     */
    @Builder
    public static CustomerDto createCustomerDto(
            Integer id, 
            Integer version, 
            String name, 
            String email, 
            String phone, 
            Instant createdOn, 
            Instant updatedOn) {

        return new CustomerDto(
            id, 
            version, 
            name, 
            email, 
            phone, 
            createdOn, 
            updatedOn
        );
    }
}
```

#### BeerOrderDto

```java
package com.jade.platform.junieproject.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object for BeerOrder entity.
 */
@Builder
public record BeerOrderDto(
    Integer id,
    
    Integer version,
    
    @NotBlank(message = "Order status is required")
    String orderStatus,
    
    @NotNull(message = "Customer ID is required")
    Integer customerId,
    
    List<BeerOrderLineDto> orderLines,
    
    Instant createdOn,
    
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrderDto instances.
     */
    @Builder
    public static BeerOrderDto createBeerOrderDto(
            Integer id, 
            Integer version, 
            String orderStatus, 
            Integer customerId, 
            List<BeerOrderLineDto> orderLines, 
            Instant createdOn, 
            Instant updatedOn) {

        return new BeerOrderDto(
            id, 
            version, 
            orderStatus, 
            customerId, 
            orderLines, 
            createdOn, 
            updatedOn
        );
    }
}
```

#### BeerOrderLineDto

```java
package com.jade.platform.junieproject.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.Instant;

/**
 * Data Transfer Object for BeerOrderLine entity.
 */
@Builder
public record BeerOrderLineDto(
    Integer id,
    
    Integer version,
    
    Integer orderId,
    
    @NotNull(message = "Beer ID is required")
    Integer beerId,
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be positive")
    Integer orderQuantity,
    
    Instant createdOn,
    
    Instant updatedOn
) {
    /**
     * Static builder method to create BeerOrderLineDto instances.
     */
    @Builder
    public static BeerOrderLineDto createBeerOrderLineDto(
            Integer id, 
            Integer version, 
            Integer orderId, 
            Integer beerId, 
            Integer orderQuantity, 
            Instant createdOn, 
            Instant updatedOn) {

        return new BeerOrderLineDto(
            id, 
            version, 
            orderId, 
            beerId, 
            orderQuantity, 
            createdOn, 
            updatedOn
        );
    }
}
```

### 6. Create Reactive Repositories

For each entity, create a reactive repository interface:

#### CustomerRepository

```java
package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Customer entity.
 */
@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    Mono<Customer> findByEmail(String email);
}
```

#### BeerOrderRepository

```java
package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.BeerOrder;
import com.jade.platform.junieproject.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for BeerOrder entity.
 */
@Repository
public interface BeerOrderRepository extends ReactiveCrudRepository<BeerOrder, Integer> {
    Flux<BeerOrder> findAllByCustomer(Customer customer);
}
```

#### BeerOrderLineRepository

```java
package com.jade.platform.junieproject.repository;

import com.jade.platform.junieproject.model.BeerOrder;
import com.jade.platform.junieproject.model.BeerOrderLine;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for BeerOrderLine entity.
 */
@Repository
public interface BeerOrderLineRepository extends ReactiveCrudRepository<BeerOrderLine, Integer> {
    Flux<BeerOrderLine> findAllByBeerOrder(BeerOrder beerOrder);
}
```

### 7. Create Mappers for New Entities

Create MapStruct mappers for each entity:

#### CustomerMapper

```java
package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.CustomerDto;
import com.jade.platform.junieproject.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between Customer entity and CustomerDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    /**
     * Convert from Customer entity to CustomerDto.
     */
    CustomerDto customerToCustomerDto(Customer customer);

    /**
     * Convert from CustomerDto to Customer entity.
     * Ignores id, createdOn, and updatedOn when mapping from DTO to entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
```

#### BeerOrderMapper

```java
package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.BeerOrderDto;
import com.jade.platform.junieproject.model.BeerOrder;
import com.jade.platform.junieproject.model.Customer;
import com.jade.platform.junieproject.repository.CustomerRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * MapStruct mapper for converting between BeerOrder entity and BeerOrderDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
        uses = {BeerOrderLineMapper.class})
public abstract class BeerOrderMapper {
    
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Convert from BeerOrder entity to BeerOrderDto.
     */
    @Mapping(target = "customerId", source = "customer.id")
    public abstract BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);

    /**
     * Convert from BeerOrderDto to BeerOrder entity.
     * Ignores id, createdOn, and updatedOn when mapping from DTO to entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "customer", ignore = true)
    public abstract BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
    
    /**
     * After mapping, set the customer based on the customerId.
     */
    @AfterMapping
    public Mono<Void> setCustomer(BeerOrderDto dto, @MappingTarget BeerOrder beerOrder) {
        return customerRepository.findById(dto.customerId())
                .doOnNext(customer -> {
                    // Use reflection to set the customer field since records are immutable
                    try {
                        var field = BeerOrder.class.getDeclaredField("customer");
                        field.setAccessible(true);
                        field.set(beerOrder, customer);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to set customer", e);
                    }
                })
                .then();
    }
}
```

#### BeerOrderLineMapper

```java
package com.jade.platform.junieproject.mappers;

import com.jade.platform.junieproject.dtos.BeerOrderLineDto;
import com.jade.platform.junieproject.model.BeerOrderLine;
import com.jade.platform.junieproject.repository.BeerOrderRepository;
import com.jade.platform.junieproject.repository.BeerRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * MapStruct mapper for converting between BeerOrderLine entity and BeerOrderLineDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BeerOrderLineMapper {
    
    @Autowired
    private BeerRepository beerRepository;
    
    @Autowired
    private BeerOrderRepository beerOrderRepository;

    /**
     * Convert from BeerOrderLine entity to BeerOrderLineDto.
     */
    @Mapping(target = "beerId", source = "beer.id")
    @Mapping(target = "orderId", source = "beerOrder.id")
    public abstract BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);

    /**
     * Convert from BeerOrderLineDto to BeerOrderLine entity.
     * Ignores id, createdOn, and updatedOn when mapping from DTO to entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "beer", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    public abstract BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
    
    /**
     * After mapping, set the beer and beerOrder based on their IDs.
     */
    @AfterMapping
    public Mono<Void> setBeerAndOrder(BeerOrderLineDto dto, @MappingTarget BeerOrderLine orderLine) {
        return Mono.zip(
                beerRepository.findById(dto.beerId()),
                beerOrderRepository.findById(dto.orderId())
            )
            .doOnNext(tuple -> {
                var beer = tuple.getT1();
                var order = tuple.getT2();
                
                // Use reflection to set the fields since records are immutable
                try {
                    var beerField = BeerOrderLine.class.getDeclaredField("beer");
                    beerField.setAccessible(true);
                    beerField.set(orderLine, beer);
                    
                    var orderField = BeerOrderLine.class.getDeclaredField("beerOrder");
                    orderField.setAccessible(true);
                    orderField.set(orderLine, order);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to set beer or order", e);
                }
            })
            .then();
    }
}
```

### 8. Important Notes for Reactive Implementation

1. **Immutability with Records**: Java records are immutable, which aligns well with reactive programming principles. However, this can make it challenging to work with JPA relationships. The mappers use reflection to set fields after mapping, which is a workaround for this limitation.

2. **Reactive Relationships**: When working with relationships in a reactive context, always use `Mono` and `Flux` to handle asynchronous operations. Avoid blocking operations.

3. **Lazy Loading**: In a reactive context, lazy loading doesn't work the same way as in traditional JPA. Always fetch related entities explicitly when needed.

4. **Transaction Management**: Use `@Transactional` with reactive repositories, but be aware that the semantics are different from traditional transactions.

5. **Testing**: Use `StepVerifier` from the `reactor-test` library to test reactive code.

### 9. Schema Updates

Don't forget to create the necessary database schema updates:

```sql
-- Customer table
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Beer Order table
CREATE TABLE beer_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    order_status VARCHAR(50) NOT NULL,
    customer_id INT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Beer Order Line table
CREATE TABLE beer_order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    beer_order_id INT NOT NULL,
    beer_id INT NOT NULL,
    order_quantity INT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
    FOREIGN KEY (beer_id) REFERENCES beer(id)
);
```

By following these instructions, you'll have a complete implementation of the beer ordering system with proper entity relationships using Java records, Lombok, and reactive programming principles.