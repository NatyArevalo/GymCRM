@component
Feature: Create new Trainee

  Scenario: Create a trainee with valid data
    Given a trainee with name "Jhon" and lastname "Doe"
    And the date of birth is "1995-05-10" and the address is "123 Main Street"
    When trainee is created
    Then the trainee should be created successfully

  Scenario: Create a trainee with missing name
    Given a trainee with name "" and lastname "Doe"
    And the date of birth is "1995-05-10" and the address is "123 Main Street"
    When trainee creation is attempted
    Then trainee creation should fail and result should be null

  Scenario: Create a trainee with invalid date of birth
    Given a trainee with name "Jhon" and lastname "Doe"
    And the date of birth is "2026-06-10" and the address is "123 Main Street"
    When trainee creation is attempted
    Then trainee creation should fail and result should be null