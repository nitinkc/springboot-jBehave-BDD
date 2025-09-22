package com.bookstore.jbehave.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
@ComponentScan(basePackages = "com.bookstore.jbehave")
@Profile("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "logging.level.com.bookstore.jbehave=DEBUG",
        "logging.level.org.springframework.web.reactive.function.client=DEBUG"
})
@Transactional
public class TestConfig {
    // Configuration for Spring context during tests
    // @Transactional ensures test data cleanup between scenarios
}
