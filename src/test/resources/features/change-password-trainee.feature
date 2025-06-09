@component
Feature: Change Password for Trainee

    Background:
        Given a trainee with name "Jhon" and lastname "Doe"
        And the date of birth is "1995-05-10" and the address is "123 Main Street"
        Then the trainee is created successfully

    Scenario: Change password with valid data
        When the trainee with username "Jhon.Doe" and password is changed to "newPassword456"
        Then the password should be updated successfully

    Scenario: Change password with incorrect old password
        When the trainee with username "Jhon.Doe" attempts to change the password with old password "wrongOldPassword"
        Then the returned result should be null
        And the password should not be changed