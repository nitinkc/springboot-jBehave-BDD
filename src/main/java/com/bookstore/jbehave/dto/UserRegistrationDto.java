package com.bookstore.jbehave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration request data")
public class UserRegistrationDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Unique username for the account", example = "johndoe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "Password for the account (min 8 characters)", example = "securePassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address", example = "johndoe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Schema(description = "Phone number in E.164 format", example = "+12025551234")
    private String phoneNumber;

    @Builder.Default
    @Schema(description = "Flag to import user data from external JSONPlaceholder API", example = "false")
    private boolean importFromExternal = false;

    @Schema(description = "External user ID from JSONPlaceholder API (required if importFromExternal is true)", example = "1")
    private Long externalUserId;
}