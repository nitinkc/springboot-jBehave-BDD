Meta:
@component
@user_registration
@validation

Narrative:
As a developer
I want to test individual components thoroughly
So that each component works correctly in isolation

Scenario: User registration with all valid fields
Given I have a complete user registration request:
|username|password|email|firstName|lastName|phoneNumber|
|john_doe|password123|john@example.com|John|Doe|+1234567890|
When I register the user
Then the registration should be successful
And the user should be created with all provided details

Scenario: User registration validation for username
Given I have a user registration request with username "<username>"
When I register the user
Then the registration should <result>

Examples:
|username|result|
|ab|fail with validation error|
|valid_user|be successful|
|user_with_very_long_name_that_exceeds_fifty_characters|fail with validation error|

Scenario: User registration validation for email
Given I have a user registration request with email "<email>"
When I register the user
Then the registration should <result>

Examples:
|email|result|
|invalid-email|fail with validation error|
|valid@email.com|be successful|
||fail with validation error|

Scenario: User registration validation for password
Given I have a user registration request with password "<password>"
When I register the user
Then the registration should <result>

Examples:
|password|result|
|short|fail with validation error|
|validpassword123|be successful|
||fail with validation error|

Scenario: User repository operations
Given I have a user in the database
When I perform repository operations
Then I should be able to find user by username
And I should be able to find user by email
And I should be able to count users
And I should be able to check user existence

Scenario: User service business logic validation
Given I have user service configured
When I test business logic operations
Then duplicate username should be rejected
And duplicate email should be rejected
And user update should work correctly
And user deletion should work correctly