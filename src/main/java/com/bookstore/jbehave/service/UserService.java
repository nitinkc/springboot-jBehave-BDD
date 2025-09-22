package com.bookstore.jbehave.service;

import com.bookstore.jbehave.dto.ExternalUserDto;
import com.bookstore.jbehave.dto.UserRegistrationDto;
import com.bookstore.jbehave.model.User;
import com.bookstore.jbehave.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ExternalUserService externalUserService;

    public String registerUser(UserRegistrationDto registrationDto) {
        log.info("Registering user: {}", registrationDto.getUsername());

        // Check if username already exists
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            log.warn("Username already exists: {}", registrationDto.getUsername());
            return "Username already exists";
        }

        // Check if email already exists
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", registrationDto.getEmail());
            return "Email already exists";
        }

        User user = User.builder()
                .username(registrationDto.getUsername())
                .password(registrationDto.getPassword()) // In real app, this should be encrypted
                .email(registrationDto.getEmail())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .phoneNumber(registrationDto.getPhoneNumber())
                .externalUserId(registrationDto.getExternalUserId())
                .build();

        // If importing from external API, enrich with external data
        if (registrationDto.isImportFromExternal() && registrationDto.getExternalUserId() != null) {
            return registerUserWithExternalData(user, registrationDto.getExternalUserId());
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        return "User registered successfully!";
    }

    private String registerUserWithExternalData(User user, Long externalUserId) {
        try {
            ExternalUserDto externalUser = externalUserService.getUserById(externalUserId).block();
            if (externalUser != null) {
                // Enrich user with external data
                user.setEmail(externalUser.getEmail());
                user.setPhoneNumber(externalUser.getPhone());
                if (externalUser.getName() != null && !externalUser.getName().isEmpty()) {
                    String[] nameParts = externalUser.getName().split(" ", 2);
                    user.setFirstName(nameParts[0]);
                    if (nameParts.length > 1) {
                        user.setLastName(nameParts[1]);
                    }
                }
                user.setExternalUserId(externalUser.getId());
                
                User savedUser = userRepository.save(user);
                log.info("User registered with external data, ID: {}", savedUser.getId());
                return "User registered successfully with external data!";
            } else {
                log.warn("External user not found for ID: {}", externalUserId);
                return "External user not found";
            }
        } catch (Exception e) {
            log.error("Error fetching external user data: {}", e.getMessage());
            // Fallback to regular registration
            User savedUser = userRepository.save(user);
            return "User registered successfully (external data unavailable)!";
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        log.debug("Finding all users");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    public String updateUser(Long id, UserRegistrationDto updateDto) {
        log.info("Updating user with ID: {}", id);
        
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warn("User not found for update, ID: {}", id);
            return "User not found";
        }

        User user = userOptional.get();
        
        // Check if new username is taken by another user
        if (!user.getUsername().equals(updateDto.getUsername())) {
            Optional<User> existingUser = userRepository.findByUsername(updateDto.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                return "Username already exists";
            }
        }

        // Check if new email is taken by another user
        if (!user.getEmail().equals(updateDto.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(updateDto.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                return "Email already exists";
            }
        }

        user.setUsername(updateDto.getUsername());
        user.setEmail(updateDto.getEmail());
        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setPhoneNumber(updateDto.getPhoneNumber());

        userRepository.save(user);
        log.info("User updated successfully, ID: {}", id);
        return "User updated successfully!";
    }

    public String deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.warn("User not found for deletion, ID: {}", id);
            return "User not found";
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully, ID: {}", id);
        return "User deleted successfully!";
    }

    @Transactional(readOnly = true)
    public long countUsers() {
        long count = userRepository.count();
        log.debug("Total users count: {}", count);
        return count;
    }

    public Mono<Boolean> validateExternalUser(Long externalUserId) {
        log.info("Validating external user ID: {}", externalUserId);
        return externalUserService.userExists(externalUserId);
    }
}
