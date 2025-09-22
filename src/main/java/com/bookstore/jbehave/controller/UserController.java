package com.bookstore.jbehave.controller;

import com.bookstore.jbehave.dto.UserRegistrationDto;
import com.bookstore.jbehave.model.User;
import com.bookstore.jbehave.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        log.info("Received registration request for username: {}", registrationDto.getUsername());
        
        try {
            String result = userService.registerUser(registrationDto);
            
            if (result.contains("successfully")) {
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        
        Optional<User> user = userService.findById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        log.info("Fetching user with username: {}", username);
        
        Optional<User> user = userService.findByUsername(username);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user with email: {}", email);
        
        Optional<User> user = userService.findByEmail(email);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserRegistrationDto updateDto) {
        log.info("Updating user with ID: {}", id);
        
        try {
            String result = userService.updateUser(id, updateDto);
            
            if (result.contains("successfully")) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        
        try {
            String result = userService.deleteUser(id);
            
            if (result.contains("successfully")) {
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

    @GetMapping("/validate-external/{externalUserId}")
    public Mono<ResponseEntity<Boolean>> validateExternalUser(@PathVariable Long externalUserId) {
        log.info("Validating external user ID: {}", externalUserId);
        
        return userService.validateExternalUser(externalUserId)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(false));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User service is healthy");
    }
}
