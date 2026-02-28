package com.bookstore.jbehave.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User entity representing a registered user in the system")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Unique username", example = "johndoe")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "User password (hashed)", example = "********", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email address", example = "johndoe@example.com")
    private String email;

    @Column(name = "first_name")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Column(name = "phone_number")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Schema(description = "User's phone number in E.164 format", example = "+12025551234")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Schema(description = "User account status", example = "ACTIVE")
    private UserStatus status = UserStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Timestamp when the user was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the user was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    // External API integration field
    @Column(name = "external_user_id")
    @Schema(description = "External user ID from JSONPlaceholder API", example = "1")
    private Long externalUserId;

    @Schema(description = "User account status enumeration")
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}