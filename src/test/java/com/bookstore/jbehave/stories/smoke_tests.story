Meta:
@smoke
@user_management
@high_priority

Narrative:
As a system administrator
I want to ensure core user functionality works
So that the basic system operations are reliable

Scenario: Health check endpoint responds successfully
Given the user service is running
When I check the health endpoint
Then the service should respond with healthy status

Scenario: User registration with minimal valid data
Given I have a new user with username "smoke_user" and email "smoke@test.com"
When I register the user
Then the registration should be successful
And the user should be stored in the database

Scenario: User can be retrieved after registration
Given I have registered a user with username "retrieve_user"
When I search for the user by username
Then the user should be found
And the user details should be correct

Scenario: System handles duplicate username gracefully
Given I have registered a user with username "duplicate_user"
When I try to register another user with the same username "duplicate_user"
Then the registration should fail with "Username already exists"

Scenario: User count increases after registration
Given I know the current user count
When I register a new user with username "count_user"
Then the user count should increase by 1