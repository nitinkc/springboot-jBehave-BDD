Meta:
@user_registration

Scenario: User registers with valid details
Given a user with username "john_doe" and password "password123"
When the user registers
Then the registration should be successful
