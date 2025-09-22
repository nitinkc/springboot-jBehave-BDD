package com.bookstore.jbehave.steps;

import com.bookstore.jbehave.controller.UserController;
import com.bookstore.jbehave.dto.UserRegistrationDto;
import com.bookstore.jbehave.model.User;
import com.bookstore.jbehave.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import com.bookstore.jbehave.config.TestConfig;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.profiles.active=test"})
@Slf4j
public class SmokeTestSteps {

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    private String healthStatus;
    private UserRegistrationDto testUser;
    private String operationResult;
    private long initialCount;
    private long finalCount;
    private User foundUser;

    @Given("the user service is running")
    public void givenUserServiceIsRunning() {
        log.info("Verifying user service is running");
        assertNotNull(userService, "User service should be available");
    }

    @When("I check the health endpoint")
    public void whenCheckHealthEndpoint() {
        log.info("Checking health endpoint");
        ResponseEntity<String> response = userController.healthCheck();
        healthStatus = response.getBody();
        log.info("Health status: {}", healthStatus);
    }

    @Then("the service should respond with healthy status")
    public void thenServiceShouldRespondHealthy() {
        log.info("Verifying healthy status response");
        assertNotNull(healthStatus, "Health status should not be null");
        assertTrue(healthStatus.contains("healthy"), "Service should be healthy");
    }

    @Given("I have a new user with username \"$username\" and email \"$email\"")
    public void givenNewUserWithUsernameAndEmail(String username, String email) {
        log.info("Creating test user with username: {} and email: {}", username, email);
        testUser = UserRegistrationDto.builder()
                .username(username)
                .password("smoketest123")
                .email(email)
                .firstName("Smoke")
                .lastName("Test")
                .build();
    }

    @When("I register the user")
    public void whenRegisterUser() {
        log.info("Registering test user: {}", testUser.getUsername());
        operationResult = userService.registerUser(testUser);
        log.info("Registration result: {}", operationResult);
    }

    @Then("the registration should be successful")
    public void thenRegistrationShouldBeSuccessful() {
        log.info("Verifying successful registration");
        assertNotNull(operationResult, "Operation result should not be null");
        assertTrue(operationResult.contains("successfully"), 
                "Registration should be successful. Result: " + operationResult);
    }

    @Then("the user should be stored in the database")
    public void thenUserShouldBeStoredInDatabase() {
        log.info("Verifying user is stored in database");
        var savedUser = userService.findByUsername(testUser.getUsername());
        assertTrue(savedUser.isPresent(), "User should be found in database");
        assertEquals(testUser.getEmail(), savedUser.get().getEmail());
    }

    @Given("I have registered a user with username \"$username\"")
    public void givenRegisteredUser(String username) {
        log.info("Pre-registering user: {}", username);
        testUser = UserRegistrationDto.builder()
                .username(username)
                .password("testpass123")
                .email(username + "@test.com")
                .build();
        
        operationResult = userService.registerUser(testUser);
        assertTrue(operationResult.contains("successfully"));
    }

    @When("I search for the user by username")
    public void whenSearchUserByUsername() {
        log.info("Searching for user: {}", testUser.getUsername());
        var userOptional = userService.findByUsername(testUser.getUsername());
        foundUser = userOptional.orElse(null);
    }

    @Then("the user should be found")
    public void thenUserShouldBeFound() {
        log.info("Verifying user was found");
        assertNotNull(foundUser, "User should be found");
    }

    @Then("the user details should be correct")
    public void thenUserDetailsShouldBeCorrect() {
        log.info("Verifying user details");
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
    }

    @When("I try to register another user with the same username \"$username\"")
    public void whenRegisterDuplicateUser(String username) {
        log.info("Attempting duplicate registration: {}", username);
        UserRegistrationDto duplicateUser = UserRegistrationDto.builder()
                .username(username)
                .password("different123")
                .email("different@test.com")
                .build();
        
        operationResult = userService.registerUser(duplicateUser);
    }

    @Then("the registration should fail with \"$expectedMessage\"")
    public void thenRegistrationShouldFail(String expectedMessage) {
        log.info("Verifying registration failure");
        assertTrue(operationResult.contains(expectedMessage), 
                "Should fail with: " + expectedMessage + ", but got: " + operationResult);
    }

    @Given("I know the current user count")
    public void givenCurrentUserCount() {
        initialCount = userService.countUsers();
        log.info("Initial user count: {}", initialCount);
    }

    @When("I register a new user with username \"$username\"")
    public void whenRegisterNewUser(String username) {
        log.info("Registering new user: {}", username);
        testUser = UserRegistrationDto.builder()
                .username(username)
                .password("count123")
                .email(username + "@count.com")
                .build();
        
        operationResult = userService.registerUser(testUser);
    }

    @Then("the user count should increase by $increment")
    public void thenUserCountShouldIncrease(int increment) {
        finalCount = userService.countUsers();
        log.info("Final user count: {}, expected increase: {}", finalCount, increment);
        assertEquals(initialCount + increment, finalCount, 
                "User count should increase by " + increment);
    }
}