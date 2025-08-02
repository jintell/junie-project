Update the BeerController to use DTOs. In the new package `dtos`, create a new Pojo called BeerDto as a record for the 
`Beer` class. The DTOs should use annotations to define the validation rules. The DTOs should also use annotations from 
the Project Lombok library, including `@Builder` and `@Data`. Create a MapStruct mapper to convert between the DTO and 
the Beer class. Mappers should be created in the `mappers` package. When converting from the DTo to the entity, 
ignore the properties id, createdOn and updatedOn. Convert the service layer to accept DTO objects and to use the 
mapstruct mapper for type conversions. Update the controller methods to use the DTOs. Update the controller tests to 
use the DTOs. Verify the tests still pass.