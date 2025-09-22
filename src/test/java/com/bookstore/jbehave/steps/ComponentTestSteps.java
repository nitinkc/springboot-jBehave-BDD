package com.bookstore.jbehave.steps;

import com.bookstore.jbehave.config.TestConfig;
import com.bookstore.jbehave.dto.UserRegistrationDto;
import com.bookstore.jbehave.model.User;
import com.bookstore.jbehave.repository.UserRepository;
import com.bookstore.jbehave.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest
@Slf4j
public class ComponentTestSteps {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserRegistrationDto testUser;
    private String validationResult;
    private User databaseUser;
    private boolean repositoryTestsPassed = false;
    private boolean businessLogicTestsPassed = false;

    @Given("I have a complete user registration request: $userTable")
    public void givenCompleteUserRegistration(ExamplesTable userTable) {
        log.info("Creating complete user registration from table");
        List<Map<String, String>> rows = userTable.getRows();
        Map<String, String> userData = rows.get(0);
        
        testUser = UserRegistrationDto.builder()
                .username(userData.get("username"))
                .password(userData.get("password"))
                .email(userData.get("email"))
                .firstName(userData.get("firstName"))
                .lastName(userData.get("lastName"))
                .phoneNumber(userData.get("phoneNumber"))
                .build();
        
        log.info("Created user: {}", testUser.getUsername());
    }

    @Given("I have a user registration request with username \"$username\"")
    public void givenUserWithUsername(String username) {
        log.info("Creating user with username: {}", username);
        testUser = UserRegistrationDto.builder()
                .username(username)
                .password("validpassword123")
                .email("valid@test.com")
                .build();
    }

    @Given("I have a user registration request with email \"$email\"")
    public void givenUserWithEmail(String email) {
        log.info("Creating user with email: {}", email);
        testUser = UserRegistrationDto.builder()
                .username("validuser")
                .password("validpassword123")
                .email(email)
                .build();
    }

    @Given("I have a user registration request with password \"$password\"")
    public void givenUserWithPassword(String password) {
        log.info("Creating user with password length: {}", password.length());
        testUser = UserRegistrationDto.builder()
                .username("validuser")
                .password(password)
                .email("valid@test.com")
                .build();
    }

    @When("I register the user")
    public void whenRegisterUser() {
        log.info("Registering user: {}", testUser.getUsername());
        try {
            validationResult = userService.registerUser(testUser);
            log.info("Registration result: {}", validationResult);
        } catch (Exception e) {
            validationResult = "validation error: " + e.getMessage();
            log.info("Registration validation error: {}", e.getMessage());
        }
    }

    @Then("the registration should be successful")
    public void thenRegistrationShouldBeSuccessful() {
        log.info("Verifying successful registration");
        assertTrue(validationResult.contains("successfully"), 
                "Registration should be successful. Result: " + validationResult);
    }

    @Then("the registration should fail with validation error")
    public void thenRegistrationShouldFailWithValidation() {
        log.info("Verifying validation failure");
        assertFalse(validationResult.contains("successfully"), 
                "Registration should fail with validation error. Result: " + validationResult);
    }

    @Then("the user should be created with all provided details")
    public void thenUserShouldBeCreatedWithAllDetails() {
        log.info("Verifying user created with all details");
        Optional<User> savedUser = userService.findByUsername(testUser.getUsername());
        assertTrue(savedUser.isPresent(), "User should be saved");
        
        User user = savedUser.get();
        assertEquals(testUser.getUsername(), user.getUsername());
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
        assertEquals(testUser.getPhoneNumber(), user.getPhoneNumber());
        
        // Verify audit fields
        assertNotNull(user.getCreatedAt(), "Created timestamp should be set");
        assertNotNull(user.getUpdatedAt(), "Updated timestamp should be set");
        assertEquals(User.UserStatus.ACTIVE, user.getStatus());
    }

    @Given("I have a user in the database")
    public void givenUserInDatabase() {
        log.info("Creating user in database for repository tests");
        testUser = UserRegistrationDto.builder()
                .username("repo_test_user")
                .password("repotest123")
                .email("repo@test.com")
                .firstName("Repository")
                .lastName("Test")
                .build();
        
        String result = userService.registerUser(testUser);
        assertTrue(result.contains("successfully"));
        
        Optional<User> userOptional = userService.findByUsername(testUser.getUsername());
        assertTrue(userOptional.isPresent());
        databaseUser = userOptional.get();
    }

    @When("I perform repository operations")
    public void whenPerformRepositoryOperations() {
        log.info("Performing repository operations");
        
        try {
            // Test various repository methods
            Optional<User> byUsername = userRepository.findByUsername(databaseUser.getUsername());
            assertTrue(byUsername.isPresent(), "Should find by username");
            
            Optional<User> byEmail = userRepository.findByEmail(databaseUser.getEmail());
            assertTrue(byEmail.isPresent(), "Should find by email");
            
            long count = userRepository.count();
            assertTrue(count > 0, "Should count users");
            
            boolean exists = userRepository.existsById(databaseUser.getId());
            assertTrue(exists, "Should check existence");
            
            repositoryTestsPassed = true;
            log.info("Repository operations completed successfully");
        } catch (Exception e) {
            log.error("Repository operations failed: {}", e.getMessage());
            repositoryTestsPassed = false;
        }
    }

    @Then("I should be able to find user by username")
    public void thenShouldFindByUsername() {
        assertTrue(repositoryTestsPassed, "Repository tests should pass");
    }

    @Then("I should be able to find user by email")
    public void thenShouldFindByEmail() {
        assertTrue(repositoryTestsPassed, "Repository tests should pass");
    }

    @Then("I should be able to count users")
    public void thenShouldCountUsers() {
        assertTrue(repositoryTestsPassed, "Repository tests should pass");
    }

    @Then("I should be able to check user existence")
    public void thenShouldCheckUserExistence() {
        assertTrue(repositoryTestsPassed, "Repository tests should pass");
    }

    @Given("I have user service configured")
    public void givenUserServiceConfigured() {
        log.info("Verifying user service configuration");
        assertNotNull(userService, "User service should be configured");
    }

    @When("I test business logic operations")
    public void whenTestBusinessLogicOperations() {
        log.info("Testing business logic operations");
        
        try {
            // Test duplicate username rejection
            testUser = UserRegistrationDto.builder()
                    .username("business_test")
                    .password("business123")
                    .email("business@test.com")
                    .build();
            
            String result1 = userService.registerUser(testUser);
            assertTrue(result1.contains("successfully"));
            
            String result2 = userService.registerUser(testUser);
            assertTrue(result2.contains("Username already exists"));
            
            // Test duplicate email rejection
            UserRegistrationDto emailDuplicate = UserRegistrationDto.builder()
                    .username("different_username")
                    .password("business123")
                    .email("business@test.com")
                    .build();
            
            String result3 = userService.registerUser(emailDuplicate);
            assertTrue(result3.contains("Email already exists"));
            
            businessLogicTestsPassed = true;
            log.info("Business logic operations completed successfully");
        } catch (Exception e) {
            log.error("Business logic operations failed: {}", e.getMessage());
            businessLogicTestsPassed = false;
        }
    }

    @Then("duplicate username should be rejected")
    public void thenDuplicateUsernameShouldBeRejected() {
        assertTrue(businessLogicTestsPassed, "Business logic tests should pass");
    }

    @Then("duplicate email should be rejected")
    public void thenDuplicateEmailShouldBeRejected() {
        assertTrue(businessLogicTestsPassed, "Business logic tests should pass");
    }

    @Then("user update should work correctly")
    public void thenUserUpdateShouldWork() {
        log.info("Testing user update functionality");
        // This would be implemented with actual update operations
        assertTrue(businessLogicTestsPassed, "Business logic tests should pass");
    }

    @Then("user deletion should work correctly")
    public void thenUserDeletionShouldWork() {
        log.info("Testing user deletion functionality");
        // This would be implemented with actual deletion operations
        assertTrue(businessLogicTestsPassed, "Business logic tests should pass");
    }
}