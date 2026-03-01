Meta:
@regression
@full_system
@comprehensive

Narrative:
As a QA engineer
I want to run comprehensive regression tests
So that system changes don't break existing functionality

Scenario: End-to-end user lifecycle management
Given the system is clean with no existing users
When I register a user with username "regression_user"
And I retrieve the user by username
And I update the user's email to "updated@test.com"
And I retrieve the user again
Then the user should have the updated email
And the user count should be 1
When I delete the user
Then the user should not be found
And the user count should be 0

Scenario: Concurrent user registrations
Given I have multiple user registration requests:
|username|email|
|user1|user1@test.com|
|user2|user2@test.com|
|user3|user3@test.com|
When I register all users concurrently
Then all users should be registered successfully
And I should be able to retrieve all users
And the user count should be 3

Scenario: User registration with external API integration
Given the external API is available
When I register a user with external user ID "1"
Then the user should be registered with external data
And the user should have external user information
And the external user ID should be stored

Scenario: System resilience with external API unavailable
Given the external API is unavailable
When I register a user with external user ID "999"
Then the user should be registered without external data
And a fallback message should be returned

Scenario: Data validation across multiple operations
Given I have registered users with various data
When I perform bulk operations
Then data integrity should be maintained
And validation rules should be enforced consistently
And audit timestamps should be updated correctly

Scenario: API endpoint comprehensive testing
Given the user service is running
When I test all REST endpoints
Then GET users endpoint should return all users
And GET user by ID endpoint should return specific user
And POST register endpoint should create new user
And PUT update endpoint should update user
And DELETE endpoint should delete user
And GET count endpoint should return user count
And GET health endpoint should return healthy status

Scenario: Error handling and edge cases
Given I have various edge case scenarios
When I test error handling
Then invalid user ID should return 404
And malformed requests should return 400
And system should handle database errors gracefully
And logging should capture all important events

Scenario: File-based batch user registration testing
Meta:
@csv_test
@file_based
Given I load user registration test data from "testdata/user_registration_requests.csv"
And I load expected responses from "testdata/user_registration_expected_responses.csv"
When I execute all registration requests from the file
Then the results should match the expected responses
And I should have processed 5 records
