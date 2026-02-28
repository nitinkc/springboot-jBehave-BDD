package com.bookstore.jbehave.controller;

import com.bookstore.jbehave.dto.UserRegistrationDto;
import com.bookstore.jbehave.model.User;
import com.bookstore.jbehave.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "APIs for user registration, retrieval, update, and deletion")
public class UserController {

    public static final String SUCCESSFULLY = "successfully";
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided registration details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid registration data or username/email already exists",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        log.info("Received registration request for username: {}", registrationDto.getUsername());
        
        try {
            String result = userService.registerUser(registrationDto);
            
            if (result.contains(SUCCESSFULLY)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed due to server error");
        }
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        
        Optional<User> user = userService.findById(id);

      return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        
        try {
            List<User> users = userService.findAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(
            @Parameter(description = "Username to search for", required = true) @PathVariable String username) {
        log.info("Fetching user with username: {}", username);
        
        Optional<User> user = userService.findByUsername(username);

      return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "Email address to search for", required = true) @PathVariable String email) {
        log.info("Fetching user with email: {}", email);
        
        Optional<User> user = userService.findByEmail(email);

      return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UserRegistrationDto updateDto) {
        log.info("Updating user with ID: {}", id);
        
        try {
            String result = userService.updateUser(id, updateDto);
            
            if (result.contains(SUCCESSFULLY)) {
                return ResponseEntity.ok(result);
            } else if (result.contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            log.error("Error during user update: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed due to server error");
        }
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        
        try {
            String result = userService.deleteUser(id);
            
            if (result.contains(SUCCESSFULLY)) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error during user deletion: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Deletion failed due to server error");
        }
    }

    @Operation(summary = "Get user count", description = "Returns the total number of registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User count retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64"))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        log.info("Fetching user count");
        
        try {
            long count = userService.countUsers();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error fetching user count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Validate external user", description = "Validates if an external user exists in JSONPlaceholder API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation result",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean"))),
            @ApiResponse(responseCode = "503", description = "External service unavailable",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean")))
    })
    @GetMapping("/validate-external/{externalUserId}")
    public Mono<ResponseEntity<Boolean>> validateExternalUser(
            @Parameter(description = "External user ID from JSONPlaceholder", required = true) @PathVariable Long externalUserId) {
        log.info("Validating external user ID: {}", externalUserId);
        
        return userService.validateExternalUser(externalUserId)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(false));
    }

    @Operation(summary = "Health check", description = "Checks if the user service is healthy and running")
    @ApiResponse(responseCode = "200", description = "Service is healthy",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User service is healthy");
    }
}
