package com.bookstore.jbehave.service;

import com.bookstore.jbehave.dto.ExternalUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Service for integrating with JSONPlaceholder API
 * https://jsonplaceholder.typicode.com/
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalUserService {

    private final WebClient webClient;

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public Mono<ExternalUserDto> getUserById(Long id) {
        log.info("Fetching external user with ID: {}", id);
        return webClient.get()
                .uri(BASE_URL + "/users/{id}", id)
                .retrieve()
                .bodyToMono(ExternalUserDto.class)
                .timeout(TIMEOUT)
                .doOnSuccess(user -> log.info("Successfully fetched external user: {}", user.getUsername()))
                .doOnError(error -> log.error("Error fetching external user with ID {}: {}", id, error.getMessage()));
    }

    public Flux<ExternalUserDto> getAllUsers() {
        log.info("Fetching all external users");
        return webClient.get()
                .uri(BASE_URL + "/users")
                .retrieve()
                .bodyToFlux(ExternalUserDto.class)
                .timeout(TIMEOUT)
                .doOnComplete(() -> log.info("Successfully fetched all external users"))
                .doOnError(error -> log.error("Error fetching external users: {}", error.getMessage()));
    }

    public Mono<ExternalUserDto> createUser(ExternalUserDto userDto) {
        log.info("Creating external user: {}", userDto.getUsername());
        return webClient.post()
                .uri(BASE_URL + "/users")
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(ExternalUserDto.class)
                .timeout(TIMEOUT)
                .doOnSuccess(user -> log.info("Successfully created external user with ID: {}", user.getId()))
                .doOnError(error -> log.error("Error creating external user: {}", error.getMessage()));
    }

    public Mono<Boolean> userExists(Long id) {
        return getUserById(id)
                .map(user -> true)
                .onErrorReturn(false);
    }
}