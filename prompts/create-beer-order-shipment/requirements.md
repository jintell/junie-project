# Change Requirements

Add a new entity to the project called `BeerOrderShipment`.

The `BeerOrderShipment` entity has the following properties:
* tracking number - not null
* carrier - not null
* shipment Date - not nul, must be in the future

The `BeerOrderShipment` entity should extend the `BaseEntity` an has a OneToMany relationship with `BeerOrder`.

Add a fFlyway migration scripts for the new `BeerOrderShipment` JPA entity.

Add Java DTOs, mappers, Spring Data Repositories, service and service implementation to support Spring MVC RESTful
CRUD controller. Add tests for all components. Update the OpenAPI documentation for the new controller operations.
Verify all tests are passing.