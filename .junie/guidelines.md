# Junie Project Developer Guidelines

## Project Overview
Junie Project is a reactive Spring Boot application that manages beer inventory. It provides a RESTful API for CRUD operations on beer entities.

## Tech Stack
- **Java 21**
- **Spring Boot 3.5.4**
- **Spring WebFlux** - Reactive web framework
- **Spring Data R2DBC** - Reactive database access
- **Spring Data JPA** - Entity management
- **H2 Database** - In-memory database
- **Flyway** - Database migrations
- **Lombok** - Reduces boilerplate code
- **MapStruct** - Object mapping
- **JUnit 5** - Testing framework
- **Mockito** - Mocking for tests
- **Gradle** - Build tool

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/jade/platform/junieproject/
│   │       ├── controllers/    # REST API endpoints
│   │       ├── model/          # Domain entities
│   │       ├── repository/     # Data access layer
│   │       └── services/       # Business logic
│   └── resources/
│       └── application.yml     # Application configuration
└── test/
    ├── java/                   # Test classes
    └── resources/
        └── application-test.yml # Test configuration
```

## Development Workflow
1. **Setup**: Clone the repository and import as a Gradle project
2. **Build**: Run `./gradlew build` to build the project
3. **Run**: Execute `./gradlew bootRun` to start the application
4. **API Access**: The API is available at `http://localhost:8080/api/v1/beers`

## Testing
1. **Run Tests**: Execute `./gradlew test` to run all tests
2. **Test Structure**:
   - Unit tests for controllers use WebTestClient and mock the service layer
   - Repository tests use an in-memory H2 database
   - Integration tests can be run with the Spring test context

## Best Practices
1. **Reactive Programming**:
   - Use Flux for collections and Mono for single items
   - Avoid blocking operations in the reactive pipeline
   - Use operators like map, flatMap, and filter for transformations

2. **Code Organization**:
   - Follow the layered architecture (controller → service → repository)
   - Keep controllers thin, with business logic in services
   - Use records for immutable data models

3. **Testing**:
   - Write tests for all new functionality
   - Mock dependencies for unit tests
   - Use WebTestClient for testing controllers
   - Test both success and error scenarios