# BeerOrderShipment Implementation Plan

This document outlines the plan for implementing the `BeerOrderShipment` entity and related components in the JunieMVC application.

## 1. Entity Implementation

### BeerOrderShipment Entity
- Create a new entity class `BeerOrderShipment` in the `entities` package
- Extend `BaseEntity` to inherit common fields (id, version, createdDate, updateDate)
- Add required fields:
  - `trackingNumber` (String, not null)
  - `carrier` (String, not null)
  - `shipmentDate` (LocalDateTime, not null, must be in the future)
- Implement OneToMany relationship with `BeerOrder`
- Add appropriate annotations for JPA, validation, and Lombok

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BeerOrderShipment extends BaseEntity {
    
    @NotNull
    @Column(nullable = false)
    private String trackingNumber;
    
    @NotNull
    @Column(nullable = false)
    private String carrier;
    
    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime shipmentDate;
    
    @OneToMany(mappedBy = "beerOrderShipment", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BeerOrder> beerOrders = new HashSet<>();
    
    // Helper methods to manage bidirectional relationship
    public void addBeerOrder(BeerOrder beerOrder) {
        beerOrders.add(beerOrder);
        beerOrder.setBeerOrderShipment(this);
    }
    
    public void removeBeerOrder(BeerOrder beerOrder) {
        beerOrders.remove(beerOrder);
        beerOrder.setBeerOrderShipment(null);
    }
}
```

### Update BeerOrder Entity
- Add a ManyToOne relationship to BeerOrderShipment
- Add appropriate annotations

```java
@ManyToOne
@JoinColumn(name = "beer_order_shipment_id")
@ToString.Exclude
@EqualsAndHashCode.Exclude
private BeerOrderShipment beerOrderShipment;
```

## 2. Database Migration

Create a new Flyway migration script `V3__add_beer_order_shipment_table.sql` in the `src/main/resources/db/migration` directory:

```sql
-- Create beer_order_shipment table
CREATE TABLE IF NOT EXISTS beer_order_shipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    tracking_number VARCHAR(255) NOT NULL,
    carrier VARCHAR(255) NOT NULL,
    shipment_date TIMESTAMP NOT NULL
);

-- Add beer_order_shipment_id column to beer_order table
ALTER TABLE beer_order ADD COLUMN beer_order_shipment_id INT;

-- Add foreign key constraint
ALTER TABLE beer_order ADD CONSTRAINT fk_beer_order_shipment 
    FOREIGN KEY (beer_order_shipment_id) REFERENCES beer_order_shipment(id);
```

## 3. DTO Implementation

Create a new DTO record for the BeerOrderShipment entity:

```java
/**
 * DTO for BeerOrderShipment entity
 */
public record BeerOrderShipmentDto(
    // readonly
    Integer id,
    Integer version,
    
    @NotBlank(message = "Tracking number is required")
    String trackingNumber,
    
    @NotBlank(message = "Carrier is required")
    String carrier,
    
    @NotNull(message = "Shipment date is required")
    @Future(message = "Shipment date must be in the future")
    LocalDateTime shipmentDate,
    
    Set<BeerOrderDto> beerOrders,
    
    // readonly createdDate
    LocalDateTime createdDate,
    // readonly updatedDate
    LocalDateTime updateDate
) {}
```

## 4. Mapper Implementation

Create a mapper interface using MapStruct:

```java
@Mapper
public interface BeerOrderShipmentMapper {
    
    BeerOrderShipmentDto beerOrderShipmentToBeerOrderShipmentDto(BeerOrderShipment beerOrderShipment);
    
    BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);
    
    void updateBeerOrderShipmentFromDto(BeerOrderShipmentDto beerOrderShipmentDto, @MappingTarget BeerOrderShipment beerOrderShipment);
}
```

## 5. Repository Implementation

Create a repository interface for BeerOrderShipment:

```java
@Repository
public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, Integer> {
    // Add custom query methods if needed
}
```

## 6. Service Implementation

### Service Interface

```java
public interface BeerOrderShipmentService {
    
    /**
     * Get all beer order shipments
     * @return list of all beer order shipments
     */
    List<BeerOrderShipmentDto> getAllBeerOrderShipments();
    
    /**
     * Get a beer order shipment by ID
     * @param id the beer order shipment ID
     * @return the beer order shipment if found
     */
    Optional<BeerOrderShipmentDto> getBeerOrderShipmentById(Integer id);
    
    /**
     * Save a beer order shipment
     * @param beerOrderShipmentDto the beer order shipment to save
     * @return the saved beer order shipment
     */
    BeerOrderShipmentDto saveBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);
    
    /**
     * Delete a beer order shipment
     * @param id the beer order shipment ID to delete
     */
    void deleteBeerOrderShipmentById(Integer id);
}
```

### Service Implementation

```java
@Service
@RequiredArgsConstructor
@Transactional
public class BeerOrderShipmentServiceImpl implements BeerOrderShipmentService {
    
    private final BeerOrderShipmentRepository beerOrderShipmentRepository;
    private final BeerOrderShipmentMapper beerOrderShipmentMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getAllBeerOrderShipments() {
        return beerOrderShipmentRepository.findAll().stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderShipmentDto> getBeerOrderShipmentById(Integer id) {
        return beerOrderShipmentRepository.findById(id)
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto);
    }
    
    @Override
    public BeerOrderShipmentDto saveBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto) {
        BeerOrderShipment beerOrderShipment = beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(beerOrderShipmentDto);
        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);
        return beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(savedBeerOrderShipment);
    }
    
    @Override
    public void deleteBeerOrderShipmentById(Integer id) {
        beerOrderShipmentRepository.deleteById(id);
    }
}
```

## 7. Controller Implementation

```java
@RestController
@RequestMapping("/api/v1/beer-order-shipments")
@RequiredArgsConstructor
public class BeerOrderShipmentController {
    
    private final BeerOrderShipmentService beerOrderShipmentService;
    
    /**
     * Get all beer order shipments
     * @return list of all beer order shipments
     */
    @GetMapping
    public List<BeerOrderShipmentDto> getAllBeerOrderShipments() {
        return beerOrderShipmentService.getAllBeerOrderShipments();
    }
    
    /**
     * Get a beer order shipment by ID
     * @param id the beer order shipment ID
     * @return the beer order shipment if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BeerOrderShipmentDto> getBeerOrderShipmentById(@PathVariable Integer id) {
        Optional<BeerOrderShipmentDto> beerOrderShipmentOptional = beerOrderShipmentService.getBeerOrderShipmentById(id);
        
        return beerOrderShipmentOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create a new beer order shipment
     * @param beerOrderShipmentDto the beer order shipment to create
     * @return the created beer order shipment
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerOrderShipmentDto createBeerOrderShipment(@Valid @RequestBody BeerOrderShipmentDto beerOrderShipmentDto) {
        // Create a new DTO with null ID to ensure a new entity is created
        BeerOrderShipmentDto newBeerOrderShipmentDto = new BeerOrderShipmentDto(
                null,
                beerOrderShipmentDto.version(),
                beerOrderShipmentDto.trackingNumber(),
                beerOrderShipmentDto.carrier(),
                beerOrderShipmentDto.shipmentDate(),
                beerOrderShipmentDto.beerOrders(),
                beerOrderShipmentDto.createdDate(),
                beerOrderShipmentDto.updateDate()
        );
        
        return beerOrderShipmentService.saveBeerOrderShipment(newBeerOrderShipmentDto);
    }
    
    /**
     * Update an existing beer order shipment
     * @param id the beer order shipment ID to update
     * @param beerOrderShipmentDto the updated beer order shipment data
     * @return the updated beer order shipment
     */
    @PutMapping("/{id}")
    public ResponseEntity<BeerOrderShipmentDto> updateBeerOrderShipment(@PathVariable Integer id, @Valid @RequestBody BeerOrderShipmentDto beerOrderShipmentDto) {
        Optional<BeerOrderShipmentDto> beerOrderShipmentOptional = beerOrderShipmentService.getBeerOrderShipmentById(id);
        
        if (beerOrderShipmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Create a new DTO with the provided ID to ensure the correct entity is updated
        BeerOrderShipmentDto updatedDto = new BeerOrderShipmentDto(
                id,
                beerOrderShipmentDto.version(),
                beerOrderShipmentDto.trackingNumber(),
                beerOrderShipmentDto.carrier(),
                beerOrderShipmentDto.shipmentDate(),
                beerOrderShipmentDto.beerOrders(),
                beerOrderShipmentDto.createdDate(),
                beerOrderShipmentDto.updateDate()
        );
        
        BeerOrderShipmentDto savedBeerOrderShipment = beerOrderShipmentService.saveBeerOrderShipment(updatedDto);
        
        return ResponseEntity.ok(savedBeerOrderShipment);
    }
    
    /**
     * Delete a beer order shipment
     * @param id the beer order shipment ID to delete
     * @return no content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeerOrderShipment(@PathVariable Integer id) {
        Optional<BeerOrderShipmentDto> beerOrderShipmentOptional = beerOrderShipmentService.getBeerOrderShipmentById(id);
        
        if (beerOrderShipmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        beerOrderShipmentService.deleteBeerOrderShipmentById(id);
        
        return ResponseEntity.noContent().build();
    }
}
```

## 8. Test Implementation

### Repository Tests

```java
@DataJpaTest
class BeerOrderShipmentRepositoryTest {
    
    @Autowired
    BeerOrderShipmentRepository beerOrderShipmentRepository;
    
    @Test
    void testSaveBeerOrderShipment() {
        // Given
        BeerOrderShipment beerOrderShipment = BeerOrderShipment.builder()
                .trackingNumber("TRACK123456")
                .carrier("FedEx")
                .shipmentDate(LocalDateTime.now().plusDays(1))
                .build();
        
        // When
        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);
        
        // Then
        assertThat(savedBeerOrderShipment).isNotNull();
        assertThat(savedBeerOrderShipment.getId()).isNotNull();
    }
    
    // Add more repository tests
}
```

### Service Tests

```java
@ExtendWith(MockitoExtension.class)
class BeerOrderShipmentServiceImplTest {
    
    @Mock
    BeerOrderShipmentRepository beerOrderShipmentRepository;
    
    @Mock
    BeerOrderShipmentMapper beerOrderShipmentMapper;
    
    @InjectMocks
    BeerOrderShipmentServiceImpl beerOrderShipmentService;
    
    @Test
    void testGetAllBeerOrderShipments() {
        // Given
        BeerOrderShipment beerOrderShipment = new BeerOrderShipment();
        BeerOrderShipmentDto beerOrderShipmentDto = new BeerOrderShipmentDto(
                1, 1, "TRACK123456", "FedEx", 
                LocalDateTime.now().plusDays(1), null, 
                LocalDateTime.now(), LocalDateTime.now()
        );
        
        when(beerOrderShipmentRepository.findAll()).thenReturn(List.of(beerOrderShipment));
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(any(BeerOrderShipment.class)))
                .thenReturn(beerOrderShipmentDto);
        
        // When
        List<BeerOrderShipmentDto> result = beerOrderShipmentService.getAllBeerOrderShipments();
        
        // Then
        assertThat(result).hasSize(1);
        verify(beerOrderShipmentRepository).findAll();
    }
    
    // Add more service tests
}
```

### Controller Tests

```java
@WebMvcTest(BeerOrderShipmentController.class)
class BeerOrderShipmentControllerTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @MockBean
    BeerOrderShipmentService beerOrderShipmentService;
    
    @Test
    void testGetAllBeerOrderShipments() throws Exception {
        // Given
        BeerOrderShipmentDto beerOrderShipmentDto = new BeerOrderShipmentDto(
                1, 1, "TRACK123456", "FedEx", 
                LocalDateTime.now().plusDays(1), null, 
                LocalDateTime.now(), LocalDateTime.now()
        );
        
        when(beerOrderShipmentService.getAllBeerOrderShipments()).thenReturn(List.of(beerOrderShipmentDto));
        
        // When/Then
        mockMvc.perform(get("/api/v1/beer-order-shipments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].trackingNumber").value("TRACK123456"));
    }
    
    // Add more controller tests
}
```

### Integration Tests

```java
@SpringBootTest
class BeerOrderShipmentIntegrationTest {
    
    @Autowired
    BeerOrderShipmentRepository beerOrderShipmentRepository;
    
    @Autowired
    BeerOrderShipmentService beerOrderShipmentService;
    
    @Test
    void testSaveAndRetrieveBeerOrderShipment() {
        // Given
        BeerOrderShipmentDto beerOrderShipmentDto = new BeerOrderShipmentDto(
                null, null, "TRACK123456", "FedEx", 
                LocalDateTime.now().plusDays(1), null, 
                null, null
        );
        
        // When
        BeerOrderShipmentDto savedDto = beerOrderShipmentService.saveBeerOrderShipment(beerOrderShipmentDto);
        Optional<BeerOrderShipmentDto> retrievedDtoOptional = beerOrderShipmentService.getBeerOrderShipmentById(savedDto.id());
        
        // Then
        assertThat(retrievedDtoOptional).isPresent();
        BeerOrderShipmentDto retrievedDto = retrievedDtoOptional.get();
        assertThat(retrievedDto.trackingNumber()).isEqualTo("TRACK123456");
        assertThat(retrievedDto.carrier()).isEqualTo("FedEx");
    }
    
    // Add more integration tests
}
```

## 9. OpenAPI Documentation Update

Update the OpenAPI specification to include the new BeerOrderShipment endpoints:

1. Create a new schema file for BeerOrderShipment in `openapi/openapi/components/schemas/BeerOrderShipment.yaml`
2. Add new path operations in `openapi/openapi/paths/beer-order-shipments.yaml`
3. Update the main `openapi.yaml` file to reference the new components

### BeerOrderShipment Schema

```yaml
type: object
properties:
  id:
    type: integer
    format: int32
    readOnly: true
  version:
    type: integer
    format: int32
  trackingNumber:
    type: string
    description: Tracking number for the shipment
  carrier:
    type: string
    description: Carrier for the shipment
  shipmentDate:
    type: string
    format: date-time
    description: Date when the shipment will be sent
  beerOrders:
    type: array
    items:
      $ref: './BeerOrder.yaml'
  createdDate:
    type: string
    format: date-time
    readOnly: true
  updateDate:
    type: string
    format: date-time
    readOnly: true
required:
  - trackingNumber
  - carrier
  - shipmentDate
```

### Beer Order Shipments Paths

```yaml
get:
  summary: Get all beer order shipments
  operationId: getAllBeerOrderShipments
  tags:
    - beer-order-shipments
  responses:
    '200':
      description: List of beer order shipments
      content:
        application/json:
          schema:
            type: array
            items:
              $ref: '../components/schemas/BeerOrderShipment.yaml'
post:
  summary: Create a new beer order shipment
  operationId: createBeerOrderShipment
  tags:
    - beer-order-shipments
  requestBody:
    content:
      application/json:
        schema:
          $ref: '../components/schemas/BeerOrderShipment.yaml'
  responses:
    '201':
      description: Beer order shipment created
      content:
        application/json:
          schema:
            $ref: '../components/schemas/BeerOrderShipment.yaml'
    '400':
      description: Invalid input
      content:
        application/json:
          schema:
            $ref: '../components/responses/Problem.yaml'
```

## 10. Implementation Steps

1. Create the BeerOrderShipment entity
2. Update the BeerOrder entity with the relationship to BeerOrderShipment
3. Create the Flyway migration script
4. Create the BeerOrderShipmentDto
5. Create the BeerOrderShipmentMapper
6. Create the BeerOrderShipmentRepository
7. Create the BeerOrderShipmentService interface and implementation
8. Create the BeerOrderShipmentController
9. Write tests for all components
10. Update the OpenAPI documentation
11. Verify all tests are passing