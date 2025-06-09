@component
Feature: Create Trainer

  Scenario: Create a trainer with valid data
    Given a trainer with name "Jhon" and lastname "Doe"
    And the specialization is "FUNCTIONAL"
    When trainer is created
    Then the trainer should be created successfully

  Scenario: Create a trainer with missing name
    Given a trainer with name "" and lastname "Doe"
    And the specialization is "FUNCTIONAL"
    When trainer creation is attempted
    Then trainer creation should fail and result should be null

  Scenario: Create a trainer with invalid specialization
    Given a trainer with name "Jhon" and lastname "Doe"
    And the specialization is "INVALID_SPECIALIZATION"
    When trainer creation is attempted
    Then trainer creation should fail and result should be null