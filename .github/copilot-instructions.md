# Spring Boot + jBehave BDD Project Instructions

## Project Architecture

This is a Spring Boot 3.5.3 application with jBehave 4.8 for BDD testing, using Java 21 and H2 in-memory database.

**Core Structure:**
- `com.bookstore.jbehave` - Main package following standard Spring Boot layered architecture
- Controller → Service → Repository pattern with JPA entities
- H2 database configured for development/testing (`jdbc:h2:mem:testdb`)
- Lombok annotations for boilerplate reduction (`@Data` on entities)

## jBehave BDD Integration Pattern

**Key Components:**
- **Story Runner**: `UserRegistrationStoryRunner extends JUnitStories`
  - Configures story loading from classpath
  - Links step definitions via `InstanceStepsFactory`
  - Story paths defined in `storyPaths()` method

- **Step Definitions**: Located in `src/test/java/.../steps/`
  - Use `@Given`, `@When`, `@Then` annotations with parameter binding
  - Example: `@Given("a user with username \"$username\" and password \"$password\"")`
  - Spring context integration via `@ContextConfiguration(classes = TestConfig.class)`

- **Story Files**: `.story` files in `src/test/java/.../stories/`
  - Follow jBehave syntax with Meta tags, Scenarios, and parameterized steps
  - Automatically copied to `target/test-classes/` during build

## Development Workflows

**Build & Test Commands:**
```bash
./mvnw clean compile                    # Compile main sources
./mvnw test                            # Run all tests (includes jBehave stories)
./mvnw spring-boot:run                 # Start application on port 8080
```

**jBehave-Specific Testing:**
- Stories execute through JUnit via `JUnitStories` base class
- Step definitions must match story syntax exactly (parameter binding with `$variable`)
- Test context configured via `TestConfig` class for Spring component scanning

## Project-Specific Conventions

**Entity Design:**
- Use `@Entity` + `@Data` (Lombok) pattern
- JPA `@GeneratedValue(strategy = GenerationType.IDENTITY)` for IDs
- Simple POJO structure with Spring Data JPA repositories

**API Layer:**
- REST endpoints follow `/resource` pattern (e.g., `/users/register`)
- Services return String messages for success/error states
- Constructor injection preferred over `@Autowired` field injection (current code uses field injection)

**Testing Structure:**
- BDD scenarios in `.story` files with descriptive names
- Step classes use Spring context for integration testing
- Separate test configuration class (`TestConfig`) for test-specific beans

## Key Integration Points

- **Database**: H2 console available in dev mode
- **JPA**: Hibernate with H2 dialect configured
- **Maven**: Standard Spring Boot parent POM with jBehave core dependency
- **Story Execution**: Stories run as part of Maven test lifecycle via JUnit integration

## Adding New Features

1. **New Entity**: Create in `model/` with `@Entity` + `@Data`
2. **New API**: Add controller in `controller/`, service in `service/`, repository interface extending `JpaRepository`
3. **New BDD Scenario**: 
   - Add `.story` file in test stories directory
   - Create corresponding step definition class
   - Update story runner to include new story path if needed

## Critical Files for AI Context

- `UserRegistrationStoryRunner.java` - Shows jBehave configuration pattern
- `user_registration.story` - Example story syntax and structure  
- `UserRegistrationSteps.java` - Step definition annotations and Spring integration
- `pom.xml` - jBehave version and Maven plugin configuration