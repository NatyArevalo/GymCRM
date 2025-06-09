@component
Feature: Update Trainer

  Background:
    Given a trainer with name "Jhon" and lastname "Doe"
    And the specialization is "FUNCTIONAL"
    Then the trainer should be created

  Scenario: Updating trainer with valid data
    When the trainer with username "Jhon.Doe" the specialization is updated to "AGILITY"
    Then the trainer's specialization should be updated successfully

  Scenario: Updating trainer with missing name
    When the trainer with username "Jhon.Doe" name is updated to ""
    Then the trainer's name should not be updated

  Scenario: Updating trainer with invalid specialization
    When the trainer with username "Jhon.Doe" is updated with specialization "INVALID_SPECIALIZATION"
    Then the trainer's specialization should not be updated