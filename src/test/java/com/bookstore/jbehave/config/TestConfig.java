package com.bookstore.jbehave.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.bookstore.jbehave")
public class TestConfig {
    // Configuration for Spring context during tests
}
