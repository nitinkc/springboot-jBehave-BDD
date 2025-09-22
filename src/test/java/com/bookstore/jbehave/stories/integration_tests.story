Meta:
@integration
@external_api
@database

Narrative:
As a system integrator
I want to test system integrations
So that all components work together correctly

Scenario: Database integration with H2
Given I have a clean H2 database
When I perform database operations
Then entities should be persisted correctly
And relationships should be maintained
And transactions should work properly

Scenario: External API integration with JSONPlaceholder
Given the JSONPlaceholder API is accessible
When I fetch user data from external API
Then I should receive valid user data
And the data should be properly formatted
And API timeouts should be handled

Scenario: WebClient configuration and usage
Given WebClient is configured
When I make HTTP requests to external services
Then requests should be made successfully
And responses should be parsed correctly
And error handling should work properly

Scenario: Spring context integration
Given Spring application context is loaded
When I access beans and components
Then dependency injection should work
And configuration should be applied
And profiles should be active correctly

Scenario: User registration with external data enrichment
Given I want to register a user with external user ID "2"
When I trigger the registration process
Then the system should fetch external user data
And enrich the local user with external information
And save the combined data to database

Scenario: Cross-layer integration testing
Given I have a complete user registration flow
When I test the entire stack from controller to database
Then HTTP request should be processed correctly
And validation should occur at service layer
And data should be persisted in database
And response should be returned to client

Scenario: Transaction management testing
Given I have operations that span multiple transactions
When I test transaction boundaries
Then successful operations should be committed
And failed operations should be rolled back
And database state should remain consistent

Scenario: Configuration management testing
Given I have different configuration profiles
When I test configuration loading
Then properties should be loaded correctly
And environment-specific settings should apply
And default values should be used when appropriate