Meta:
@user_registration
@basic_functionality

Narrative:
As a new user
I want to register in the system
So that I can access the application features

Scenario: User registers with valid details
Given a user with username "john_doe" and password "password123"
And the user has email "john@example.com"
When the user registers
Then the registration should be successful
And the user should be stored in the system

Scenario: User registration with complete profile
Given a user with complete details:
|username|password|email|firstName|lastName|phoneNumber|
|jane_smith|securepass123|jane@example.com|Jane|Smith|+1987654321|
When the user registers with all details
Then the registration should be successful
And all user details should be saved correctly
