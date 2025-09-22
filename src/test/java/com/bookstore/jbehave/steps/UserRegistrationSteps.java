package com.bookstore.jbehave.steps;

import com.bookstore.jbehave.config.TestConfig;
import com.bookstore.jbehave.dto.UserRegistrationDto;
import com.bookstore.jbehave.model.User;
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
public class UserRegistrationSteps {

    @Autowired
    private UserService userService;

    private UserRegistrationDto currentUser;
    private String registrationResult;
    private User retrievedUser;
    private long initialUserCount;

    @BeforeScenario
    public void setUp() {
        log.info("Setting up test scenario");
        currentUser = null;
        registrationResult = null;
        retrievedUser = null;
    }

    @Given("a user with username \"$username\" and password \"$password\"")
    public void givenUserWithUsernameAndPassword(String username, String password) {
        log.info("Creating user with username: {} and password: [HIDDEN]", username);
        currentUser = UserRegistrationDto.builder()
                .username(username)
                .password(password)
                .email(username + "@test.com") // Default email for basic scenarios
                .build();
    }

    @Given("the user has email \"$email\"")
    public void givenUserHasEmail(String email) {
        log.info("Setting user email: {}", email);
        if (currentUser != null) {
            currentUser.setEmail(email);
        } else {
            fail("Current user not initialized");
        }
    }

    @Given("a user with complete details: $userTable")
    public void givenUserWithCompleteDetails(ExamplesTable userTable) {
        log.info("Creating user with complete details from table");
        List<Map<String, String>> rows = userTable.getRows();
        Map<String, String> userRow = rows.get(0);
        
        currentUser = UserRegistrationDto.builder()
                .username(userRow.get("username"))
                .password(userRow.get("password"))
                .email(userRow.get("email"))
                .firstName(userRow.get("firstName"))
                .lastName(userRow.get("lastName"))
                .phoneNumber(userRow.get("phoneNumber"))
                .build();
    }

    @Given("I have a new user with username \"$username\" and email \"$email\"")
    public void givenNewUserWithUsernameAndEmail(String username, String email) {
        log.info("Creating new user with username: {} and email: {}", username, email);
        currentUser = UserRegistrationDto.builder()
                .username(username)
                .password("testpassword123")
                .email(email)
                .build();
    }

    @Given("I have registered a user with username \"$username\"")
    public void givenRegisteredUserWithUsername(String username) {
        log.info("Pre-registering user with username: {}", username);
        UserRegistrationDto user = UserRegistrationDto.builder()
                .username(username)
                .password("testpassword123")
                .email(username + "@test.com")
                .build();
        
        String result = userService.registerUser(user);
        assertTrue(result.contains("successfully"), "Pre-registration should succeed");
    }

    @Given("I know the current user count")
    public void givenCurrentUserCount() {
        initialUserCount = userService.countUsers();
        log.info("Initial user count: {}", initialUserCount);
    }

    @When("the user registers")
    public void whenUserRegisters() {
        log.info("Registering user: {}", currentUser.getUsername());
        assertNotNull(currentUser, "Current user should be initialized");
        registrationResult = userService.registerUser(currentUser);
        log.info("Registration result: {}", registrationResult);
    }

    @When("the user registers with all details")
    public void whenUserRegistersWithAllDetails() {
        log.info("Registering user with all details: {}", currentUser.getUsername());
        assertNotNull(currentUser, "Current user should be initialized");
        registrationResult = userService.registerUser(currentUser);
        log.info("Registration result: {}", registrationResult);
    }

    @When("I register the user")
    public void whenRegisterUser() {
        whenUserRegisters();
    }

    @When("I search for the user by username")
    public void whenSearchUserByUsername() {
        log.info("Searching for user by username");
        assertNotNull(currentUser, "Current user should be initialized");
        Optional<User> userOptional = userService.findByUsername(currentUser.getUsername());
        if (userOptional.isPresent()) {
            retrievedUser = userOptional.get();
            log.info("Found user: {}", retrievedUser.getUsername());
        } else {
            log.warn("User not found with username: {}", currentUser.getUsername());
        }
    }

    @When("I try to register another user with the same username \"$username\"")
    public void whenRegisterDuplicateUser(String username) {
        log.info("Attempting to register duplicate username: {}", username);
        UserRegistrationDto duplicateUser = UserRegistrationDto.builder()
                .username(username)
                .password("anotherpassword123")
                .email("another@test.com")
                .build();
        
        registrationResult = userService.registerUser(duplicateUser);
        log.info("Duplicate registration result: {}", registrationResult);
    }

    @When("I register a new user with username \"$username\"")
    public void whenRegisterNewUser(String username) {
        log.info("Registering new user: {}", username);
        currentUser = UserRegistrationDto.builder()
                .username(username)
                .password("testpassword123")
                .email(username + "@test.com")
                .build();
        
        registrationResult = userService.registerUser(currentUser);
    }

    @Then("the registration should be successful")
    public void thenRegistrationShouldBeSuccessful() {
        log.info("Verifying successful registration");
        assertNotNull(registrationResult, "Registration result should not be null");
        assertTrue(registrationResult.contains("successfully"), 
                "Registration should be successful. Actual result: " + registrationResult);
    }

    @Then("the user should be stored in the database")
    public void thenUserShouldBeStoredInDatabase() {
        log.info("Verifying user is stored in database");
        assertNotNull(currentUser, "Current user should be initialized");
        Optional<User> savedUser = userService.findByUsername(currentUser.getUsername());
        assertTrue(savedUser.isPresent(), "User should be found in database");
        assertEquals(currentUser.getUsername(), savedUser.get().getUsername());
        assertEquals(currentUser.getEmail(), savedUser.get().getEmail());
    }

    @Then("the user should be stored in the system")
    public void thenUserShouldBeStoredInSystem() {
        thenUserShouldBeStoredInDatabase();
    }

    @Then("the user should be found")
    public void thenUserShouldBeFound() {
        log.info("Verifying user was found");
        assertNotNull(retrievedUser, "Retrieved user should not be null");
    }

    @Then("the user details should be correct")
    public void thenUserDetailsShouldBeCorrect() {
        log.info("Verifying user details are correct");
        assertNotNull(retrievedUser, "Retrieved user should not be null");
        assertNotNull(currentUser, "Current user should not be null");
        assertEquals(currentUser.getUsername(), retrievedUser.getUsername());
        assertEquals(currentUser.getEmail(), retrievedUser.getEmail());
    }

    @Then("all user details should be saved correctly")
    public void thenAllUserDetailsShouldBeSavedCorrectly() {
        log.info("Verifying all user details are saved correctly");
        Optional<User> savedUser = userService.findByUsername(currentUser.getUsername());
        assertTrue(savedUser.isPresent(), "User should be found in database");
        
        User user = savedUser.get();
        assertEquals(currentUser.getUsername(), user.getUsername());
        assertEquals(currentUser.getEmail(), user.getEmail());
        assertEquals(currentUser.getFirstName(), user.getFirstName());
        assertEquals(currentUser.getLastName(), user.getLastName());
        assertEquals(currentUser.getPhoneNumber(), user.getPhoneNumber());
    }

    @Then("the registration should fail with \"$expectedMessage\"")
    public void thenRegistrationShouldFailWith(String expectedMessage) {
        log.info("Verifying registration failure with message: {}", expectedMessage);
        assertNotNull(registrationResult, "Registration result should not be null");
        assertTrue(registrationResult.contains(expectedMessage), 
                "Registration should fail with expected message. Expected: " + expectedMessage + 
                ", Actual: " + registrationResult);
    }

    @Then("the user count should increase by $increment")
    public void thenUserCountShouldIncreaseBy(int increment) {
        log.info("Verifying user count increased by: {}", increment);
        long currentCount = userService.countUsers();
        assertEquals(initialUserCount + increment, currentCount, 
                "User count should increase by " + increment);
    }

    @AfterScenario
    public void tearDown() {
        log.info("Cleaning up after test scenario");
        // Cleanup is handled by @Transactional rollback in test configuration
    }
}

