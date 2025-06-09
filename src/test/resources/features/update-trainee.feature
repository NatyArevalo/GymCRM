@component
Feature: Update Trainee Information

  Background:
    Given a trainee with name "Jhon" and lastname "Doe"
    And the date of birth is "1995-05-10" and the address is "123 Main Street"
    Then the trainee is created successfully

  Scenario: Update trainee's address with valid data
    When the trainee with username "Jhon.Doe" address is updated to "456 Elm Street"
    Then the trainee's address should be updated successfully

  Scenario: Update trainee's name with missing first name
    When the trainee with username "Jhon.Doe" name is updated to ""
    Then the trainee's name should not be updated

  Scenario: Update trainee's date of birth with invalid date
    When the trainee with username "Jhon.Doe" date of birth is updated to "1980-06-10"
    Then the trainee's date of birth should not be updated