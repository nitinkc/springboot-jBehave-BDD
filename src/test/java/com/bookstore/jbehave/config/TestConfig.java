package com.bookstore.jbehave.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@ComponentScan(basePackages = "com.bookstore.jbehave")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "logging.level.com.bookstore.jbehave=DEBUG",
        "logging.level.org.springframework.web.reactive.function.client=DEBUG"
})
public class TestConfig {
    // Configuration for Spring context during tests
    // Tests perform explicit cleanup between scenarios
}
